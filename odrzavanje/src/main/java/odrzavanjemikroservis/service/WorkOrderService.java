package odrzavanjemikroservis.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityNotFoundException;
import odrzavanjemikroservis.client.SmestajClient;
import odrzavanjemikroservis.config.RabbitMQConfig;
import odrzavanjemikroservis.dtos.MaintenanceStatisticsDto;
import odrzavanjemikroservis.dtos.UnitDto;
import odrzavanjemikroservis.dtos.WorkOrderDto;
import odrzavanjemikroservis.exception.ServiceUnavailableException;
import odrzavanjemikroservis.entity.MaintenanceTask;
import odrzavanjemikroservis.entity.WorkOrder;
import odrzavanjemikroservis.entity.Worker;
import odrzavanjemikroservis.entityMapper.EntityMapperFactory;
import odrzavanjemikroservis.entityMapper.MapperFactory;
import odrzavanjemikroservis.entityMapper.WorkOrderMapper;
import odrzavanjemikroservis.enums.WorkOrderStatus;
import odrzavanjemikroservis.observer.WorkOrderEventPublisher;
import odrzavanjemikroservis.repository.MaintenanceTaskRepository;
import odrzavanjemikroservis.repository.WorkOrderRepository;
import odrzavanjemikroservis.repository.WorkerRepository;
import org.raflab.sharedevents.WorkOrderCreatedEvent;
import org.raflab.sharedevents.WorkOrderStatusChangedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class WorkOrderService {

    private static final Logger log = LoggerFactory.getLogger(WorkOrderService.class);

    @Autowired
    private MaintenanceTaskRepository maintenanceTaskRepository;
    @Autowired
    private WorkOrderRepository workOrderRepository;
    @Autowired
    private WorkerRepository workerRepository;
    @Autowired
    private MaintenanceLogService maintenanceLogService;
    @Qualifier("odrzavanjemikroservis.client.SmestajClient")
    @Autowired
    private SmestajClient smestajClient;
    @Autowired
    private MapperFactory mapperFactory;
    @Autowired
    private WorkOrderEventPublisher workOrderEventPublisher;
    @Autowired
    @Lazy
    private WorkOrderService self;
    @Autowired
    private WorkerService workerService;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PostConstruct
    public void init() {
        workOrderEventPublisher.subscribe(maintenanceLogService);
    }

    @Retry(name = "smestajService")
    @CircuitBreaker(name = "smestajService", fallbackMethod = "unitExistsFallback")
    public Boolean unitExists(Long unitId) {
        return smestajClient.unitExists(unitId);
    }

    public Boolean unitExistsFallback(Long unitId, Throwable t) {
        Throwable cause = (t instanceof java.lang.reflect.UndeclaredThrowableException) ? t.getCause() : t;
        if (cause instanceof EntityNotFoundException e)     throw e;
        if (cause instanceof IllegalArgumentException e)    throw e;
        if (cause instanceof IllegalStateException e)       throw e;
        if (cause instanceof ServiceUnavailableException e) throw e;
        if (cause.getClass().getSimpleName().equals("CallNotPermittedException")) {
            log.warn("[CB OPEN] Smestaj servis CB je otvoren pri proveri unitId={}", unitId);
            throw new ServiceUnavailableException("Smestaj servis nije dostupan (Circuit Breaker je otvoren).");
        }
        log.warn("[CIRCUIT BREAKER] Smestaj servis nije dostupan pri proveri unitId={}: {}", unitId, cause.getMessage());
        throw new ServiceUnavailableException("Smestaj servis trenutno nije dostupan.");
    }

    public Boolean isUnitReserved(Long unitId) {
        return false;
    }

    @Retry(name = "smestajService")
    @CircuitBreaker(name = "smestajService", fallbackMethod = "getUnitByIdFallback")
    public UnitDto getUnitById(Long unitId) {
        return smestajClient.getUnitById(unitId);
    }

    public UnitDto getUnitByIdFallback(Long unitId, Throwable t) {
        Throwable cause = (t instanceof java.lang.reflect.UndeclaredThrowableException) ? t.getCause() : t;
        if (cause instanceof EntityNotFoundException e)     throw e;
        if (cause instanceof IllegalArgumentException e)    throw e;
        if (cause instanceof IllegalStateException e)       throw e;
        if (cause instanceof ServiceUnavailableException e) throw e;
        if (cause.getClass().getSimpleName().equals("CallNotPermittedException")) {
            log.warn("[CB OPEN] Smestaj servis CB je otvoren pri dohvatu unitId={}", unitId);
            throw new ServiceUnavailableException("Smestaj servis nije dostupan (Circuit Breaker je otvoren).");
        }
        log.warn("[CIRCUIT BREAKER] Smestaj servis nije dostupan pri dohvatu unitId={}: {}", unitId, cause.getMessage());
        throw new ServiceUnavailableException("Smestaj servis trenutno nije dostupan.");
    }

    private WorkOrderMapper getWorkorderMapper() {
        EntityMapperFactory<WorkOrder, WorkOrderDto> mapper = mapperFactory.getMapper(WorkOrder.class);
        if (mapper instanceof WorkOrderMapper workOrderMapper) {
            return workOrderMapper;
        }
        return null;
    }

    public WorkOrderDto save(WorkOrderDto workOrderDto) {
        Long unitId = workOrderDto.getUnitId();

        if (!self.unitExists(unitId)) {
            throw new EntityNotFoundException("Jedinica sa id " + unitId + " ne postoji.");
        }

        workOrderDto.setStatus(WorkOrderStatus.PENDING);

        Worker worker = workerRepository.findWorkerById(workOrderDto.getWorkerId());

        WorkOrderMapper workOrderMapper = getWorkorderMapper();
        MaintenanceTask task = null;
        if (workOrderDto.getMaintenanceTaskId() != null) {
            task = maintenanceTaskRepository.findTaskById(workOrderDto.getMaintenanceTaskId());
        }

        if (workOrderDto.getScheduledFor().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Datum zakazivanja mora biti u budućnosti.");
        }

        workerService.checkWorkerAvailability(worker.getId(), workOrderDto.getScheduledFor());

        WorkOrder order = workOrderRepository.save(workOrderMapper.toEntityWithDependencies(workOrderDto, task, worker));

        try {
            rabbitTemplate.convertAndSend(RabbitMQConfig.ODRZAVANJE_EXCHANGE, "workorder.created",
                    new WorkOrderCreatedEvent(
                            order.getId(),
                            order.getUnitId(),
                            worker != null ? worker.getId() : null,
                            task != null ? task.getId() : null,
                            order.getScheduledFor(),
                            task != null ? task.getTaskName() : null,
                            task != null ? task.getEstimatedDurationHours() : 1
                    ));
            log.info("[RabbitMQ] Objavljen dogadjaj workorder.created za nalog id={}", order.getId());
        } catch (AmqpException e) {
            log.warn("[RabbitMQ] Nije moguce objaviti dogadjaj workorder.created: {}", e.getMessage());
        }

        return workOrderMapper.toDto(order);
    }

    public List<WorkOrderDto> getAllWorkOrders() {
        WorkOrderMapper workOrderMapper = getWorkorderMapper();
        List<WorkOrder> workOrders = workOrderRepository.findAll();
        List<WorkOrderDto> workOrderDtos = new ArrayList<>();
        for (WorkOrder workOrder : workOrders) {
            workOrderDtos.add(workOrderMapper.toDto(workOrder));
        }
        return workOrderDtos;
    }

    public String getCurrentStatus(Long orderId) {
        WorkOrder workOrder = workOrderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Radni nalog sa id " + orderId + " nije pronađen."));
        return workOrder.getStatus().toString();
    }

    public void updateStatus(Long orderId, String notes) {
        WorkOrder workOrder = workOrderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Radni nalog sa id " + orderId + " nije pronađen."));
        WorkOrderStatus currStatus = workOrder.getStatus();

        switch (currStatus) {
            case PENDING:
                workOrder.setStatus(WorkOrderStatus.IN_PROGRESS);
                workOrderRepository.save(workOrder);
                break;
            case IN_PROGRESS:
                workOrder.setStatus(WorkOrderStatus.COMPLETED);
                workOrderRepository.save(workOrder);
                workOrderEventPublisher.notify(workOrder, notes);
                try {
                    rabbitTemplate.convertAndSend(RabbitMQConfig.ODRZAVANJE_EXCHANGE, "workorder.completed",
                            new WorkOrderStatusChangedEvent(
                                    workOrder.getId(),
                                    workOrder.getUnitId(),
                                    currStatus.name(),
                                    WorkOrderStatus.COMPLETED.name(),
                                    notes
                            ));

                    log.info("[RabbitMQ] Objavljen dogadjaj workorder.completed za nalog id={}", workOrder.getId());
                } catch (AmqpException e) {
                    log.warn("[RabbitMQ] Nije moguce objaviti dogadjaj workorder.completed: {}", e.getMessage());
                }
                break;
            case COMPLETED:
                throw new IllegalStateException("Radni nalog je već završen.");
            default:
                workOrder.setStatus(WorkOrderStatus.PENDING);
                workOrderRepository.save(workOrder);
        }
    }

    public void cancelAllForUnit(Long unitId) {
        List<WorkOrder> orders = workOrderRepository.findByUnitId(unitId);
        for (WorkOrder order : orders) {
            if (order.getStatus() != WorkOrderStatus.COMPLETED && order.getStatus() != WorkOrderStatus.CANCELLED) {
                order.setStatus(WorkOrderStatus.CANCELLED);
                workOrderRepository.save(order);
                log.info("[Smestaj] Otkazan radni nalog id={} zbog brisanja jedinice id={}", order.getId(), unitId);
            }
        }
    }

    public List<WorkOrderDto> getWorkOrdersByUnitId(Long unitId) {
        WorkOrderMapper workOrderMapper = getWorkorderMapper();
        List<WorkOrder> workOrders = workOrderRepository.findByUnitId(unitId);
        List<WorkOrderDto> result = new ArrayList<>();
        for (WorkOrder order : workOrders) {
            result.add(workOrderMapper.toDto(order));
        }
        return result;
    }

    public MaintenanceStatisticsDto getStatisticsByUnitId(Long unitId) {
        MaintenanceStatisticsDto dto = new MaintenanceStatisticsDto();
        dto.setUnitId(unitId);
        dto.setAvgDurationHours(maintenanceLogService.avgMaintenanceDurationHours(unitId));
        List<WorkOrder> workOrders = workOrderRepository.findByUnitId(unitId);
        dto.setTotalWorkOrders(workOrders.size());
        int completedWorkOrders = 0;

        if (workOrders.isEmpty()) {
            dto.setTotalWorkOrders(0);
            dto.setCompletedWorkOrders(0);
            dto.setLastMaintenanceDate(null);
        } else {
            LocalDateTime lastDate = null;
            for (WorkOrder order : workOrders) {
                if (order.getStatus().equals(WorkOrderStatus.COMPLETED)) {
                    completedWorkOrders++;
                    if (lastDate == null || lastDate.isBefore(order.getScheduledFor())) {
                        lastDate = order.getScheduledFor();
                    }
                }
            }
            dto.setLastMaintenanceDate(lastDate != null ? lastDate.toString() : null);
            dto.setCompletedWorkOrders(completedWorkOrders);
        }

        if (self.unitExists(unitId)) {
            UnitDto unitDto = getUnitById(unitId);
            dto.setUnitName(unitDto.getName());
            dto.setCity(unitDto.getCity());
        } else {
            dto.setUnitName(null);
            dto.setCity(null);
        }

        return dto;
    }

    public List<WorkOrderDto> getAllWorkOrdersInProggress() {
        WorkOrderMapper workOrderMapper = getWorkorderMapper();
        List<WorkOrder> wo = workOrderRepository.getAllWithStatus(WorkOrderStatus.IN_PROGRESS);
        List<WorkOrderDto> wod = new ArrayList<>();
        for (WorkOrder w : wo) {
            wod.add(workOrderMapper.toDto(w));
        }
        return wod;
    }
}

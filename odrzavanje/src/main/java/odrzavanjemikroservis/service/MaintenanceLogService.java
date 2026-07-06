package odrzavanjemikroservis.service;

import odrzavanjemikroservis.entity.MaintenanceLog;
import odrzavanjemikroservis.entity.WorkOrder;
import odrzavanjemikroservis.observer.WorkOrderObserver;
import odrzavanjemikroservis.repository.MaintenanceLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class MaintenanceLogService implements WorkOrderObserver {

    @Autowired
    private MaintenanceLogRepository maintenanceLogRepository;


    public MaintenanceLog saveLog(WorkOrder order, String notes){
        MaintenanceLog log = new MaintenanceLog();
        log.setCompletedAt(LocalDateTime.now()); //todo HARDKODOVAO SAM DA SE KREIRANJE LOGA VODI KAO VREME ZAVRSETKA
        log.setWorkOrder(order);
        log.setNotes(notes);
        log.setUnit_id(order.getUnitId());
        if(order.getWorker()!=null)
            log.setWorkerName(order.getWorker().getFirstName()+" "+order.getWorker().getLastName());
        if(order.getMaintenanceTask()!=null)
            log.setTaskDescription(order.getMaintenanceTask().getTaskName());

       return maintenanceLogRepository.save(log);
    }

    public double avgMaintenanceDurationHours(Long unitId){
        List<MaintenanceLog> logs = maintenanceLogRepository.findByUnitId(unitId);
        if(logs.isEmpty())
            return 0.0;

        LocalDateTime completedat;
        LocalDateTime startedat;
        double sum=0;
        double hoursdifference;
        for(MaintenanceLog log : logs){
            completedat = log.getCompletedAt();
            startedat=log.getWorkOrder().getScheduledFor();
            hoursdifference = Duration.between(startedat, completedat).toMinutes()/60.0;
            sum+=hoursdifference;
        }
        return sum/ logs.size();
    }

    @Override
    public void onWorkOrderCompleted(WorkOrder workOrder, String notes) {
        System.out.println("cuvanje loga za workorder id: "+workOrder.getId());
        saveLog(workOrder, notes);
    }

    @Override
    public void onWorkOrderStarted(WorkOrder workOrder) {
        //zasad nista
    }
}

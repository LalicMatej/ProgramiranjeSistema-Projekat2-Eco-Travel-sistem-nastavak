package smestajmikroservis.services;

import org.raflab.sharedevents.WorkOrderCreatedEvent;
import org.raflab.sharedevents.AdventureTimeSlotUpdatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import smestajmikroservis.dtos.AvailabilityCalendarDto;
import smestajmikroservis.entity.AvailabilityCalendar;
import smestajmikroservis.entity.Unit;
import smestajmikroservis.repository.AvailabilityCalendarRepository;
import smestajmikroservis.repository.UnitRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class AvailabilityCalendarService {

    private static final Logger log = LoggerFactory.getLogger(AvailabilityCalendarService.class);

    @Autowired
    private AvailabilityCalendarRepository availabilityCalendarRepository;
    @Autowired
    private UnitRepository unitRepository;

    public AvailabilityCalendarDto addReservation(AvailabilityCalendarDto availabilityCalendarDto) {
        Unit unit = unitRepository.findByNameAndType(availabilityCalendarDto.getUnitName(), availabilityCalendarDto.getUnitType());
        if (unit == null) {
            throw new IllegalArgumentException("Unit not found");
        }
        if (jeZauzeto(availabilityCalendarDto)) {
            throw new IllegalArgumentException("Jedinica je vec zauzeta");
        }

        AvailabilityCalendar availabilityCalendar = new AvailabilityCalendar();
        availabilityCalendar.setUnit(unit);
        availabilityCalendar.setReason(availabilityCalendarDto.getReason());
        availabilityCalendar.setStartDate(availabilityCalendarDto.getStartDate());
        availabilityCalendar.setEndDate(availabilityCalendarDto.getEndDate());
        availabilityCalendar.setTimeSlotId(availabilityCalendarDto.getTimeSlotId());

        availabilityCalendarRepository.save(availabilityCalendar);

        return availabilityCalendarDto;
    }

    public boolean jeZauzeto(AvailabilityCalendarDto availabilityCalendarDto) {
        String unitName = availabilityCalendarDto.getUnitName();
        LocalDate startDate = availabilityCalendarDto.getStartDate();
        LocalDate endDate = availabilityCalendarDto.getEndDate();
        Unit unit = unitRepository.findByNameAndType(unitName, availabilityCalendarDto.getUnitType());
        if (unit == null) {
            throw new IllegalArgumentException("Unit not found");
        }
        String unitAddress = unit.getAddress().getStreetAddress();
        List<AvailabilityCalendar> lista = new ArrayList<>(
                availabilityCalendarRepository.findOccupiedByDateAndUnitNameAndAddress(startDate, endDate, unitName, unitAddress)
        );
        return !lista.isEmpty();
    }

    public void blockForMaintenance(WorkOrderCreatedEvent event) {
        Unit unit = unitRepository.findById(event.getUnitId()).orElse(null);
        if (unit == null) {
            log.warn("[Odrzavanje] Jedinica id={} nije pronadjena, preskacem blokiranje kalendara", event.getUnitId());
            return;
        }
        AvailabilityCalendar block = new AvailabilityCalendar();
        block.setUnit(unit);
        block.setStartDate(event.getScheduledFor().toLocalDate());
        int hours = event.getEstimatedDurationHours() != null ? event.getEstimatedDurationHours() : 1;
        LocalDate endDate = event.getScheduledFor().plusHours(hours).toLocalDate();
        if (!endDate.isAfter(block.getStartDate())) {
            endDate = block.getStartDate().plusDays(1);
        }
        block.setEndDate(endDate);
        block.setReason("Odrzavanje: " + (event.getTaskName() != null ? event.getTaskName() : "radni nalog")
                + " (nalog #" + event.getWorkOrderId() + ")");
        block.setWorkOrderId(event.getWorkOrderId());
        availabilityCalendarRepository.save(block);
        log.info("[Odrzavanje] Blokiran kalendar za jedinicu id={}, radni nalog id={}", event.getUnitId(), event.getWorkOrderId());
    }

    public void removeMaintenanceBlock(Long workOrderId) {
        List<AvailabilityCalendar> blocks = availabilityCalendarRepository.findByWorkOrderId(workOrderId);
        if (!blocks.isEmpty()) {
            availabilityCalendarRepository.deleteAll(blocks);
            log.info("[Odrzavanje] Uklonjena blokada kalendara za radni nalog id={}", workOrderId);
        }
    }

    @Transactional
    public void updateReservationFromTimeSlot(AdventureTimeSlotUpdatedEvent event) {
        AvailabilityCalendar calendar = availabilityCalendarRepository.findByTimeSlotId(event.getTimeSlotId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Rezervacija za timeSlotId=" + event.getTimeSlotId() + " nije pronadjena"
                ));

        if (event.getNewStartDate() == null || event.getNewEndDate() == null) {
            throw new IllegalArgumentException("Novi pocetak i kraj termina moraju biti poslati");
        }
        if (event.getNewEndDate().isBefore(event.getNewStartDate())) {
            throw new IllegalArgumentException("Novi kraj termina ne sme biti pre pocetka");
        }

        List<AvailabilityCalendar> overlaps = availabilityCalendarRepository.findOverlappingForUnitExcludingCalendar(
                calendar.getUnit().getId(),
                calendar.getId(),
                event.getNewStartDate(),
                event.getNewEndDate()
        );
        if (!overlaps.isEmpty()) {
            throw new IllegalArgumentException("Novi termin se preklapa sa postojecom rezervacijom");
        }

        calendar.setStartDate(event.getNewStartDate());
        calendar.setEndDate(event.getNewEndDate());
        calendar.setReason("Avantura termin #" + event.getTimeSlotId() + " - azuriran termin");
        availabilityCalendarRepository.save(calendar);
        log.info("[Avanture] Azuriran kalendar smestaja za timeSlotId={}", event.getTimeSlotId());
    }
}

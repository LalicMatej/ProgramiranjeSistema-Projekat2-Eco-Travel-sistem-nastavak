package odrzavanjemikroservis.observer;

import odrzavanjemikroservis.entity.WorkOrder;
import odrzavanjemikroservis.entityMapper.WorkOrderMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class WorkOrderEventPublisher {
    private final List<WorkOrderObserver> observers = new ArrayList<>();

    public void subscribe(WorkOrderObserver observer) {
        observers.add(observer);
        System.out.println("dodat novi observer "+ observer.getClass().getSimpleName());
    }

    public void unsubscribe(WorkOrderObserver observer) {
        observers.remove(observer);
        System.out.printf("obrisan observer "+ observer.getClass().getSimpleName());
    }

    public void notify(WorkOrder workOrder, String notes){
        System.out.println("notify metoda bajo");

        for (WorkOrderObserver observer : observers) {
            try {
                observer.onWorkOrderCompleted(workOrder, notes);
            } catch (Exception e) {
                System.err.println("OBSERVER Greska u " + observer.getClass().getSimpleName() + ": " + e.getMessage());
            }
        }
    }
}

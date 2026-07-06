package odrzavanjemikroservis.observer;

import odrzavanjemikroservis.entity.WorkOrder;

public interface WorkOrderObserver {
    void onWorkOrderCompleted(WorkOrder workOrder ,String notes);
    void onWorkOrderStarted(WorkOrder workOrder);
}

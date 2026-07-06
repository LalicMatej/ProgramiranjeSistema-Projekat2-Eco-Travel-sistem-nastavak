package org.raflab.sharedevents;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkOrderStatusChangedEvent {
    private Long workOrderId;
    private Long unitId;
    private String oldStatus;
    private String newStatus;
    private String notes;
}

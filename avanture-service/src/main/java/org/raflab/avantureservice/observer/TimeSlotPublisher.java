package org.raflab.avantureservice.observer;

import org.raflab.avantureservice.model.TimeSlots;

public interface TimeSlotPublisher {
    void addSubscriber(AnnouncementsSubscriber subscriber);
    void removeSubscriber(AnnouncementsSubscriber subscriber);
    void notifySubscriber(TimeSlots timeSlots);

}

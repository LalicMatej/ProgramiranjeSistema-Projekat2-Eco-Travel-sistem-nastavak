package org.raflab.avantureservice.observer;

import org.raflab.avantureservice.model.TimeSlots;

public interface AnnouncementsSubscriber {
    void update(TimeSlots timeSlots);
}

package com.federico.LessonBookingSystem.adapters.out.persistence;

import com.eventstore.dbclient.EventStoreDBClient;
import com.federico.LessonBookingSystem.application.services.ports.out.persistence.LessonRepository;
import events.LessonEvent;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


public class EventStoreLessonRepository implements LessonRepository {
    @Autowired
    private EventStoreDBClient dbClient;

    @Override
    public List<LessonEvent> save(List<LessonEvent> events) {
        // TODO - Add implementation here
        return List.of();
    }
}

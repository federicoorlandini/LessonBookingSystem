package com.federico.LessonBookingSystem.adapters.out.persistence;

import com.eventstore.dbclient.EventData;
import com.eventstore.dbclient.EventStoreDBClient;
import com.federico.LessonBookingSystem.application.services.ports.out.persistence.LessonRepository;
import Lesson.LessonEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Repository
@Qualifier("EventStoreDBRepository")
public class EventStoreLessonRepository implements LessonRepository {
    private final EventStoreDBClient dbClient;

    @Autowired
    public EventStoreLessonRepository(EventStoreDBClient dbClient) {
        this.dbClient = dbClient;
    }

    @Override
    public List<LessonEvent> save(List<LessonEvent> events) throws ExecutionException, InterruptedException, IOException {
        for (LessonEvent event : events) {
            var eventData = EventData
                    .builderAsJson(event.eventId(), event.getClass().getSimpleName(), event)
                    .build();

            dbClient.appendToStream(event.eventStreamId(), eventData).get();
        }

        return events;
    }
}

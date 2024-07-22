package com.federico.LessonBookingSystem.adapters.out.persistence;

import com.federico.LessonBookingSystem.application.services.ports.out.persistence.LessonRepository;
import events.LessonEvent;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;

@Qualifier("NullRepository")
public class NullLessonRepository implements LessonRepository {
    @Override
    public List<LessonEvent> save(List<LessonEvent> events) {
        // Not doing anything then returning the input event list
        return events;
    }
}

package com.federico.LessonBookingSystem.adapters.out.persistence;

import Lesson.Lesson;
import com.federico.LessonBookingSystem.application.services.ports.out.persistence.LessonRepository;
import Lesson.LessonEvent;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Qualifier("NullRepository")
@Repository
public class NullLessonRepository implements LessonRepository {
    @Override
    public List<LessonEvent> save(List<LessonEvent> events) {
        // Not doing anything then returning the input event list
        return events;
    }
}

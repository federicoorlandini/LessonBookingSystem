package com.federico.LessonBookingSystem.application.services.ports.out.persistence;

import events.LessonEvent;

import java.util.List;

public interface LessonRepository {
    List<LessonEvent> save(List<LessonEvent> events);
}

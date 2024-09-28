package com.federico.LessonBookingSystem.application.services.ports.out.persistence;

import Lesson.LessonEvent;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

public interface LessonRepository {
    List<LessonEvent> save(List<LessonEvent> events) throws ExecutionException, InterruptedException, IOException;
}

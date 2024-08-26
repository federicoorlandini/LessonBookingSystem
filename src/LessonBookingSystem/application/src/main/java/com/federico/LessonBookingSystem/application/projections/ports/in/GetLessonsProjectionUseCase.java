package com.federico.LessonBookingSystem.application.projections.ports.in;

import model.Lesson;
import java.util.List;
import java.util.concurrent.ExecutionException;

public interface GetLessonsProjectionUseCase {
    List<Lesson> GetLessonsProjection() throws ExecutionException, InterruptedException;
}

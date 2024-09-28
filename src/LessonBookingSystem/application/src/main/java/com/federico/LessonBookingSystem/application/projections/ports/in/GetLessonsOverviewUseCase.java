package com.federico.LessonBookingSystem.application.projections.ports.in;

import Lesson.Lesson;
import java.util.List;
import java.util.concurrent.ExecutionException;

public interface GetLessonsOverviewUseCase {
    List<Lesson> GetLessonsProjection() throws ExecutionException, InterruptedException;
}

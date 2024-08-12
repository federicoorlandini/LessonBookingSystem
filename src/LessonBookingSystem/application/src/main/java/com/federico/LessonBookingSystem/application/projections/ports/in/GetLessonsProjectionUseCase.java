package com.federico.LessonBookingSystem.application.projections.ports.in;

import com.federico.LessonBookingSystem.application.projections.ports.in.models.Lesson;

import java.util.List;

public interface GetLessonsProjectionUseCase {
    List<Lesson> GetLessonsProjection();
}

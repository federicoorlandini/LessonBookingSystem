package com.federico.LessonBookingSystem.application.projections.ports.in.models;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public record Lesson(UUID lessonId, LocalDate date, LocalTime startTime, LocalTime endTime, boolean canceled) {
    // Nothing here
}

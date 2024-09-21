package com.federico.LessonBookingSystem.adapters.in.rest;

import model.Lesson;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public record CreateLessonResponse(UUID lessonId, LocalDate date, LocalTime startTime, LocalTime endTime, int maxNumberAttenders, Lesson.Status status) {
    // Nothing here
}

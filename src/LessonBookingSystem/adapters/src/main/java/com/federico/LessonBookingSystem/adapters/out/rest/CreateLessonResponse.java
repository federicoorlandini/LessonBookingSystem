package com.federico.LessonBookingSystem.adapters.out.rest;

import java.time.LocalDateTime;
import java.util.UUID;

public record CreateLessonResponse(UUID lessonId, LocalDateTime dayAndTime, int maxNumberAttenders) {
    // Nothing here
}

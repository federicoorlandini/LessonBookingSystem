package io.rest;

import java.time.LocalDateTime;
import java.util.UUID;

public class CreateLessonResponse {
    private final UUID lessonId;
    private final LocalDateTime dayAndTime;
    private final int maxNumberAttenders;

    public CreateLessonResponse(UUID lessonId, LocalDateTime dayAndTime, int maxNumberAttenders) {
        this.lessonId = lessonId;
        this.dayAndTime = dayAndTime;
        this.maxNumberAttenders = maxNumberAttenders;
    }
}

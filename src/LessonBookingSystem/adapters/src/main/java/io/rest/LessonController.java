package io.rest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
public class LessonController {
    @PostMapping("/lesson")
    public CreateLessonResponse createLesson(@RequestBody CreateLessonRequest request) {
        // Validate the input

        // Trigger the command

        // How the event sourcing allows to return the ID?
        return new CreateLessonResponse(UUID.randomUUID(), LocalDateTime.now(), 16);
    }
}

package com.federico.LessonBookingSystem.adapters.in.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.federico.LessonBookingSystem.application.services.ports.in.CreateLessonUseCase;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.concurrent.ExecutionException;

@RestController
public class LessonController {
    @Autowired
    private CreateLessonUseCase createLessonUseCase;

    @PostMapping("/lesson")
    public CreateLessonResponse createLesson(@RequestBody CreateLessonRequest request) throws IOException, ExecutionException, InterruptedException {
        // TODO - Validate the input

        // Trigger the use case
        var newLesson = createLessonUseCase.CreateLesson(
                LocalDateTime.parse(request.dayAndTime()),
                request.maxNumberAttenders());

        var response = new CreateLessonResponse(newLesson.getId(),
                newLesson.getDateAndTime(),
                newLesson.getMaxNumberAttenders());
        return response;
    }
}


package com.federico.LessonBookingSystem.adapters.out.rest;

import model.Lesson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.federico.LessonBookingSystem.application.services.ports.in.ApplicationService;

import java.io.InvalidClassException;
import java.time.LocalDateTime;

@RestController
public class LessonController {

    @Autowired
    private ApplicationService applicationService;

    @PostMapping("/lesson")
    public CreateLessonResponse createLesson(@RequestBody CreateLessonRequest request) throws InvalidClassException {
        // Validate the input

        // Trigger the use case
        var newLesson = applicationService.CreateLessonUseCase(
                LocalDateTime.parse(request.dayAndTime()),
                request.maxNumberAttenders());

        var response = new CreateLessonResponse(newLesson.getId(),
                newLesson.getDateAndTime(),
                newLesson.getMaxNumberAttenders());
        return response;
    }

    @GetMapping("/test")
    public int test() {
        return 1;
    }
}


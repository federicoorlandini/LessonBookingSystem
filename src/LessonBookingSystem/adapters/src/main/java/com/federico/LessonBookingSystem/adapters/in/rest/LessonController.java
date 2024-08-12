package com.federico.LessonBookingSystem.adapters.in.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.federico.LessonBookingSystem.application.services.ports.in.CreateLessonUseCase;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.concurrent.ExecutionException;

@RestController
public class LessonController {
    private final CreateLessonUseCase createLessonUseCase;

    private final String dateTimeFormatterPatter = "dd-MM-yyyy HH:mm";
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(dateTimeFormatterPatter);

    @Autowired
    public LessonController(CreateLessonUseCase createLessonUseCase) {
        this.createLessonUseCase = createLessonUseCase;
    }

    @PostMapping("/lesson")
    public ResponseEntity createLesson(@RequestBody CreateLessonRequest request) throws IOException, ExecutionException, InterruptedException {
        // Validation
        // Date and time format: dd-MM-yyyy HH:mm
        LocalDateTime dateAndTime;
        try {
            dateAndTime = LocalDateTime.parse(request.dayAndTime(), dateTimeFormatter);
        }
        catch (DateTimeParseException ex) {
            var errorMessage = String.format("Invalid date and time (format %s)", dateTimeFormatterPatter);
            return ResponseEntity.badRequest().body(errorMessage);
        }

        if( request.maxNumberAttenders() <= 0 ) {
            return ResponseEntity
                    .badRequest()
                    .body(String.format("The parameter maxNumberAttenders must be a positive number. Actual value: %s", request.maxNumberAttenders()));
        }

        // Trigger the use case
        var newLesson = createLessonUseCase.CreateLesson(
                dateAndTime,
                request.maxNumberAttenders());

        var response = new CreateLessonResponse(newLesson.getId(),
                newLesson.getDateAndTime(),
                newLesson.getMaxNumberAttenders());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/lessons")
    public ResponseEntity getLessons() {
        return null;
    }
}


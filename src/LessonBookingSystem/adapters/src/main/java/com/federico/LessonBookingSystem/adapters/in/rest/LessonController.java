package com.federico.LessonBookingSystem.adapters.in.rest;

import com.federico.LessonBookingSystem.application.projections.ports.in.GetLessonsOverviewUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.federico.LessonBookingSystem.application.services.ports.in.CreateLessonUseCase;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.concurrent.ExecutionException;

@RestController
public class LessonController {
    private final CreateLessonUseCase createLessonUseCase;
    private final GetLessonsOverviewUseCase getLessonsOverviewUseCase;

    private final String DATE_FORMATTER_PATTERN = "dd-MM-yyyy";
    private final String TIME_FORMATTER_PATTERN = "HH:mm";
    private final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMATTER_PATTERN);
    private final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern(TIME_FORMATTER_PATTERN);

    @Autowired
    public LessonController(CreateLessonUseCase createLessonUseCase,
                            GetLessonsOverviewUseCase getLessonsOverviewUseCase) {
        this.createLessonUseCase = createLessonUseCase;
        this.getLessonsOverviewUseCase = getLessonsOverviewUseCase;
    }

    @PostMapping("/lesson")
    public ResponseEntity createLesson(@RequestBody CreateLessonRequest request) throws IOException, ExecutionException, InterruptedException {
        // Validation
        // Date format: dd-MM-yyyy
        // Time format: HH:mm

        // Validate the date
        LocalDate date;
        try {
            date = LocalDate.parse(request.date(), DATE_FORMATTER);
        }
        catch (DateTimeParseException ex) {
            var errorMessage = String.format("Invalid date (format %s)", DATE_FORMATTER_PATTERN);
            return ResponseEntity.badRequest().body(errorMessage);
        }

        // Validate the star time
        LocalTime startTime;
        try {
            startTime = LocalTime.parse(request.startTime(), TIME_FORMATTER);
        }
        catch (DateTimeParseException ex) {
            var errorMessage = String.format("Invalid start time (format %s)", TIME_FORMATTER_PATTERN);
            return ResponseEntity.badRequest().body(errorMessage);
        }

        // Validate the end time
        LocalTime endTime;
        try {
            endTime = LocalTime.parse(request.endTime(), TIME_FORMATTER);
        }
        catch (DateTimeParseException ex) {
            var errorMessage = String.format("Invalid end time (format %s)", TIME_FORMATTER_PATTERN);
            return ResponseEntity.badRequest().body(errorMessage);
        }

        // Validate start time is earlier than end time
        if( startTime.isAfter(endTime) ) {
            return ResponseEntity
                    .badRequest()
                    .body(String.format("StartTime %S is earlier than EndTime %s", request.startTime(), request.endTime()));
        }


        if( request.maxNumberAttenders() <= 0 ) {
            return ResponseEntity
                    .badRequest()
                    .body(String.format("The parameter maxNumberAttenders must be a positive number. Actual value: %s", request.maxNumberAttenders()));
        }

        // Trigger the use case
        var newLesson = createLessonUseCase.CreateLesson(
                date,
                startTime,
                endTime,
                request.maxNumberAttenders());

        var response = new CreateLessonResponse(newLesson.getId(),
                newLesson.getDate(),
                newLesson.getStartTime(),
                newLesson.getEndTime(),
                newLesson.getMaxNumberAttenders(),
                newLesson.getStatus());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/lessons")
    public ResponseEntity getLessons() throws ExecutionException, InterruptedException {
        var lessons = getLessonsOverviewUseCase.GetLessonsProjection();

        var response = new GetLessonsProjectionResponse(lessons);

        return ResponseEntity.ok(response);
    }
}


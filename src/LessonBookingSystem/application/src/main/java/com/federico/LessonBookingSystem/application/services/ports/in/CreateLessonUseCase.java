package com.federico.LessonBookingSystem.application.services.ports.in;

import Lesson.Lesson;
import Lesson.DuplicatedLessonException;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.concurrent.ExecutionException;

public interface CreateLessonUseCase {
    Lesson CreateLesson(LocalDate date, LocalTime startTime, LocalTime endTime, int maxNumberAttenders)
            throws IOException, ExecutionException, InterruptedException, DuplicatedLessonException;
}

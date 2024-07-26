package com.federico.LessonBookingSystem.application.services.ports.in;

import model.Lesson;

import java.io.IOException;
import java.io.InvalidClassException;
import java.time.LocalDateTime;
import java.util.concurrent.ExecutionException;

public interface CreateLessonUseCase {
    Lesson CreateLesson(LocalDateTime dateAndTime, int maxNumberAttenders) throws IOException, ExecutionException, InterruptedException;
}

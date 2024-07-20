package com.federico.LessonBookingSystem.application.services.ports.in;

import model.Lesson;

import java.io.InvalidClassException;
import java.time.LocalDateTime;

public interface CreateLessonUseCase {
    Lesson CreateLesson(LocalDateTime dateAndTime, int maxNumberAttenders) throws InvalidClassException;
}

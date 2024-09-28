package com.federico.LessonBookingSystem.application.projections.ports.out.persistence;

import Lesson.Lesson;
import jdk.jshell.spi.ExecutionControl;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.ExecutionException;

public interface LessonProjectionsRepository {
    String LESSONS_PROJECTION_NAME = "LessonProjection";

    void EnsureProjectionExists() throws ExecutionException, InterruptedException, ExecutionControl.NotImplementedException;
    List<Lesson> GetLessons() throws ExecutionException, InterruptedException;

    List<Lesson> getLessonsByDay(LocalDate day) throws ExecutionException, InterruptedException;
}

package com.federico.LessonBookingSystem.application.projections.ports.out.persistence;

import com.federico.LessonBookingSystem.application.projections.ports.in.models.Lesson;
import jdk.jshell.spi.ExecutionControl;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface LessonProjectionsRepository {
    void EnsureProjectionExists() throws ExecutionException, InterruptedException, ExecutionControl.NotImplementedException;
    List<Lesson> GetLessons();
}

package com.federico.LessonBookingSystem.application.projections.ports.out.persistence;

import com.federico.LessonBookingSystem.application.projections.ports.in.models.Lesson;
import jdk.jshell.spi.ExecutionControl;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface LessonProjectionsRepository {
    void EnsureProjectionExists() throws ExecutionControl.NotImplementedException, ExecutionException, InterruptedException;
    List<Lesson> GetLessons();
}

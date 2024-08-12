package com.federico.LessonBookingSystem.application.usecases;

import com.federico.LessonBookingSystem.application.projections.ports.in.models.Lesson;
import com.federico.LessonBookingSystem.application.projections.ports.out.persistence.LessonProjectionsRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class GetLessonsProjectionUseCaseImpl implements com.federico.LessonBookingSystem.application.projections.ports.in.GetLessonsProjectionUseCase {
    @Autowired
    private LessonProjectionsRepository lessonProjectionsRepository;

    @Override
    public List<Lesson> GetLessonsProjection() {
        return List.of();
    }
}

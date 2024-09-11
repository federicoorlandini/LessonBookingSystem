package com.federico.LessonBookingSystem.application.usecases;

import com.federico.LessonBookingSystem.application.projections.ports.in.GetLessonsOverviewUseCase;
import model.Lesson;
import com.federico.LessonBookingSystem.application.projections.ports.out.persistence.LessonProjectionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class GetLessonsOverviewUseCaseImpl implements GetLessonsOverviewUseCase {
    @Autowired
    private LessonProjectionsRepository lessonProjectionsRepository;

    @Override
    public List<Lesson> GetLessonsProjection() throws ExecutionException, InterruptedException {
        return lessonProjectionsRepository.GetLessons();
    }
}

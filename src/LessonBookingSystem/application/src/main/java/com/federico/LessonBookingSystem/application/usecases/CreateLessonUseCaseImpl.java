package com.federico.LessonBookingSystem.application.usecases;

import com.federico.LessonBookingSystem.application.projections.ports.out.persistence.LessonProjectionsRepository;
import com.federico.LessonBookingSystem.application.services.ports.in.CreateLessonUseCase;
import com.federico.LessonBookingSystem.application.services.ports.out.persistence.LessonRepository;
import Lesson.LessonCommand;
import Lesson.Lesson;
import Lesson.DuplicatedLessonException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Service
public class CreateLessonUseCaseImpl implements CreateLessonUseCase {

    private final LessonRepository lessonRepository;
    private final LessonProjectionsRepository lessonProjectionsRepository;

    @Autowired
    public CreateLessonUseCaseImpl(@Qualifier("EventStoreDBRepository") LessonRepository lessonRepository,
                                   LessonProjectionsRepository lessonProjectionsRepository) {
        this.lessonRepository = lessonRepository;
        this.lessonProjectionsRepository = lessonProjectionsRepository;
    }

    @Override
    public Lesson CreateLesson(LocalDate date, LocalTime startTime, LocalTime endTime, int maxNumberAttenders)
            throws IOException, ExecutionException, InterruptedException, DuplicatedLessonException {
        // We must check if a open lesson with the same date, startTime and endTime already exists
        var lessonsOnSameDay = lessonProjectionsRepository.getLessonsByDay(date);
        
        // Trigger the command
        var command = new LessonCommand.CreateLessonCommand(UUID.randomUUID(),
                date,
                startTime,
                endTime,
                maxNumberAttenders,
                lessonsOnSameDay);
        var newLesson = new Lesson();   // TODO - Refactor using factory pattern?
        var events = newLesson.handle(command);

        // Save the events in the event store
        lessonRepository.save(events);

        return newLesson;
    }
}

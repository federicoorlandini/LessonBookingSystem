package com.federico.LessonBookingSystem.application.usecases;

import com.federico.LessonBookingSystem.application.services.ports.in.CreateLessonUseCase;
import com.federico.LessonBookingSystem.application.services.ports.out.persistence.LessonRepository;
import commands.LessonCommand;
import model.Lesson;
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

    private LessonRepository lessonRepository;

    @Autowired
    public CreateLessonUseCaseImpl(@Qualifier("EventStoreDBRepository") LessonRepository lessonRepository) {
        this.lessonRepository = lessonRepository;
    }

    @Override
    public Lesson CreateLesson(LocalDate date, LocalTime startTime, LocalTime endTime, int maxNumberAttenders) throws IOException, ExecutionException, InterruptedException {
        // Trigger the command
        var command = new LessonCommand.CreateLessonCommand(UUID.randomUUID(), date, startTime, endTime, maxNumberAttenders);
        var newLesson = new Lesson();   // TODO - Refactor using factory pattern?
        var events = newLesson.handle(command);

        // Save the events in the event store
        lessonRepository.save(events);

        return newLesson;
    }
}

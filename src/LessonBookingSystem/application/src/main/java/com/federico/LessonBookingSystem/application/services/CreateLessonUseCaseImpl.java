package com.federico.LessonBookingSystem.application.services;

import com.federico.LessonBookingSystem.application.services.ports.in.CreateLessonUseCase;
import com.federico.LessonBookingSystem.application.services.ports.out.persistence.LessonRepository;
import commands.LessonCommand;
import model.Lesson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Service
public class CreateLessonUseCaseImpl implements CreateLessonUseCase {
    @Autowired
    @Qualifier("EventStoreDBRepository")
    private LessonRepository lessonRepository;

    @Override
    public Lesson CreateLesson(LocalDateTime dateAndTime, int maxNumberAttenders) throws IOException, ExecutionException, InterruptedException {
        // Trigger the command
        var command = new LessonCommand.CreateLessonCommand(UUID.randomUUID(), dateAndTime, maxNumberAttenders);
        var newLesson = new Lesson();
        var events = newLesson.handle(command);

        // Save the events in the event store
        lessonRepository.save(events);

        // Generate the aggregate current state
        newLesson.evolve(events);

        return newLesson;
    }
}

package com.federico.LessonBookingSystem.application.services;

import com.federico.LessonBookingSystem.application.services.ports.in.CreateLessonUseCase;
import com.federico.LessonBookingSystem.application.services.ports.out.persistence.LessonRepository;
import commands.LessonCommand;
import model.Lesson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InvalidClassException;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class CreateLessonUseCaseImpl implements CreateLessonUseCase {
    @Autowired
    private LessonRepository lessonRepository;

    @Override
    public Lesson CreateLesson(LocalDateTime dateAndTime, int maxNumberAttenders) throws InvalidClassException {
        // Trigger the command
        var command = new LessonCommand.CreateLessonCommand(UUID.randomUUID(), dateAndTime, maxNumberAttenders);
        var newLesson = new Lesson();
        var events = newLesson.handle(command);

        // Save the events in the event store

        // Generate the aggregate current state
        newLesson.evolve(events);

        return newLesson;
    }
}

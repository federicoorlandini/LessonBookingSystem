package com.federico.LessonBookingSystem.adapters.out.persistence;

import com.eventstore.dbclient.EventStoreDBProjectionManagementClient;
import com.federico.LessonBookingSystem.application.projections.ports.in.models.Lesson;
import com.federico.LessonBookingSystem.application.projections.ports.out.persistence.LessonProjectionsRepository;
import jdk.jshell.spi.ExecutionControl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Repository
public class EventStoreLessonProjectionsRepository implements LessonProjectionsRepository {
    private final EventStoreDBProjectionManagementClient projectionClient;

    private final String LessonsProjectionName = "LessonProjection";

    @Autowired
    public EventStoreLessonProjectionsRepository(EventStoreDBProjectionManagementClient projectionClient) {
        this.projectionClient = projectionClient;
    }

    @Override
    public void EnsureProjectionExists() throws ExecutionControl.NotImplementedException, ExecutionException, InterruptedException {
        // Must check if the projection needed by this repository is present in the event store
        if( !ProjectionExists() ) {
            CreateProjection();
        }
    }

    private boolean ProjectionExists() throws ExecutionControl.NotImplementedException, ExecutionException, InterruptedException {
        var projections = projectionClient.list().get();
        return projections
                .stream()
                .anyMatch(item -> item.getName().equalsIgnoreCase(LessonsProjectionName));
    }

    private void CreateProjection() throws ExecutionControl.NotImplementedException {
        // TODO - Add code here
        throw new ExecutionControl.NotImplementedException("Not implemented yet");
    }

    @Override
    public List<Lesson> GetLessons() {
        return List.of();
    }
}

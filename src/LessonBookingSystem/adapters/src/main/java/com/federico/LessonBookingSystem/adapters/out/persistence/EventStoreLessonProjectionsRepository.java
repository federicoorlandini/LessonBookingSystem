package com.federico.LessonBookingSystem.adapters.out.persistence;

import com.eventstore.dbclient.EventStoreDBProjectionManagementClient;
import Lesson.Lesson;
import com.federico.LessonBookingSystem.application.projections.ports.out.persistence.LessonProjectionsRepository;
import jdk.jshell.spi.ExecutionControl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Repository
public class EventStoreLessonProjectionsRepository implements LessonProjectionsRepository {
    // This class is only used to deserialize the result of the projection LessonProjection
    private static class LessonProjectionResult {
        public ArrayList<Lesson> lessons;
    }
    private final EventStoreDBProjectionManagementClient projectionClient;

    @Autowired
    public EventStoreLessonProjectionsRepository(EventStoreDBProjectionManagementClient projectionClient) {
        this.projectionClient = projectionClient;
    }

    @Override
    public void EnsureProjectionExists() throws ExecutionException, InterruptedException, ExecutionControl.NotImplementedException {
        // Must check if the projection needed by this repository is present in the event store
        if( ProjectionExists() ) {
            System.out.println(String.format("Projection %s already exists. Skipping the creation.", LESSONS_PROJECTION_NAME));
        }
        else {
            System.out.println(String.format("Projection %s doesn't exist. Creating it.", LESSONS_PROJECTION_NAME));
            CreateProjection();
        }
    }

    @Override
    public List<Lesson> GetLessons() throws ExecutionException, InterruptedException {
        var deserializedEntity = projectionClient.getResult(LESSONS_PROJECTION_NAME, LessonProjectionResult.class).get();
        return deserializedEntity.lessons;
    }

    @Override
    public List<Lesson> getLessonsByDay(LocalDate day) throws ExecutionException, InterruptedException {
        // We cannot directly filter the projection in the EventStore server so we need to filter the entire collection
        return projectionClient
                .getResult(LESSONS_PROJECTION_NAME, LessonProjectionResult.class)
                .get()
                .lessons
                .stream()
                .filter(lesson -> lesson.getDate().equals(day))
                .toList();
    }

    private boolean ProjectionExists() throws ExecutionException, InterruptedException {
        var projections = projectionClient.list().get();
        return projections
                .stream()
                .anyMatch(item -> item.getName().equalsIgnoreCase(LESSONS_PROJECTION_NAME));
    }

    private void CreateProjection() throws ExecutionException, InterruptedException {
        final String LESSON_STATUS_JS_SCRIPT = "fromAll()\n" +
                "    .when({\n" +
                "        $init: function () {\n" +
                "            return {\n" +
                "                lessons: []\n" +
                "            }\n" +
                "        },\n" +
                "        LessonCreated: function (state, event) {\n" +
                "            const bodyraw = JSON.parse(event.bodyRaw);\n" +
                "            state.lessons.push({id: bodyraw.lessonId,\n" +
                "                date: bodyraw.date,\n" +
                "                startTime: bodyraw.startTime,\n" +
                "                endTime: bodyraw.endTime,\n" +
                "                maxNumberAttenders: bodyraw.maxNumberAttenders,\n" +
                "                status: 'OPEN'});\n" +
                "        }\n" +
                "    })\n" +
                "    .outputState()";

        projectionClient.create("LessonProjection", LESSON_STATUS_JS_SCRIPT).get();
    }
}

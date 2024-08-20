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
    private final String LESSONS_PROJECTION_NAME = "LessonProjection";

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
    public List<Lesson> GetLessons() {
        return List.of();
    }

    private boolean ProjectionExists() throws ExecutionControl.NotImplementedException, ExecutionException, InterruptedException {
        var projections = projectionClient.list().get();
        return projections
                .stream()
                .anyMatch(item -> item.getName().equalsIgnoreCase(LESSONS_PROJECTION_NAME));
    }

    private void CreateProjection() throws ExecutionControl.NotImplementedException, ExecutionException, InterruptedException {
        final String LESSON_STATUS_JS_SCRIPT = "fromAll()\n" +
                "    .when({\n" +
                "        $init: function () {\n" +
                "            return {\n" +
                "                rows: []\n" +
                "            }\n" +
                "        },\n" +
                "        LessonCreated: function (state, event) {\n" +
                "            const bodyraw = JSON.parse(event.bodyRaw);\n" +
                "            log('LOG:' + JSON.stringify(bodyraw.lessonId));\n" +
                "            state.rows.push({lessonId: bodyraw.lessonId,\n" +
                "                date: bodyraw.date,\n" +
                "                startTime: bodyraw.startTime,\n" +
                "                endTime: bodyraw.endTime,\n" +
                "                maxNumberAttenders: bodyraw.maxNumberAttenders,\n" +
                "                status: 'open'});\n" +
                "        }\n" +
                "    })\n" +
                "    .outputState()";

        projectionClient.create("LessonProjection", LESSON_STATUS_JS_SCRIPT).get();
    }
}

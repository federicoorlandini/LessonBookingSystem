package events;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.UUID;

// This interface defines the events for the model.Lesson
public sealed interface LessonEvent {
     UUID eventId();
     UUID lessonId();
     int version();

     default String eventStreamId() {
          return MessageFormat.format("Lesson-{0}", lessonId());
     }

     record LessonCreated(
             UUID eventId,
             UUID lessonId,
             int version,
             LocalDateTime dateAndTime,
             int maxNumberAttenders) implements LessonEvent {

          @JsonSerialize(using = LocalDateSerializer.class)
          @Override
          public LocalDateTime dateAndTime() {
               return dateAndTime;
          }
     };
}




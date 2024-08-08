import com.federico.LessonBookingSystem.adapters.in.rest.CreateLessonRequest;
import com.federico.LessonBookingSystem.adapters.in.rest.CreateLessonResponse;
import com.federico.LessonBookingSystem.adapters.in.rest.LessonController;
import com.federico.LessonBookingSystem.application.services.ports.in.CreateLessonUseCase;
import model.Lesson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class LessonControllerTests {

    @Mock
    private CreateLessonUseCase createLessonUseCase;

    @InjectMocks
    private LessonController lessonController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateLesson_Success() throws IOException, ExecutionException, InterruptedException {
        final var uuid = UUID.randomUUID();
        final var dateAndTimeAsString = "01-01-2023 10:00";
        final var dateAndTime = LocalDateTime.of(2023, 1, 1, 10, 0);
        final var maxNumberAttenders = 10;

        // Given
        CreateLessonRequest request = new CreateLessonRequest(dateAndTimeAsString, maxNumberAttenders);
        Lesson lesson = mock(Lesson.class);
        when(lesson.getId()).thenReturn(uuid);
        when(lesson.getDateAndTime()).thenReturn(dateAndTime);
        when(lesson.getMaxNumberAttenders()).thenReturn(maxNumberAttenders);

        when(createLessonUseCase.CreateLesson(any(LocalDateTime.class), eq(maxNumberAttenders)))
                .thenReturn(lesson);

        // When
        ResponseEntity responseEntity = lessonController.createLesson(request);

        // Then
        assertEquals(200, responseEntity.getStatusCodeValue());
        CreateLessonResponse response = (CreateLessonResponse) responseEntity.getBody();
        assertEquals(uuid, response.lessonId());
        assertEquals(dateAndTime, response.dayAndTime());
        assertEquals(maxNumberAttenders, response.maxNumberAttenders());
    }

    @Test
    public void testCreateLesson_InvalidDateFormat() throws IOException, ExecutionException, InterruptedException {
        // Given
        CreateLessonRequest request = new CreateLessonRequest("invalid-date-format", 10);

        // When
        ResponseEntity responseEntity = lessonController.createLesson(request);

        // Then
        assertEquals(400, responseEntity.getStatusCodeValue());
        assertEquals("Invalid date and time (format dd-MM-yyyy HH:mm)", responseEntity.getBody());
    }

    @Test
    public void testCreateLesson_InvalidMaxNumberAttenders() throws IOException, ExecutionException, InterruptedException {
        // Given
        CreateLessonRequest request = new CreateLessonRequest("01-01-2023 10:00", -1);

        // When
        ResponseEntity responseEntity = lessonController.createLesson(request);

        // Then
        assertEquals(400, responseEntity.getStatusCodeValue());
        assertEquals("The parameter maxNumberAttenders must be a positive number. Actual value: -1", responseEntity.getBody());
    }
}
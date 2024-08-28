package unit;

import com.federico.LessonBookingSystem.adapters.in.rest.CreateLessonRequest;
import com.federico.LessonBookingSystem.adapters.in.rest.CreateLessonResponse;
import com.federico.LessonBookingSystem.adapters.in.rest.LessonController;
import com.federico.LessonBookingSystem.application.projections.ports.in.GetLessonsProjectionUseCase;
import com.federico.LessonBookingSystem.application.services.ports.in.CreateLessonUseCase;
import model.Lesson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class LessonControllerTests {

    @Mock
    private CreateLessonUseCase createLessonUseCase;

    @Mock
    private GetLessonsProjectionUseCase getLessonsProjectionUseCase;

    @InjectMocks
    private LessonController lessonController;

    final UUID uuid = UUID.randomUUID();

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateLesson_Success() throws IOException, ExecutionException, InterruptedException {
        final var dateAsString = "01-01-2023";
        final var startTimeAsString = "10:00";
        final var endTimeAsString = "11:00";
        final var date = LocalDate.of(2023, 1, 1);
        final var startTime = LocalTime.of(10, 0);
        final var endTime = LocalTime.of(11, 0);
        final var maxNumberAttenders = 10;

        // Given
        CreateLessonRequest request = new CreateLessonRequest(dateAsString, startTimeAsString, endTimeAsString, maxNumberAttenders);
        Lesson lesson = mock(Lesson.class);
        when(lesson.getId()).thenReturn(uuid);
        when(lesson.getDate()).thenReturn(date);
        when(lesson.getStartTime()).thenReturn(startTime);
        when(lesson.getEndTime()).thenReturn(endTime);
        when(lesson.getMaxNumberAttenders()).thenReturn(maxNumberAttenders);

        when(createLessonUseCase.CreateLesson(any(LocalDate.class),any(LocalTime.class), any(LocalTime.class), eq(maxNumberAttenders)))
                .thenReturn(lesson);

        // When
        var responseEntity = lessonController.createLesson(request);

        // Then
        assertEquals(200, responseEntity.getStatusCode().value());
        CreateLessonResponse response = (CreateLessonResponse) responseEntity.getBody();
        assertNotNull(response);
        assertEquals(uuid, response.lessonId());
        assertEquals(date, response.date());
        assertEquals(startTime, response.startTime());
        assertEquals(endTime, response.endTime());
        assertEquals(maxNumberAttenders, response.maxNumberAttenders());
    }

    @Test
    public void testCreateLesson_whenStartTimeIsLaterThanEndTime_thenShouldReturnBadRequestResponse() throws IOException, ExecutionException, InterruptedException {
        // Given
        CreateLessonRequest request = new CreateLessonRequest("01-01-2024", "11:00", "10:00", 10);

        // When
        var responseEntity = lessonController.createLesson(request);

        // Then
        assertEquals(400, responseEntity.getStatusCode().value());
        assertEquals(String.format("StartTime %S is earlier than EndTime %s", request.startTime(), request.endTime()), responseEntity.getBody());
    }

    @Test
    public void testCreateLesson_whenInvalidDateFormat_shouldReturnBadRequestResponse() throws IOException, ExecutionException, InterruptedException {
        // Given
        CreateLessonRequest request = new CreateLessonRequest("invalid-date-format", "10:00", "11:00", 10);

        // When
        var responseEntity = lessonController.createLesson(request);

        // Then
        assertEquals(400, responseEntity.getStatusCode().value());
        assertEquals("Invalid date (format dd-MM-yyyy)", responseEntity.getBody());
    }

    @Test
    public void testCreateLesson_whenInvalidStartTimeFormat_shouldReturnBadResponse() throws IOException, ExecutionException, InterruptedException {
        // Given
        CreateLessonRequest request = new CreateLessonRequest("31-01-2024", "10:73", "11:00", 10);

        // When
        var responseEntity = lessonController.createLesson(request);

        // Then
        assertEquals(400, responseEntity.getStatusCode().value());
        assertEquals("Invalid start time (format HH:mm)", responseEntity.getBody());
    }

    @Test
    public void testCreateLesson_whenMaxNumberAttendersIsZero_shouldReturnBadRequestResponse() throws IOException, ExecutionException, InterruptedException {
        // Given
        CreateLessonRequest request = new CreateLessonRequest("31-01-2023", "10:00", "11:00", 0);

        // When
        var responseEntity = lessonController.createLesson(request);

        // Then
        assertEquals(400, responseEntity.getStatusCode().value());
        assertEquals(String.format("The parameter maxNumberAttenders must be a positive number. Actual value: %s", request.maxNumberAttenders()),
                responseEntity.getBody());
    }

    @Test
    public void testCreateLesson_whenNegativeMaxNumberAttenders_shouldReturnBadRequestResponse() throws IOException, ExecutionException, InterruptedException {
        // Given
        CreateLessonRequest request = new CreateLessonRequest("31-01-2023", "10:00", "11:00", -1);

        // When
        var responseEntity = lessonController.createLesson(request);

        // Then
        assertEquals(400, responseEntity.getStatusCode().value());
        assertEquals("The parameter maxNumberAttenders must be a positive number. Actual value: -1", responseEntity.getBody());
    }


    @Test
    void testGetLessons_shouldCallTheRepository() throws Exception {
        var lessonsProjection = new ArrayList<Lesson>() {{ add(new Lesson()); }};
        when(getLessonsProjectionUseCase.GetLessonsProjection()).thenReturn(lessonsProjection);

        lessonController.getLessons();

        Mockito.verify(getLessonsProjectionUseCase, times(1)).GetLessonsProjection();
    }
}
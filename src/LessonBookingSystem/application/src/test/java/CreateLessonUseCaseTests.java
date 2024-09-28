import Lesson.DuplicatedLessonException;
import com.federico.LessonBookingSystem.application.projections.ports.out.persistence.LessonProjectionsRepository;
import com.federico.LessonBookingSystem.application.services.ports.out.persistence.LessonRepository;
import Lesson.LessonEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import com.federico.LessonBookingSystem.application.usecases.CreateLessonUseCaseImpl;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

public class CreateLessonUseCaseTests {
    @Mock
    private LessonRepository lessonRepository;

    @Mock
    private LessonProjectionsRepository lessonProjectionsRepository;

    @InjectMocks
    private CreateLessonUseCaseImpl useCase;

    @Captor
    private ArgumentCaptor<ArrayList<LessonEvent>> captor;

    private final LocalDate TOMORROW = LocalDate.now().plusDays(1); // In the future, to pass validations
    private final LocalTime START_TIME = LocalTime.now();
    private final LocalTime END_TIME = LocalTime.now().plusHours(1);
    private final int MAX_NUMBER_ATTENDERS = 5;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void CreateLesson_shouldCallTheLessonRepositoryWithTheCorrectEventType()
            throws IOException, ExecutionException, InterruptedException, DuplicatedLessonException {
        // Arrange
        when(lessonProjectionsRepository.getLessonsByDay(TOMORROW)).thenReturn(List.of());

        // Act
        useCase.CreateLesson(TOMORROW, START_TIME, END_TIME, MAX_NUMBER_ATTENDERS);

        // Assert
        verify(lessonRepository, times(1)).save(captor.capture());
        var capturedValue = captor.getValue();
        assertThat(capturedValue).hasSize(1);
        var capturedEvent = capturedValue.stream().findFirst().get();
        assertThat(capturedEvent.eventStreamId()).isNotBlank();
        assertThat(capturedEvent.eventId()).isNotNull();
        assertThat(capturedEvent.lessonId()).isNotNull();
        assertThat(capturedEvent.version()).isEqualTo(1);
        assertThat(capturedEvent).isInstanceOf(LessonEvent.LessonCreated.class);
        var capturedLessonCreatedEvent = (LessonEvent.LessonCreated)capturedEvent;
        assertThat(capturedLessonCreatedEvent.date()).isEqualTo(TOMORROW);
        assertThat(capturedLessonCreatedEvent.startTime()).isEqualTo(START_TIME);
        assertThat(capturedLessonCreatedEvent.endTime()).isEqualTo(END_TIME);
        assertThat(capturedLessonCreatedEvent.maxNumberAttenders()).isEqualTo(MAX_NUMBER_ATTENDERS);
    }

    @Test
    public void CreateLesson_whenAnOpenLessonWithSameDateAndStartTimeExists_thenShouldThrowADuplicatedLessonException() throws DuplicatedLessonException, IOException, ExecutionException, InterruptedException {
        // This is the test case described in the event modelling model for the case "Create Lesson"
        // Given we create a lesson with the following properties:
        //      - Date: tomorrow
        //      - StartTime: 19:30
        //      - EndTime: 20:30
        //      - MaxNumberAttenders: 16
        // When we try to create another lesson with the properties:
        //      - Date: tomorrow
        //      - StartTime: 19:30
        //      - EndTime: 20:30
        //      - MaxNumberAttenders: 16
        // Then the use case code should throw a DuplicatedLessonException exception

        // Arrange
        when(lessonProjectionsRepository.getLessonsByDay(TOMORROW)).thenReturn(List.of());

        final var firstLesson = useCase.CreateLesson(TOMORROW, START_TIME, END_TIME, MAX_NUMBER_ATTENDERS);

        when(lessonProjectionsRepository.getLessonsByDay(TOMORROW)).thenReturn(List.of(firstLesson));

        // Arrange
        assertThatThrownBy(() -> useCase.CreateLesson(TOMORROW, START_TIME, END_TIME, MAX_NUMBER_ATTENDERS))
                .isInstanceOf(DuplicatedLessonException.class);

    }
}

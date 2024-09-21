import com.federico.LessonBookingSystem.application.services.ports.out.persistence.LessonRepository;
import events.LessonEvent;
import model.Lesson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import com.federico.LessonBookingSystem.application.usecases.CreateLessonUseCaseImpl;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

public class CreateLessonUseCaseTests {
    @Mock
    private LessonRepository lessonRepository;

    @InjectMocks
    private CreateLessonUseCaseImpl useCase;

    @Captor
    private ArgumentCaptor<ArrayList<LessonEvent>> captor;

    private final LocalDate DATE = LocalDate.now().plusDays(1); // In the future, to pass validations
    private final LocalTime START_TIME = LocalTime.now();
    private final LocalTime END_TIME = LocalTime.now().plusHours(1);
    private final int MAX_NUMBER_ATTENDERS = 5;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void CreateLesson_shouldCallTheRepositoryWithTheCorrectEventType() throws IOException, ExecutionException, InterruptedException {
        // Arrange

        // Act
        useCase.CreateLesson(DATE, START_TIME, END_TIME, MAX_NUMBER_ATTENDERS);

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
        assertThat(capturedLessonCreatedEvent.date()).isEqualTo(DATE);
        assertThat(capturedLessonCreatedEvent.startTime()).isEqualTo(START_TIME);
        assertThat(capturedLessonCreatedEvent.endTime()).isEqualTo(END_TIME);
        assertThat(capturedLessonCreatedEvent.maxNumberAttenders()).isEqualTo(MAX_NUMBER_ATTENDERS);
    }
}

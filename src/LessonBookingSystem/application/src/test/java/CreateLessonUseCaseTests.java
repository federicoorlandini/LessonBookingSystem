import com.federico.LessonBookingSystem.application.services.ports.out.persistence.LessonRepository;
import events.LessonEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import usecases.CreateLessonUseCaseImpl;

import java.io.IOException;
import java.time.LocalDateTime;
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

    private final LocalDateTime DATE_AND_TIME = LocalDateTime.now().plusDays(1); // In the future, to pass validations
    private final int MAX_NUMBER_ATTENDERS = 5;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void CreateLesson_shouldCallTheRepositoryWithTheCorrectEventType() throws IOException, ExecutionException, InterruptedException {
        // Arrange

        // Act
        useCase.CreateLesson(DATE_AND_TIME, MAX_NUMBER_ATTENDERS);

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
        assertThat(capturedLessonCreatedEvent.dateAndTime()).isEqualTo(DATE_AND_TIME);
        assertThat(capturedLessonCreatedEvent.maxNumberAttenders()).isEqualTo(MAX_NUMBER_ATTENDERS);
    }
}

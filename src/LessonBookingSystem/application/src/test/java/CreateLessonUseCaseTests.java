import com.federico.LessonBookingSystem.application.services.ports.in.CreateLessonUseCase;
import com.federico.LessonBookingSystem.application.services.ports.out.persistence.LessonRepository;
import events.LessonEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import usecases.CreateLessonUseCaseImpl;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

public class CreateLessonUseCaseTests {
    @Mock
    private LessonRepository lessonRepository;

    @InjectMocks
    private CreateLessonUseCaseImpl useCase;

    @Captor
    private ArgumentCaptor<ArrayList<LessonEvent>> captor;

    private LocalDateTime DATE_AND_TIME = LocalDateTime.now().plusDays(1); // In the future, to pass validations
    private int MAX_NUMBER_ATTENDERS = 5;

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
        var capturedLessonCreadetEvent = (LessonEvent.LessonCreated)capturedEvent;
        assertThat(capturedLessonCreadetEvent.dateAndTime()).isEqualTo(dateAndTime);
        assertThat(capturedLessonCreadetEvent.maxNumberAttenders()).isEqualTo(maxNumberAttendants);
    }
}

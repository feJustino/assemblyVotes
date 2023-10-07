package br.com.justino.assemblyvotes.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import br.com.justino.assemblyvotes.model.Schedule;
import br.com.justino.assemblyvotes.repository.ScheduleRepository;
import br.com.justino.assemblyvotes.service.impl.ScheduleServiceImpl;
import jakarta.persistence.EntityNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class ScheduleServiceImplTest {

    @InjectMocks
    private ScheduleServiceImpl scheduleService;

    @Mock
    private ScheduleRepository scheduleRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void checkAndCloseExpiredSchedules_ShouldCloseExpiredSchedules() {
        // Arrange
        LocalDateTime currentTime = LocalDateTime.now();
        Schedule openSchedule1 = new Schedule();
        openSchedule1.setOpen(true);
        openSchedule1.setEndTime(currentTime.minusMinutes(1));

        Schedule openSchedule2 = new Schedule();
        openSchedule2.setOpen(true);
        openSchedule2.setEndTime(currentTime.plusMinutes(1));

        List<Schedule> openSchedules = Arrays.asList(openSchedule1, openSchedule2);

        when(scheduleRepository.findByOpenIsTrueAndEndTimeLessThan(currentTime)).thenReturn(openSchedules);

        doAnswer(invocation -> {
            Schedule schedule = invocation.getArgument(0);
            schedule.setOpen(false);
            return null;
        }).when(scheduleRepository).save(any(Schedule.class));

        // Act
        scheduleService.checkAndCloseExpiredSchedules();

        // Assert
        assertFalse(openSchedule1.isOpen());
        assertFalse(openSchedule2.isOpen());
        verify(scheduleRepository, times(2)).save(any(Schedule.class));
    }

    @Test
    void save_ShouldSaveScheduleWithGivenDuration() {
        // Arrange
        String description = "Test Schedule";
        int durationMinutes = 30;
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime expectedEndTime = currentTime.plusMinutes(durationMinutes);

        when(scheduleRepository.save(any(Schedule.class))).thenAnswer(invocation -> {
            Schedule savedSchedule = invocation.getArgument(0);
            savedSchedule.setId(1L); // Simulate saving in the database
            return savedSchedule;
        });

        // Act
        Schedule savedSchedule = scheduleService.save(description, durationMinutes);

        // Assert
        assertEquals(description, savedSchedule.getDescricao());
        assertEquals(currentTime, savedSchedule.getStartTime());
        assertEquals(expectedEndTime, savedSchedule.getEndTime());
        assertTrue(savedSchedule.isOpen());
        assertNotNull(savedSchedule.getId());
    }

    @Test
    void findById_ExistingId_ShouldReturnSchedule() {
        // Arrange
        Long scheduleId = 1L;
        Schedule existingSchedule = new Schedule();
        existingSchedule.setId(scheduleId);

        when(scheduleRepository.findById(scheduleId)).thenReturn(Optional.of(existingSchedule));

        // Act
        Schedule foundSchedule = scheduleService.findById(scheduleId);

        // Assert
        assertEquals(existingSchedule, foundSchedule);
    }

    @Test
    void findById_NonExistingId_ShouldThrowEntityNotFoundException() {
        // Arrange
        Long nonExistingId = 999L;

        when(scheduleRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> scheduleService.findById(nonExistingId));
    }
}

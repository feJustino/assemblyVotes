package br.com.justino.assemblyvotes.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import br.com.justino.assemblyvotes.model.Schedule;
import br.com.justino.assemblyvotes.service.ScheduleService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class ScheduleControllerTest {

    private ScheduleController scheduleController;
    private ScheduleService scheduleService;

    @BeforeEach
    void setUp() {
        scheduleService = mock(ScheduleService.class);
        scheduleController = new ScheduleController(scheduleService);
    }

    @Test
    void save_ValidRequest_ShouldReturnCreatedStatus() {
        // Arrange
        String description = "Test Schedule";
        int durationMinutes = 30;
        Schedule savedSchedule = new Schedule();
        savedSchedule.setId(1L);

        when(scheduleService.save(description, durationMinutes)).thenReturn(savedSchedule);

        // Act
        ResponseEntity<Schedule> response = scheduleController.save(description, durationMinutes);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(savedSchedule, response.getBody());
    }

    @Test
    void save_InvalidRequest_ShouldReturnBadRequestStatus() {
        // Arrange
        String description = "Test Schedule";
        int durationMinutes = 30;

        when(scheduleService.save(description, durationMinutes)).thenThrow(EntityNotFoundException.class);

        // Act
        ResponseEntity<Schedule> response = scheduleController.save(description, durationMinutes);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }
}

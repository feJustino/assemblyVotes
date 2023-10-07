package br.com.justino.assemblyvotes.controller;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

import br.com.justino.assemblyvotes.AssemblyVotesApplication;
import br.com.justino.assemblyvotes.exception.ScheduleNotFoundException;
import br.com.justino.assemblyvotes.service.VoteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

class VoteControllerTest {

    private VoteController voteController;
    private VoteService voteService;

    @BeforeEach
    void setUp() {
        voteService = mock(VoteService.class);
        voteController = new VoteController(voteService);
    }

    @Test
    void registerVote_ValidRequest_ShouldReturnHttpStatusCreated() {
        // Arrange
        long scheduleId = 1L;
        long associadoId = 2L;
        boolean vote = true;

        // Act
        ResponseEntity<String> response = voteController.registerVote(scheduleId, associadoId, vote);

        // Assert
        verify(voteService, times(1)).save(scheduleId, associadoId, vote);
        assertSame(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void registerVote_ScheduleNotFoundException_ShouldReturnHttpStatusBadRequestAndErrorMessage() {
        // Arrange
        long scheduleId = 1L;
        long associadoId = 2L;
        boolean vote = true;

        when(voteService.save(scheduleId, associadoId, vote))
            .thenThrow(new ScheduleNotFoundException("Schedule not found"));

        // Act
        ResponseEntity<String> response = voteController.registerVote(scheduleId, associadoId, vote);

        // Assert
        verify(voteService, times(1)).save(scheduleId, associadoId, vote);
        assertSame(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
    }
}

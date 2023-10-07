package br.com.justino.assemblyvotes.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import br.com.justino.assemblyvotes.exception.ScheduleNotFoundException;
import br.com.justino.assemblyvotes.model.Schedule;
import br.com.justino.assemblyvotes.model.Vote;
import br.com.justino.assemblyvotes.repository.VoteRepository;
import br.com.justino.assemblyvotes.service.ScheduleService;
import br.com.justino.assemblyvotes.service.impl.VoteServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

class VoteServiceImplTest {

    private VoteServiceImpl voteService;
    private VoteRepository voteRepository;
    private ScheduleService scheduleService;

    @BeforeEach
    void setUp() {
        voteRepository = mock(VoteRepository.class);
        scheduleService = mock(ScheduleService.class);
        voteService = new VoteServiceImpl(voteRepository, scheduleService);
    }

    @Test
    void save_ValidScheduleAndVote_ShouldSaveVote() {
        // Arrange
        long scheduleId = 1L;
        long associateId = 2L;
        boolean vote = true;

        Schedule openSchedule = new Schedule();
        openSchedule.setId(scheduleId);
        openSchedule.setOpen(true);

        when(scheduleService.findById(scheduleId)).thenReturn(openSchedule);

        // Act
        voteService.save(scheduleId, associateId, vote);

        // Assert
        verify(voteRepository, times(1)).save(any(Vote.class));
    }

    @Test
    void save_ClosedSchedule_ShouldThrowException() {
        // Arrange
        long scheduleId = 1L;
        long associateId = 2L;
        boolean vote = true;

        Schedule closedSchedule = new Schedule();
        closedSchedule.setId(scheduleId);
        closedSchedule.setOpen(false);

        when(scheduleService.findById(scheduleId)).thenReturn(closedSchedule);

        // Act & Assert
        assertThrows(ScheduleNotFoundException.class, () -> {
            voteService.save(scheduleId, associateId, vote);
        });
    }
}

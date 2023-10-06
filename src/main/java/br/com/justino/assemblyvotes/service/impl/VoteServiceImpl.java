package br.com.justino.assemblyvotes.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.justino.assemblyvotes.exception.ScheduleNotFoundException;
import br.com.justino.assemblyvotes.model.Schedule;
import br.com.justino.assemblyvotes.model.Vote;
import br.com.justino.assemblyvotes.repository.VoteRepository;
import br.com.justino.assemblyvotes.service.ScheduleService;
import br.com.justino.assemblyvotes.service.VoteService;
import jakarta.transaction.Transactional;

public class VoteServiceImpl implements VoteService {

  private final VoteRepository voteRepository;
  private final ScheduleService scheduleService;

  private static final Logger logger = LoggerFactory.getLogger(VoteServiceImpl.class);

  @Autowired
  public VoteServiceImpl(VoteRepository voteRepository, ScheduleService scheduleService) {
    this.voteRepository = voteRepository;
    this.scheduleService = scheduleService;
  }

  private void checkIfScheduleIsOpen(Schedule schedule) {
    if (!schedule.isOpen()) {
      logger.warn("Attempted to vote on a closed schedule: {}", schedule.getId());
      throw new ScheduleNotFoundException("A pauta já foi encerrada para votação.");
    }
  }

  @Override
  @Transactional
  public Vote save(Long scheduleId, Long associateId, boolean vote) {
    Schedule schedule = scheduleService.findById(scheduleId);
    checkIfScheduleIsOpen(schedule);
    Vote newVote = new Vote();
    newVote.setSchedule(schedule);
    newVote.setAssociateId(associateId);
    newVote.setVote(vote);
    return voteRepository.save(newVote);
  }
}

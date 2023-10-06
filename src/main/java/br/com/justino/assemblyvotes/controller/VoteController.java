package br.com.justino.assemblyvotes.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.justino.assemblyvotes.exception.ScheduleNotFoundException;
import br.com.justino.assemblyvotes.service.VoteService;

@RestController
@RequestMapping("/api/votes")
public class VoteController {
  private VoteService voteService;

  @Autowired
  public VoteController(VoteService voteService) {
    this.voteService = voteService;
  }

  private static final Logger logger = LoggerFactory.getLogger(VoteController.class);

  @PostMapping
  public ResponseEntity<String> registerVote(@RequestParam Long scheduleId, @RequestParam Long associadoId,
      @RequestParam boolean vote) {
    try {
      voteService.save(scheduleId, associadoId, vote);
      return ResponseEntity.status(HttpStatus.CREATED).build();
    } catch (ScheduleNotFoundException e) {
      logger.error("Error occurred while processing vote request:", e);
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
  }
}
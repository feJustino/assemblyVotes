package br.com.justino.assemblyvotes.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.justino.assemblyvotes.model.Schedule;
import br.com.justino.assemblyvotes.service.ScheduleService;

@RestController
@RequestMapping("/api/schedules")
public class ScheduleController {
    private final ScheduleService scheduleService;

    private static final Logger logger = LoggerFactory.getLogger(VoteController.class);

    @Autowired
    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @PostMapping
    public ResponseEntity<Schedule> save(@RequestBody String desc, @RequestParam(required = false, defaultValue = "1") Integer durationMinutes) {
        try {
            Schedule schedule = scheduleService.save(desc, durationMinutes);
            return ResponseEntity.status(HttpStatus.CREATED).body(schedule);
        } catch (Exception e) {
            logger.error("Error occurred while processing schedule request:", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

}

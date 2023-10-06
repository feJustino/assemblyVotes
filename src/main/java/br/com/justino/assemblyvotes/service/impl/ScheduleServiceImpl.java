package br.com.justino.assemblyvotes.service.impl;

import br.com.justino.assemblyvotes.model.Schedule;
import br.com.justino.assemblyvotes.repository.ScheduleRepository;
import br.com.justino.assemblyvotes.service.ScheduleService;
import jakarta.persistence.EntityNotFoundException;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class ScheduleServiceImpl implements ScheduleService {
  @Autowired
  private ScheduleRepository scheduleRepository;

  @Scheduled(fixedDelay = 60000)
  public void checkAndCloseExpiredSchedules() {
    LocalDateTime currentTime = LocalDateTime.now();
    List<Schedule> schedules = scheduleRepository.findByOpenIsTrueAndEndTimeLessThan(currentTime);

    for (Schedule schedule : schedules) {
      schedule.setOpen(false);
      scheduleRepository.save(schedule);
    }
  }

  @Override
  public Schedule save(String desc, int durationMinutes) {
    LocalDateTime startTime = LocalDateTime.now();
    LocalDateTime endTime = startTime.plusMinutes(durationMinutes);
    
    Schedule schedule = new Schedule();
    schedule.setDescricao(desc);
    schedule.setStartTime(startTime);
    schedule.setEndTime(endTime);
    schedule.setOpen(true);
    return scheduleRepository.save(schedule);
  }

  @Override
  public Schedule findById(Long Id) {
    return scheduleRepository.findById(Id).orElseThrow(() -> new EntityNotFoundException("Pauta não encontrada"));
  }

}
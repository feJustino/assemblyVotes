package br.com.justino.assemblyvotes.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.justino.assemblyvotes.model.Schedule;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
  List<Schedule> findByOpenIsTrueAndEndTimeLessThan(LocalDateTime endTime);
}

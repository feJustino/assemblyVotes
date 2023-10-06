package br.com.justino.assemblyvotes.service;

import br.com.justino.assemblyvotes.model.Schedule;

public interface ScheduleService {
  Schedule save(String desc, int durationMinutes);
  
  Schedule findById(Long Id);
}

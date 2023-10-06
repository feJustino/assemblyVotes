package br.com.justino.assemblyvotes.service;

import br.com.justino.assemblyvotes.model.Vote;

public interface VoteService {
 Vote save(Long scheduleId, Long associadoId, boolean voto); 
}

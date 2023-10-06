package br.com.justino.assemblyvotes.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.justino.assemblyvotes.model.Vote;

public interface VoteRepository extends JpaRepository<Vote, Long> {
  
}

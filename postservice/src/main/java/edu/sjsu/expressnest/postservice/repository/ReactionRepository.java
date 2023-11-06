package edu.sjsu.expressnest.postservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.sjsu.expressnest.postservice.model.Reaction;

public interface ReactionRepository extends JpaRepository<Reaction, Long>{

}
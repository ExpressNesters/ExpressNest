package edu.sjsu.expressnest.postservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.sjsu.expressnest.postservice.model.User;

public interface UserRepository extends JpaRepository<User, Long>{

}

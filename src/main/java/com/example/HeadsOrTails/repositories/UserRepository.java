package com.example.HeadsOrTails.repositories;

import com.example.HeadsOrTails.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Users, Long> {

}

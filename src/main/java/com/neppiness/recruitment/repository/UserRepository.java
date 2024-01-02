package com.neppiness.recruitment.repository;

import com.neppiness.recruitment.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long>, UserCustomRepository {
}

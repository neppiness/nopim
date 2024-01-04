package com.neppiness.nopim.repository;

import com.neppiness.nopim.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long>, UserCustomRepository {
}

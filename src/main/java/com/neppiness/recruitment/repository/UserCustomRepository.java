package com.neppiness.recruitment.repository;

import com.neppiness.recruitment.domain.User;
import java.util.Optional;

public interface UserCustomRepository {

    Optional<User> findByNameAndPassword(String name, String password);

    Optional<User> findByName(String name);

}


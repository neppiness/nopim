package com.neppiness.nopim.repository;

import com.neppiness.nopim.domain.User;
import java.util.Optional;

public interface UserCustomRepository {

    Optional<User> findByNameAndPassword(String name, String password);

    Optional<User> findByName(String name);

}


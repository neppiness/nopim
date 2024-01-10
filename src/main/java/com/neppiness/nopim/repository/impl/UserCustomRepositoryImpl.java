package com.neppiness.nopim.repository.impl;

import static com.neppiness.nopim.domain.QUser.user;

import com.neppiness.nopim.domain.User;
import com.neppiness.nopim.repository.UserCustomRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserCustomRepositoryImpl implements UserCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<User> findByNameAndPassword(String name, String password) {
        return queryFactory
                .selectFrom(user)
                .where(user.name.eq(name))
                .where(user.password.eq(password))
                .stream()
                .findFirst();
    }

    @Override
    public Optional<User> findByName(String name) {
        return queryFactory
                .selectFrom(user)
                .where(user.name.eq(name))
                .stream()
                .findFirst();
    }

}

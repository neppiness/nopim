package com.neppiness.recruitment.repository.impl;

import com.neppiness.recruitment.domain.User;
import com.neppiness.recruitment.repository.UserCustomRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserCustomRepositoryImpl implements UserCustomRepository {

    private final EntityManager entityManager;

    @Override
    public Optional<User> findByNameAndPassword(String name, String password) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> query = builder.createQuery(User.class);
        Root<User> user = query.from(User.class);

        Predicate hasGivenName = builder.equal(user.get("name"), builder.literal(name));
        Predicate hasGivenPassword = builder.equal(user.get("password"), builder.literal(password));
        query.select(user);
        query.where(builder.and(hasGivenName, hasGivenPassword));

        return entityManager
                .createQuery(query)
                .getResultStream()
                .findFirst();
    }

    @Override
    public Optional<User> findByName(String name) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> query = builder.createQuery(User.class);
        Root<User> user = query.from(User.class);

        Predicate hasGivenName = builder.equal(user.get("name"), builder.literal(name));
        query.select(user);
        query.where(hasGivenName);
        return entityManager
                .createQuery(query)
                .getResultStream()
                .findFirst();
    }

}

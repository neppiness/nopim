package com.neppiness.nopim.repository.impl;

import com.neppiness.nopim.domain.Job;
import com.neppiness.nopim.dto.JobResponse;
import com.neppiness.nopim.repository.JobCustomRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JobCustomRepositoryImpl implements JobCustomRepository {

    private final EntityManager entityManager;

    @Override
    public List<JobResponse> findByKeyword(String keyword) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<JobResponse> query = builder.createQuery(JobResponse.class);
        Root<Job> job = query.from(Job.class);

        String pattern = "%" + keyword + "%";
        Predicate companyNameLikeKeyword = builder.like(job.get("company").get("name"), pattern);
        Predicate companyRegionLikeKeyword = builder.like(job.get("company").get("region"), pattern);
        Predicate companyCountryLikeKeyword = builder.like(job.get("company").get("country"), pattern);
        Predicate positionLikeKeyword = builder.like(job.get("position"), pattern);
        Predicate stackLikeKeyword = builder.like(job.get("stack"), pattern);

        query.select(builder.construct(JobResponse.class, job.get("id"), job.get("company").get("name"),
                job.get("company").get("region"), job.get("company").get("country"), job.get("position"),
                job.get("bounty"), job.get("stack"), job.get("status")));
        query.where(builder.or(companyNameLikeKeyword, companyRegionLikeKeyword, companyCountryLikeKeyword,
                positionLikeKeyword, stackLikeKeyword));

        return entityManager
                .createQuery(query)
                .getResultList();
    }

}

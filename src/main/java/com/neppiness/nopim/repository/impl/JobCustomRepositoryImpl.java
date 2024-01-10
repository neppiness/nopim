package com.neppiness.nopim.repository.impl;

import static com.neppiness.nopim.domain.QJob.job;

import com.neppiness.nopim.dto.JobResponse;
import com.neppiness.nopim.repository.JobCustomRepository;
import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JobCustomRepositoryImpl implements JobCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<JobResponse> findByKeyword(String keyword) {
        ConstructorExpression<JobResponse> jobResponse = Projections.constructor(JobResponse.class,
                job.id, job.company.name, job.company.country, job.company.region, job.position, job.bounty, job.stack,
                job.status);

        BooleanExpression companyNameHasKeyword = job.company.name.contains(keyword);
        BooleanExpression companyRegionHasKeyword = job.company.region.contains(keyword);
        BooleanExpression companyCountryHasKeyword = job.company.country.contains(keyword);
        BooleanExpression positionHasKeyword = job.position.contains(keyword);
        BooleanExpression stackHasKeyword = job.stack.contains(keyword);

        BooleanExpression orAll = companyNameHasKeyword
                .or(companyRegionHasKeyword)
                .or(companyCountryHasKeyword)
                .or(positionHasKeyword)
                .or(stackHasKeyword);

        return jpaQueryFactory
                .select(jobResponse)
                .where(orAll)
                .from(job)
                .fetch();
    }

}

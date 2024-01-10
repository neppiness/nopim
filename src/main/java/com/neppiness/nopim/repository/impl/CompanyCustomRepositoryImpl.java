package com.neppiness.nopim.repository.impl;

import static com.neppiness.nopim.domain.QCompany.company;

import com.neppiness.nopim.dto.CompanyResponse;
import com.neppiness.nopim.repository.CompanyCustomRepository;
import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CompanyCustomRepositoryImpl implements CompanyCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<CompanyResponse> findByParameters(String name, String region, String country) {
        ConstructorExpression<CompanyResponse> companyResponse = Projections.constructor(CompanyResponse.class,
                company.id, company.name, company.region, company.country);

        List<BooleanExpression> booleanExpressionList = getBooleanExpressionList(name, region, country);
        BooleanExpression hasGivenConditions = Expressions.TRUE;
        for (BooleanExpression hasGivenCondition : booleanExpressionList) {
            hasGivenConditions = hasGivenConditions.and(hasGivenCondition);
        }

        return jpaQueryFactory
                .select(companyResponse)
                .where(hasGivenConditions)
                .from(company)
                .fetch();
    }

    private List<BooleanExpression> getBooleanExpressionList(String name, String region, String country) {
        List<BooleanExpression> booleanExpressionList = new ArrayList<>();
        if (name != null) {
            booleanExpressionList.add(company.name.eq(name));
        }
        if (region != null) {
            booleanExpressionList.add(company.region.eq(region));
        }
        if (country != null) {
            booleanExpressionList.add(company.country.eq(country));
        }
        return booleanExpressionList;
    }

}

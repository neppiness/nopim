package com.neppiness.nopim.repository.impl;

import com.neppiness.nopim.domain.Company;
import com.neppiness.nopim.dto.CompanyResponse;
import com.neppiness.nopim.repository.CompanyCustomRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Selection;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CompanyCustomRepositoryImpl implements CompanyCustomRepository {

    private final EntityManager entityManager;

    @Override
    public List<CompanyResponse> findByParameters(String name, String region, String country) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<CompanyResponse> query = builder.createQuery(CompanyResponse.class);
        Root<Company> company = query.from(Company.class);

        List<Predicate> predicateList = getPredicateList(builder, company, name, region, country);
        Expression<Boolean> hasGivenConditions = builder.literal(true);
        for (Predicate hasGivenCondition : predicateList) {
            hasGivenConditions = builder.and(hasGivenCondition, hasGivenConditions);
        }
        Selection<Long> idSelection = company.get("id");
        Selection<Long> nameSelection = company.get("name");
        Selection<Long> regionSelection = company.get("region");
        Selection<Long> countrySelection = company.get("country");
        query.select(builder.construct(CompanyResponse.class, idSelection, nameSelection, regionSelection,
                countrySelection));
        query.where(hasGivenConditions);

        return entityManager
                .createQuery(query)
                .getResultList();
    }

    private List<Predicate> getPredicateList(CriteriaBuilder builder, Root<Company> company, String givenName,
                                             String givenRegion, String givenCountry) {
        List<Predicate> predicateList = new ArrayList<>();
        if (givenName != null) {
            Predicate hasGivenName = builder.equal(company.get("name"), builder.literal(givenName));
            predicateList.add(hasGivenName);
        }
        if (givenRegion != null) {
            Predicate hasGivenRegion = builder.equal(company.get("region"), builder.literal(givenRegion));
            predicateList.add(hasGivenRegion);
        }
        if (givenCountry != null) {
            Predicate hasGivenCountry = builder.equal(company.get("country"), builder.literal(givenCountry));
            predicateList.add(hasGivenCountry);
        }
        return predicateList;
    }

}

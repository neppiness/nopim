package recruitment.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.List;
import lombok.RequiredArgsConstructor;
import recruitment.domain.Job;
import recruitment.dto.JobSimpleResponse;
import recruitment.repository.JobCustomRepository;

@RequiredArgsConstructor
public class JobCustomRepositoryImpl implements JobCustomRepository {

    private final EntityManager entityManager;

    @Override
    public List<JobSimpleResponse> findByKeyword(String keyword) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<JobSimpleResponse> query = builder.createQuery(JobSimpleResponse.class);
        Root<Job> job = query.from(Job.class);

        Predicate companyNameLikeKeyword = builder.like(job.get("company").get("name"), "%" + keyword + "%");
        Predicate companyRegionLikeKeyword = builder.like(job.get("company").get("region"), "%" + keyword + "%");
        Predicate companyCountryLikeKeyword = builder.like(job.get("company").get("country"), "%" + keyword + "%");
        Predicate positionLikeKeyword = builder.like(job.get("position"), "%" + keyword + "%");
        Predicate stackLikeKeyword = builder.like(job.get("stack"), "%" + keyword + "%");

        query.select(builder.construct(JobSimpleResponse.class, job.get("id"), job.get("company").get("name"),
                job.get("company").get("region"), job.get("company").get("country"), job.get("position"),
                job.get("bounty"), job.get("stack"), job.get("status")));
        query.where(builder.or(companyNameLikeKeyword, companyRegionLikeKeyword, companyCountryLikeKeyword,
                positionLikeKeyword, stackLikeKeyword));

        return entityManager
                .createQuery(query)
                .getResultList();
    }

}

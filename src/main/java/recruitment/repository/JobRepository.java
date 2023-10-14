package recruitment.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import recruitment.domain.Job;

import java.util.Collection;

public interface JobRepository extends CrudRepository<Job, Long> {

    @Query(value = "SELECT * FROM job j WHERE j.position = :position", nativeQuery = true)
    Collection<Job> findJobsByPosition(
            @Param("position") String position
    );

    @Query(value = "SELECT * FROM job j WHERE j.stack = :stack", nativeQuery = true)
    Collection<Job> findJobsByStack(
            @Param("stack") String stack
    );

    @Query(
            value = "SELECT j.id, j.company_id, j.position, j.bounty, j.stack, j.description, c.name " +
                    "FROM job AS j JOIN company AS c ON j.company_id = c.id " +
                    "WHERE c.name = :companyName",
            nativeQuery = true
    )
    Collection<Job> findJobsByCompanyName(
            @Param("companyName") String companyName
    );
}
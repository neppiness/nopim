package recruitment.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import recruitment.domain.Application;

import java.util.Collection;
import java.util.Optional;

public interface ApplicationRepository extends CrudRepository<Application, Long> {

    @Query(value = "SELECT * FROM application a WHERE a.user_id = :userId AND a.job_id = :jobId", nativeQuery = true)
    Optional<Application> findApplicationByUserIdAndJobId(
            @Param("userId") Long userId,
            @Param("jobId") Long jobId
    );

    @Query(value = "SELECT * FROM application a WHERE a.user_id = :userId", nativeQuery = true)
    Collection<Application> findApplicationsByUserId(
            @Param("userId") Long userId
    );

    @Modifying
    @Query(value = "DELETE FROM application a WHERE a.user_id = :userId", nativeQuery = true)
    void deleteApplicationByUserId(
            @Param("userId") Long userId
    );
}
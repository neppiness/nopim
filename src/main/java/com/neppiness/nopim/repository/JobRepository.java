package com.neppiness.nopim.repository;

import com.neppiness.nopim.domain.Job;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobRepository extends JpaRepository<Job, Long>, JobCustomRepository {
}
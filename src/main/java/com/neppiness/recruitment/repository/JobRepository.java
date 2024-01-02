package com.neppiness.recruitment.repository;

import com.neppiness.recruitment.domain.Job;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobRepository extends JpaRepository<Job, Long>, JobCustomRepository {
}
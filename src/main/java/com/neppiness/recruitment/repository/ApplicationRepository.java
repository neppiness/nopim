package com.neppiness.recruitment.repository;

import com.neppiness.recruitment.domain.Application;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
}
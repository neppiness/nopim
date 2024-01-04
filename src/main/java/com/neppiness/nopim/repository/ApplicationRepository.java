package com.neppiness.nopim.repository;

import com.neppiness.nopim.domain.Application;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
}
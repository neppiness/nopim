package com.neppiness.recruitment.repository;

import com.neppiness.recruitment.dto.JobSimpleResponse;
import java.util.List;

public interface JobCustomRepository {

    List<JobSimpleResponse> findByKeyword(String keyword);

}

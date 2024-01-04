package com.neppiness.nopim.repository;

import com.neppiness.nopim.dto.JobSimpleResponse;
import java.util.List;

public interface JobCustomRepository {

    List<JobSimpleResponse> findByKeyword(String keyword);

}

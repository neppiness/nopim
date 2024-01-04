package com.neppiness.nopim.repository;

import com.neppiness.nopim.dto.JobResponse;
import java.util.List;

public interface JobCustomRepository {

    List<JobResponse> findByKeyword(String keyword);

}

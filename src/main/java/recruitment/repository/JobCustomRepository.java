package recruitment.repository;

import java.util.List;
import recruitment.dto.JobSimpleResponse;

public interface JobCustomRepository {

    List<JobSimpleResponse> findByKeyword(String keyword);

}

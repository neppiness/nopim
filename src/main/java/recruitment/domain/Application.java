package recruitment.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Setter;

@Entity
@Setter
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private long jobId;

    private long userId;

    @JsonProperty("지원내역_id")
    public long getId() {
        return id;
    }

    @JsonProperty("지원내역_채용공고_id")
    public long getJobId() {
        return jobId;
    }

    @JsonProperty("지원내역_사용자_id")
    public long getUserId() {
        return userId;
    }
}

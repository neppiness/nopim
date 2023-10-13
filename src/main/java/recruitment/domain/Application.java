package recruitment.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Setter;

@Entity
@Setter
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "application_id")
    private long id;

    @Column(name = "application_job_id")
    private long jobId;

    @Column(name = "application_user_id")
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

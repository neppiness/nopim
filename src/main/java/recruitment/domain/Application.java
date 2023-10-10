package recruitment.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private long jobId;
    private long userId;
}

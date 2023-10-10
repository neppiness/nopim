package recruitment.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private long companyId;
    private String position;
    private long bounty;
    private String description;
    private String stack;
    private List<Long> otherJobs;

    public JobInShort convertToJobInShort() {
        JobInShort jobInShort = new JobInShort();
        jobInShort.setId(this.id);
        jobInShort.setCompanyId(this.companyId);
        jobInShort.setPosition(this.position);
        jobInShort.setBounty(this.bounty);
        jobInShort.setStack(this.stack);
        return jobInShort;
    }
}

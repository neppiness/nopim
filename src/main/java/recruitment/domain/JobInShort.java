package recruitment.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JobInShort {

    private long id;
    private long companyId;
    private String position;
    private long bounty;
    private String stack;
}

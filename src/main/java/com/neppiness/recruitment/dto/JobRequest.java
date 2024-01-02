package com.neppiness.recruitment.dto;

import com.neppiness.recruitment.domain.Status;
import java.beans.ConstructorProperties;
import lombok.Builder;
import lombok.Getter;

@Getter
public class JobRequest {

    private final Long companyId;

    private final String position;

    private final Long bounty;

    private final String stack;

    private final String description;

    private final Status status;

    @Builder
    @ConstructorProperties({"company-id", "position", "bounty", "stack", "description", "status"})
    public JobRequest(final Long companyId, final String position, final Long bounty, final String stack,
                      final String description, final Status status) {
        this.companyId = companyId;
        this.position = position;
        this.bounty = bounty;
        this.stack = stack;
        this.description = description;
        this.status = status;
    }

}

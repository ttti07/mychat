package com.github.godnsheeps.mychat.domain;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
@Builder
public class Mention {
    @Id
    private String id;

    private Long checked;

    @DBRef
    private User target;

    @DBRef
    private Message message;
}

package com.github.godnsheeps.mychat.domain;

import lombok.Data;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
@SuperBuilder
public class MessageToken {
    @Id
    private String id;

    private String text;
}

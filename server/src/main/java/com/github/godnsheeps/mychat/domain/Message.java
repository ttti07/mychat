package com.github.godnsheeps.mychat.domain;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * @author jcooky
 */
@Document
@Data
@Builder
public class Message {
    @Id
    private String id;

    @DBRef
    private User from;

    private String text;

    @Builder.Default
    private Long timestamp = System.currentTimeMillis();
    private List<MessageToken> messageTokens;

    @DBRef
    private Chat chat;
}

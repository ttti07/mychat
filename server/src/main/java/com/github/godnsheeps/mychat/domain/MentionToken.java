package com.github.godnsheeps.mychat.domain;

import com.mongodb.lang.Nullable;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
@SuperBuilder
public class MentionToken extends MessageToken {
    @Indexed
    @Nullable
    private Long read;

    @DBRef
    private User target;
}

package com.github.godnsheeps.mychat.domain;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface EmojiRepository extends ReactiveMongoRepository<Emoji, String> {
    Flux<Emoji> findAllByUser(User user);
}

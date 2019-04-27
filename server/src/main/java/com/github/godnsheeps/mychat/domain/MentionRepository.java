package com.github.godnsheeps.mychat.domain;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface MentionRepository extends ReactiveMongoRepository<Mention, String> {
    Flux<Mention> findAllByTarget(User target);
}

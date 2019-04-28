package com.github.godnsheeps.mychat.web.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.godnsheeps.mychat.MyChatServerApplication;
import com.github.godnsheeps.mychat.domain.*;
import com.google.common.collect.ImmutableMap;
import io.jsonwebtoken.Jwts;
import lombok.Builder;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class MentionHandler {

    ObjectMapper objectMapper;
    UserRepository userRepository;
    MessageRepository messageRepository;

    @Autowired
    public MentionHandler(ObjectMapper objectMapper, UserRepository userRepository, MessageRepository messageRepository) {
        this.objectMapper = objectMapper;
        this.userRepository = userRepository;
        this.messageRepository = messageRepository;
    }

    public Mono<ServerResponse> getMention(ServerRequest request) {
        return request.bodyToFlux(GetMentionPayload.class)
                .flatMap(payload -> userRepository.findById(Jwts.parser().setSigningKey(MyChatServerApplication.SECRET_KEY)
                                    .parseClaimsJws(payload.getToken())
                                    .getBody().getId()))
                .flatMap(user -> messageRepository.findAll()
                                .filter(message -> message.getMessageTokens().stream()
                                        .filter(tok -> tok instanceof MentionToken)
                                        .anyMatch(tok -> ((MentionToken) tok).getTarget() == user)))
                .flatMap(message -> Flux.fromIterable(message.getMessageTokens())
                                .filter(tok -> tok instanceof MentionToken)
                                .flatMap(tok -> Mono.just(ImmutableMap.of(
                                        "id", tok.getId(),
                                        "from", message.getFrom().getName(),
                                        "messageId", message.getId(),
                                        "userId", ((MentionToken) tok).getTarget().getId()))))
                .collectList()
                .flatMap(t -> ServerResponse.ok().syncBody(t))
                .switchIfEmpty(ServerResponse.noContent().build())
                .onErrorResume(IllegalArgumentException.class, e -> {
                    return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).syncBody(e.getMessage());
                });
    }

    public Mono<ServerResponse> readMention(ServerRequest request) {
        return request.bodyToMono(ReadMentionPayload.class)
                .flatMap(mentionPayload -> {
                    String userId = Jwts.parser().setSigningKey(MyChatServerApplication.SECRET_KEY)
                            .parseClaimsJws(mentionPayload.getToken())
                            .getBody().getId();
                    return Mono.zip(Mono.just(mentionPayload.getMessageId()), Mono.just(userId));
                })
                .flatMap(t -> messageRepository.findById(t.getT1())
                        .flatMap(message -> {
                            message.getMessageTokens().stream()
                                    .filter(tok -> tok instanceof MentionToken)
                                    .filter(tok -> ((MentionToken) tok).getTarget().getId() == t.getT2())
                                    .forEach(tok -> ((MentionToken) tok).setRead(System.currentTimeMillis()));
                            return messageRepository.save(message);
                        }))
                .flatMap(message -> ServerResponse.ok().syncBody(message))
                .switchIfEmpty(ServerResponse.notFound().build())
                .log();
    }

    @Data
    public static class GetMentionPayload {
        String token;
    }

    @Data
    public static class ReadMentionPayload {
        String token;
        String messageId;
    }
}

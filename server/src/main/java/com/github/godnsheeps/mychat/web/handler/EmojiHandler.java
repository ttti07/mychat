package com.github.godnsheeps.mychat.web.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.godnsheeps.mychat.MyChatServerApplication;
import com.github.godnsheeps.mychat.domain.EmojiRepository;
import com.github.godnsheeps.mychat.domain.UserRepository;
import com.github.godnsheeps.mychat.util.Functions;
import io.jsonwebtoken.Jwts;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class EmojiHandler {

    ObjectMapper objectMapper;
    UserRepository userRepository;
    EmojiRepository emojiRepository;

    @Autowired
    public EmojiHandler(ObjectMapper objectMapper, UserRepository userRepository, EmojiRepository emojiRepository) {
        this.objectMapper = objectMapper;
        this.userRepository = userRepository;
        this.emojiRepository = emojiRepository;
    }

    public Mono<ServerResponse> getEmoji(ServerRequest request) {
        val userId = request.queryParam("fromToken")
                .map(fromToken -> Jwts.parser().setSigningKey(MyChatServerApplication.SECRET_KEY)
                        .parseClaimsJws(fromToken)
                        .getBody().getId())
                .orElseThrow();
        return userRepository.findById(userId)
                .flatMap(user -> emojiRepository.findAllByUser(user).collectList())
                .flatMap(emojiList -> ServerResponse.ok().syncBody(emojiList));


    }
}

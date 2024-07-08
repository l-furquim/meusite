package meusite.controller.post.dto;

import java.time.Instant;

public record FeedPostDtoResponse(String content ,String userEmail, String postedAt) {
}

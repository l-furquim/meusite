package meusite.controller.post.dto;

import meusite.controller.comments.dto.GetCommentsFormattedDto;
import meusite.repository.coments.jpa.ComentsJpaEntity;
import meusite.repository.post.jpa.PostJpaEntity;

import java.util.List;

public record GetPostResponseDto(FeedPostDtoResponse post, List<GetCommentsFormattedDto> comments) {
}

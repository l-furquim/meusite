package meusite.controller.likes.dto;

import meusite.repository.likes.jpa.LikesJpaEntity;
import meusite.repository.user.jpa.UserJpaEntity;

import java.util.List;

public record GetUserLikesResponseDto(Long likeId, UserJpaEntity userId, Long postId, Long commentId){
}

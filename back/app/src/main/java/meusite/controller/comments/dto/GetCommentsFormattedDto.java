package meusite.controller.comments.dto;

import meusite.repository.coments.jpa.ComentsJpaEntity;

public record GetCommentsFormattedDto (Long comentId, String content, String userEmail, Long postId, Integer likes){
}

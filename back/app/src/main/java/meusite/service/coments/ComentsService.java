package meusite.service.coments;

import meusite.controller.comments.dto.ComentPostRequestDto;
import meusite.controller.comments.dto.GetCommentsFormattedDto;
import meusite.repository.coments.jpa.ComentsJpaEntity;
import meusite.repository.post.jpa.PostJpaEntity;
import meusite.repository.user.jpa.UserJpaEntity;

import java.util.List;
import java.util.Optional;


public interface ComentsService {
    void comentPost(ComentPostRequestDto comentPostRequestDto, Optional<PostJpaEntity> post, Optional<UserJpaEntity> userJpaEntity);

    List<GetCommentsFormattedDto> listCommentsToModel(List<ComentsJpaEntity> comentsJpaEntityList, String userEmail);
}

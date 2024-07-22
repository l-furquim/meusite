package meusite.service.coments;

import meusite.controller.comments.dto.ComentPostRequestDto;
import meusite.repository.post.jpa.PostJpaEntity;
import meusite.repository.user.jpa.UserJpaEntity;

import java.util.Optional;


public interface ComentsService {
    void comentPost(ComentPostRequestDto comentPostRequestDto, Optional<PostJpaEntity> post, Optional<UserJpaEntity> userJpaEntity);


}

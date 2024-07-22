package meusite.domain.gateway;

import meusite.controller.likes.dto.GetUserLikesResponseDto;
import meusite.repository.likes.jpa.LikesJpaEntity;
import meusite.repository.post.jpa.PostJpaEntity;
import meusite.repository.user.jpa.UserJpaEntity;

import java.util.List;
import java.util.Optional;

public interface LikeGateway {

    void save(LikesJpaEntity likesJpaEntity);

   List<GetUserLikesResponseDto> findLikesByUserIdModel(UserJpaEntity id);

    void delete(LikesJpaEntity likesJpaEntity);

    Optional<LikesJpaEntity> findLikeByUserIdAndPostId(UserJpaEntity userJpaEntity, PostJpaEntity postJpa);

    Optional<List<LikesJpaEntity>> findLikesByUserId(UserJpaEntity id);
}

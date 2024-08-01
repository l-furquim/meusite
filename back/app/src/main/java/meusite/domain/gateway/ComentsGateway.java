package meusite.domain.gateway;

import meusite.repository.coments.ComentsJpaGateway;
import meusite.repository.coments.jpa.ComentsJpaEntity;
import meusite.repository.coments.jpa.ComentsJpaRepository;
import meusite.repository.post.jpa.PostJpaEntity;
import meusite.repository.user.jpa.UserJpaEntity;

import java.util.List;
import java.util.Optional;


public interface ComentsGateway {

    void save(ComentsJpaEntity comentsJpaEntity);

    void updateLikes(Integer likes, Long comentId);

    Optional<ComentsJpaEntity> findById(Long id);

    List<ComentsJpaEntity> findCommentByPostId(PostJpaEntity id);

    Optional<List<ComentsJpaEntity>> findAllByUserId(UserJpaEntity userJpaEntity);

    void delete(ComentsJpaEntity comentsJpaEntity);
}

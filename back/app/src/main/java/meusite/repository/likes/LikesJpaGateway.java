package meusite.repository.likes;

import meusite.controller.likes.dto.GetUserLikesResponseDto;
import meusite.domain.gateway.LikeGateway;
import meusite.repository.coments.jpa.ComentsJpaEntity;
import meusite.repository.likes.jpa.LikesJpaEntity;
import meusite.repository.likes.jpa.LikesRepository;

import meusite.repository.post.jpa.PostJpaEntity;
import meusite.repository.user.jpa.UserJpaEntity;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


public class LikesJpaGateway implements LikeGateway {


    private final LikesRepository likesRepository;

    private LikesJpaGateway(LikesRepository likesRepository) {
        this.likesRepository = likesRepository;
    }


    public static LikesJpaGateway build(final LikesRepository likesRepository){
        return new LikesJpaGateway(likesRepository);
    }


    @Override
    public void save(LikesJpaEntity likesJpaEntity){
        this.likesRepository.save(likesJpaEntity);
    }

    @Override
    public List<GetUserLikesResponseDto> findLikesByUserIdModel(UserJpaEntity id) {
        var likes =  likesRepository.findLikeByUserIdModel(id);

        return likes.get().stream().map(like -> LikesJpaEntity.toModel(like)).collect(Collectors.toList());
    }

    @Override
    public void delete(LikesJpaEntity likesJpaEntity) {
        likesRepository.delete(likesJpaEntity);
    }

    @Override
    public Optional<LikesJpaEntity> findLikeByUserIdAndPostId(UserJpaEntity userJpaEntity, PostJpaEntity postJpa) {
        return likesRepository.findLikeByUserIdAndPostId(userJpaEntity, postJpa);
    }

    @Override
    public Optional<List<LikesJpaEntity>> findLikesByUserId(UserJpaEntity id) {
        return likesRepository.findLikesByUserId(id);
    }

    @Override
    public Optional<LikesJpaEntity> findLikeByUserAndCommentId(UserJpaEntity user, ComentsJpaEntity comment) {
        return this.likesRepository.findLikeByUserAndCommentId(user, comment);
    }


}

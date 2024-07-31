package meusite.service.likes.implementation;

import meusite.domain.gateway.LikeGateway;
import meusite.domain.likes.Likes;
import meusite.repository.coments.exception.ComentsException;
import meusite.repository.coments.jpa.ComentsJpaEntity;
import meusite.repository.likes.jpa.LikesJpaEntity;
import meusite.repository.post.exception.PostException;
import meusite.repository.post.jpa.PostJpaEntity;
import meusite.repository.user.exception.UserException;
import meusite.repository.user.jpa.UserJpaEntity;
import meusite.service.likes.LikesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.OptimisticLockingFailureException;

public class LikesServiceImplementation implements LikesService {

    private static final Logger log = LoggerFactory.getLogger(LikesServiceImplementation.class);
    private LikeGateway likeGateway;

    private LikesServiceImplementation(LikeGateway likeGateway){
        this.likeGateway = likeGateway;
    }

    public static LikesServiceImplementation build(final LikeGateway likeGateway){
        return new LikesServiceImplementation(likeGateway);
    }

    @Override
    public void likePost(UserJpaEntity user, PostJpaEntity postJpa) {
        var like = this.likeGateway.findLikesByUserId(user);
        Logger logger = LoggerFactory.getLogger(LikesServiceImplementation.class);

        logger.info(postJpa.toString());


        if (like.isPresent()) {

            for (int i = 0; i < like.get().size(); i++) {


                if (like.get().get(i).getPostId() == postJpa.getTweetId()) {
                    throw new PostException("Voce nao pode curtir o mesmo post duas vezes");
                }
            }

            Likes likes = new Likes(user, postJpa);

            var likesEntity = LikesJpaEntity.fromPost(likes);

            likesEntity.setComentId(null);

            try {
                this.likeGateway.save(likesEntity);
            } catch (IllegalArgumentException e) {
                throw new PostException(e.getMessage());
            } catch (OptimisticLockingFailureException e) {
                throw new UserException((e.getMessage()));
            }

        }
    }


    @Override
    public void likeComment(UserJpaEntity user,ComentsJpaEntity coments) {
        var like = this.likeGateway.findLikesByUserId(user);
        Logger logger = LoggerFactory.getLogger(LikesServiceImplementation.class);

        if (like.isPresent()) {
            for (int i = 0; i < like.get().size(); i++) {
                if (like.get().get(i).getComentId() == coments.getId()) {
                    throw new PostException("Voce nao pode curtir o mesmo comentario duas vezes");
                }
            }
        }
            Likes likeComent = new Likes(
                    user,
                    coments
            );

            LikesJpaEntity likesJpaEntity = LikesJpaEntity.fromComment(likeComent);
            likesJpaEntity.setPostId(null);
            try {
                likeGateway.save(likesJpaEntity);
            } catch (IllegalArgumentException illegalArgumentException) {
                throw new UserException(illegalArgumentException.getMessage());
            } catch (OptimisticLockingFailureException e) {
                throw new UserException((e.getMessage()));
            }
        }

    @Override
    public void unlikeComment(UserJpaEntity user, ComentsJpaEntity comment) {
        var like = likeGateway.findLikeByUserAndCommentId(user,comment);

        if(like.isPresent()){
            likeGateway.delete(like.get());
        }
        throw new ComentsException("nao foi possivel dar deslike em um comentario que nao possui o like indentificado !");
    }
}

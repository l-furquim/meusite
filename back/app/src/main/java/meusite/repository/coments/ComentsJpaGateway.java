package meusite.repository.coments;

import meusite.domain.gateway.ComentsGateway;
import meusite.repository.coments.jpa.ComentsJpaEntity;
import meusite.repository.coments.jpa.ComentsJpaRepository;
import meusite.repository.post.jpa.PostJpaEntity;
import meusite.repository.user.jpa.UserJpaEntity;

import java.util.List;
import java.util.Optional;


public class ComentsJpaGateway implements ComentsGateway {


    private ComentsJpaRepository comentsJpaRepository;

    private ComentsJpaGateway(ComentsJpaRepository comentsJpaRepository) {
        this.comentsJpaRepository = comentsJpaRepository;
    }

    public static ComentsJpaGateway build(final ComentsJpaRepository comentsJpaRepository){
        return new ComentsJpaGateway(comentsJpaRepository);
    }


    @Override
    public void save(ComentsJpaEntity comentsJpaEntity) {
        comentsJpaRepository.save(comentsJpaEntity);
    }

    @Override
    public void updateLikes(Integer likes, Long comentId) {
        comentsJpaRepository.updateComents(likes, comentId);
    }

    @Override
    public Optional<ComentsJpaEntity> findById(Long id) {
        return comentsJpaRepository.findById(id);
    }

    @Override
    public List<ComentsJpaEntity> findCommentByPostId(PostJpaEntity postJpaEntity) {


        return comentsJpaRepository.findCommentByTweetId(postJpaEntity);
    }

    @Override
    public Optional<List<ComentsJpaEntity>> findAllByUserId(UserJpaEntity userJpaEntity){
        return comentsJpaRepository.findCommentByUserId(userJpaEntity);
    }

    @Override
    public void delete(ComentsJpaEntity comentsJpaEntity) {
        comentsJpaRepository.delete(comentsJpaEntity);
    }


}

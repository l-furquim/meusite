package meusite.service.likes;


import meusite.repository.coments.jpa.ComentsJpaEntity;
import meusite.repository.post.jpa.PostJpaEntity;
import meusite.repository.user.jpa.UserJpaEntity;

public interface LikesService {

    void likePost(UserJpaEntity user, PostJpaEntity postJpa);

    void likeComment(UserJpaEntity user,ComentsJpaEntity coments);



}

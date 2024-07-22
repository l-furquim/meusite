package meusite.domain.likes;

import meusite.repository.coments.jpa.ComentsJpaEntity;
import meusite.repository.post.jpa.PostJpaEntity;
import meusite.repository.user.jpa.UserJpaEntity;

public class Likes {
    private Long id;
    private UserJpaEntity userId;
    private PostJpaEntity postId;
    private ComentsJpaEntity comentId;

    public Likes(){

    }

    public Likes(UserJpaEntity userId, PostJpaEntity postId) {
        this.userId = userId;
        this.postId = postId;
    }

    public Likes(UserJpaEntity userId, ComentsJpaEntity comentId) {
        this.userId = userId;
        this.comentId = comentId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserJpaEntity getUserId() {
        return userId;
    }

    public void setUserId(UserJpaEntity userId) {
        this.userId = userId;
    }

    public PostJpaEntity getPostId() {
        return postId;
    }

    public void setPostId(PostJpaEntity postId) {
        this.postId = postId;
    }

    public ComentsJpaEntity getComentId() {
        return comentId;
    }

    public void setComentId(ComentsJpaEntity comentId) {
        this.comentId = comentId;
    }
}

package meusite.domain.coments;

import meusite.repository.coments.exception.ComentsException;
import meusite.repository.post.jpa.PostJpaEntity;
import meusite.repository.user.jpa.UserJpaEntity;

public class Coments {
    private Long id;
    private String content;
    private int likes;
    private UserJpaEntity user;
    private PostJpaEntity post;

    public Coments(String content, int likes,UserJpaEntity user, PostJpaEntity postId) {

        this.content = content;
        this.likes = likes;
        this.user = user;
        this.post = postId;
    }

    public Coments() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public UserJpaEntity  getUserId() {
        return user;
    }

    public void setUserId(UserJpaEntity userId) {
        this.user = userId;
    }

    public PostJpaEntity getPostId() {
        return post;
    }

    public void setPostId(PostJpaEntity postId) {
        this.post= postId;
    }
    public void validate(){
        if(this.content.length() == 0){
            throw new ComentsException("Não eh possivel fazer um comentario vazio");
        }
    }
}

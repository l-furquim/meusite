package meusite.repository.likes.jpa;

import jakarta.persistence.*;
import meusite.controller.likes.dto.GetUserLikesResponseDto;
import meusite.domain.likes.Likes;
import meusite.repository.coments.jpa.ComentsJpaEntity;
import meusite.repository.likes.LikesJpaGateway;
import meusite.repository.post.jpa.PostJpaEntity;
import meusite.repository.user.jpa.UserJpaEntity;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "likes")
public class LikesJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "like_id")
    private Long like_id;

    @ManyToOne
    @JoinColumn(name = "id")
    private UserJpaEntity id;

    @ManyToOne
    @JoinColumn(name = "tweet_id")
    private PostJpaEntity tweet_id;

    @ManyToOne
    @JoinColumn(name = "coment_id")
    private ComentsJpaEntity coment_id;

    public LikesJpaEntity(){

    }

    public LikesJpaEntity(UserJpaEntity userId, ComentsJpaEntity comentId) {
        this.id = userId;
        this.coment_id = comentId;
    }

    public LikesJpaEntity(UserJpaEntity userId) {
        this.id = userId;
    }

    public LikesJpaEntity(UserJpaEntity userId, PostJpaEntity postId) {
        this.id = userId;
        this.tweet_id = postId;
    }

    public Long getId() {
        return like_id;
    }

    public void setId(Long id) {
        this.like_id = id;
    }

    public UserJpaEntity getUserId() {
        return id;
    }

    public void setUserId(UserJpaEntity userId) {
        this.id = userId;
    }

    public Long getPostId() {
        if(tweet_id == null){
            return 0L;
        }
        return tweet_id.getTweetId();
    }

    public void setPostId(PostJpaEntity postId) {
        this.tweet_id = postId;
    }

    public Long getComentId() {
        if(coment_id == null){
            return 0L;
        }
        return coment_id.getId();
    }

    public void setComentId(ComentsJpaEntity comentId) {
        this.coment_id = comentId;
    }
    public static LikesJpaEntity fromPost(Likes likes){
        return new LikesJpaEntity(
                likes.getUserId(),
                likes.getPostId()
        );
    }
    public static LikesJpaEntity fromComment(Likes likes){
        return new LikesJpaEntity(
                likes.getUserId(),
                likes.getComentId()
        );
    }
    public static GetUserLikesResponseDto toModel(LikesJpaEntity likesJpaEntity){
        return new GetUserLikesResponseDto(
                likesJpaEntity.getId(),
                null,
                likesJpaEntity.getPostId(),
                likesJpaEntity.getComentId()
        );
    }


}

package meusite.repository.coments.jpa;

import jakarta.persistence.*;
import meusite.controller.comments.dto.GetCommentsFormattedDto;
import meusite.domain.coments.Coments;
import meusite.repository.post.jpa.PostJpaEntity;
import meusite.repository.user.jpa.UserJpaEntity;

import java.util.List;

@Entity
@Table(name = "coments")
public class ComentsJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coment_id")
    private Long coment_id;

    @Column(name = "content")
    private String content;

    @ManyToOne
    @JoinColumn(name = "id")
    private UserJpaEntity id;

    @ManyToOne
    @JoinColumn(name = "tweet_id")
    private PostJpaEntity tweet_id;


    @Column(name = "likes")
    private Integer likes;

    public ComentsJpaEntity(){

    }

    public ComentsJpaEntity(String content, UserJpaEntity userId, PostJpaEntity postID, Integer likes) {
        this.content = content;
        this.id = userId;
        this.tweet_id = postID;
        this.likes = likes;
    }

    public Long getId() {
        return coment_id;
    }

    public void setId(Long id) {
        this.coment_id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public UserJpaEntity getUserId() {
        return id;
    }

    public void setUserId(UserJpaEntity userId) {
        this.id = userId;
    }

    public Long getPostID() {
        return tweet_id.getTweetId();
    }

    public void setPostID(PostJpaEntity postID) {
        this.tweet_id = postID;
    }

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    public static ComentsJpaEntity from(Coments  coments){
        return new ComentsJpaEntity(
                coments.getContent(),
                coments.getUserId(),
                coments.getPostId(),
                coments.getLikes()
        );
    }
    public static GetCommentsFormattedDto toModel(ComentsJpaEntity comentsJpaEntity, String userEmail){
        return new GetCommentsFormattedDto(
                comentsJpaEntity.getId(),
                comentsJpaEntity.getContent(),
                userEmail,
                comentsJpaEntity.getPostID(),
                comentsJpaEntity.getLikes()
        );
    }


}

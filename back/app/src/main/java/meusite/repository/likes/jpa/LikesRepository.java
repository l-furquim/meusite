package meusite.repository.likes.jpa;

import meusite.repository.post.jpa.PostJpaEntity;
import meusite.repository.user.jpa.UserJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikesRepository extends JpaRepository<LikesJpaEntity, Long> {

    @Query("SELECT l FROM LikesJpaEntity l WHERE l.id = :id")
    Optional<List<LikesJpaEntity>> findLikeByUserIdModel(@Param("id") UserJpaEntity id);

    @Query("SELECT l FROM LikesJpaEntity l WHERE l.id = :id AND l.tweet_id = :postId")
    Optional<LikesJpaEntity> findLikeByUserIdAndPostId(@Param("id") UserJpaEntity id, @Param("postId") PostJpaEntity postId);

    @Query("Select l FROM LikesJpaEntity l WHERE l.id = :id")
    Optional<List<LikesJpaEntity>> findLikesByUserId(@Param("id") UserJpaEntity id);
}

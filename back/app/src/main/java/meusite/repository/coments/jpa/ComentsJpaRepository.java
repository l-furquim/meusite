package meusite.repository.coments.jpa;

import meusite.repository.post.jpa.PostJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ComentsJpaRepository extends JpaRepository<ComentsJpaEntity, Long> {

    @Modifying
    @Transactional
    @Query("UPDATE ComentsJpaEntity c SET c.likes = :likes WHERE c.coment_id = :comentId")
    void updateComents(@Param("likes") Integer likes, @Param("comentId") Long comentId);

    @Query("SELECT c FROM ComentsJpaEntity c WHERE c.tweet_id = :tweet_id")
    List<ComentsJpaEntity> findCommentByTweetId(@Param("tweet_id")PostJpaEntity tweetId);
}

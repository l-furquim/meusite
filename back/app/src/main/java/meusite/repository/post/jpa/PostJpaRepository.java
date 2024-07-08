package meusite.repository.post.jpa;

import meusite.repository.user.jpa.UserJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface PostJpaRepository extends JpaRepository<PostJpaEntity,Long> {

    @Query("SELECT u FROM UserJpaEntity u WHERE u.id = :id")
    Optional<UserJpaEntity> findUserFromHisId(@Param("id")String id);

    @Modifying
    @Transactional
    @Query("UPDATE PostJpaEntity p SET p.content = :content, p.timeStampTweet = :timestamp WHERE p.tweetId = :tweetId")
    void updatePostContent(@Param("tweetId") Long tweetId, @Param("content") String content,@Param("timestamp") Instant timestamp);

    @Query("SELECT p FROM PostJpaEntity p")
    List<PostJpaEntity> findAllPostData();


}

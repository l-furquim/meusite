package meusite.repository.post.jpa;

import meusite.repository.user.jpa.UserJpaEntity;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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


    @Query(value = "SELECT * FROM post_table LIMIT :limit", nativeQuery = true)
    List<PostJpaEntity> findLimitedNumberOfPosts(@Param("limit") int limit);


    Page<PostJpaEntity> findAll(Pageable pageable);

    @Modifying
    @Transactional
    @Query("UPDATE PostJpaEntity p SET p.likes = :likes WHERE p.tweetId = :tweetId")
    void updateLikes(@Param("likes") Integer likes,@Param("tweetId") Long tweetId);

    @Modifying
    @Transactional
    @Query("UPDATE PostJpaEntity p SET p.ncoments = :ncoments WHERE p.tweetId = :tweetId")
    void updateComents(@Param("ncoments") Integer coments,@Param("tweetId") Long tweetId);

    @Query("SELECT p FROM PostJpaEntity p WHERE p.id = :id")
    Optional<List<PostJpaEntity>> findAllByUserId(@Param("id") UserJpaEntity userJpaEntity);
}

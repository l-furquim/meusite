package meusite.repository.post;

import meusite.controller.post.dto.EditPostDtoRequest;
import meusite.controller.post.dto.FeedPostDtoResponse;
import meusite.repository.post.jpa.PostJpaEntity;
import meusite.repository.post.jpa.PostJpaRepository;
import meusite.repository.user.UserJpaGateWay;
import meusite.repository.user.jpa.UserJpaEntity;
import meusite.repository.user.jpa.UserJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
@Service
public class PostJpaGateWay {
    @Autowired
    private PostJpaRepository postJpaRepository;

    private PostJpaGateWay(PostJpaRepository postJpaRepository) {
        this.postJpaRepository = postJpaRepository;
    }
    public static PostJpaGateWay build(final PostJpaRepository postJpaRepository){
        return new PostJpaGateWay(postJpaRepository);
    }

    public void save(PostJpaEntity postJpaEntity){
        this.postJpaRepository.save(postJpaEntity);
    }
    public Optional<UserJpaEntity> findUserByHisId(String id){
        return this.postJpaRepository.findUserFromHisId(id);
    }

    public void editPostContent(Long tweetId,EditPostDtoRequest editPostDtoRequest){
        ZoneId zoneId = ZoneId.systemDefault();

        Instant instant = Instant.now();

        ZonedDateTime zonedDateTime = instant.atZone(zoneId);
        this.postJpaRepository.updatePostContent(tweetId,editPostDtoRequest.newContent(), Instant.from(zonedDateTime));
    }

    public Optional<PostJpaEntity> findPostById(Long id){
        return this.postJpaRepository.findById(id);
    }
    public List<PostJpaEntity> getAllPosts(){
        return this.postJpaRepository.findAll();
    }

    public List<PostJpaEntity> getLimitedPosts(Integer number){
        return this.postJpaRepository.findLimitedNumberOfPosts(number);
    }

    public Page<PostJpaEntity> findAll(Pageable number){
        return this.findAll(number);
    }
}

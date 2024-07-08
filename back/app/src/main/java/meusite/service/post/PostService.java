package meusite.service.post;

import meusite.controller.post.dto.EditPostDtoRequest;
import meusite.controller.post.dto.FeedPostDtoResponse;
import meusite.controller.post.dto.NewPostDtoRequest;
import meusite.controller.post.dto.NewPostResponseDto;
import meusite.domain.post.Post;
import meusite.repository.post.PostJpaGateWay;
import meusite.repository.post.exception.PostException;
import meusite.repository.post.jpa.PostJpaEntity;
import meusite.repository.user.UserJpaGateWay;
import meusite.repository.user.jpa.UserJpaRepository;
import meusite.service.auth.AuthService;
import meusite.service.exception.ServiceException;
import meusite.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Service
public class PostService {

    @Autowired
    AuthService authService;

    @Autowired
    PostJpaGateWay postJpaGateWay;

    @Autowired
    UserJpaRepository userJpaRepository;

    public PostService(){
    }

    public PostService(PostJpaGateWay postJpaGateWay){
        this.postJpaGateWay = postJpaGateWay;
    }

    public NewPostResponseDto createPost(NewPostDtoRequest postDtoRequest, String token){

        var aContent = postDtoRequest.content();
        var aToken = token;

        if(aToken.isEmpty() || aContent.isEmpty() ){
            throw new PostException("Conteudo nao pode ser em banco !");
        }
        var user = authService.extractUserFromToken(aToken);

        if(user.isEmpty()){
            throw new PostException("usuario invalido !");
        }
        var post = new Post(
                aContent,
                user.get(),
                Instant.now()

        );
        var postEntity = PostJpaEntity.from(post);
        postJpaGateWay.save(postEntity);
        return new NewPostResponseDto(post.getContent(),post.getUser().getEmail());
    }

    public void save(PostJpaEntity postJpaEntity){
        this.postJpaGateWay.save(postJpaEntity);
    }

    public void editPost(Long tweetId,EditPostDtoRequest editPostDtoRequest){
        this.postJpaGateWay.editPostContent(tweetId,editPostDtoRequest);
    }
    public Optional<PostJpaEntity> findPostById(Long id){
        return this.postJpaGateWay.findPostById(id);
    }
    public List<FeedPostDtoResponse> getAllPostsFormated(){
            var ob = this.postJpaGateWay.getAllPosts();

            return ob.stream().map(post -> PostJpaEntity.toModel(post)).collect(Collectors.toList());

    }
}

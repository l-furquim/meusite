package meusite.service.post;
import meusite.controller.post.dto.*;
import meusite.domain.post.Post;
import meusite.repository.post.PostJpaGateWay;
import meusite.repository.post.exception.PostException;
import meusite.repository.post.jpa.PostJpaEntity;
import meusite.repository.user.jpa.UserJpaEntity;
import meusite.service.auth.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class PostService {

    @Autowired
    private AuthService authService;

    @Autowired
    private PostJpaGateWay postJpaGateWay;


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
        post.setLikes(0);
        post.setComents(0);

        var postEntity = PostJpaEntity.from(post);
        postJpaGateWay.save(postEntity);
        return new NewPostResponseDto(post.getContent(),post.getUser().getEmail(), post.getLikes(), post.getComents());
    }

    public void save(PostJpaEntity postJpaEntity){
        this.postJpaGateWay.save(postJpaEntity);
    }

    public void editPost(Long tweetId,EditPostDtoRequest editPostDtoRequest){
        this.postJpaGateWay.editPostContent(tweetId,editPostDtoRequest);
    }
    public Optional<PostJpaEntity> findPostById(Long id){
        var aId = postJpaGateWay.findPostById(id);

        if(aId.isEmpty()){
            throw new PostException("Não foi possivel encontrar  o post pelo id");
        }
        return aId;
    }
    public List<FeedPostDtoResponse> getAllPostsFormated(){
            var ob = this.postJpaGateWay.getAllPosts();

            return ob.stream().map(post -> PostJpaEntity.toModel(post)).collect(Collectors.toList());

    }

    public List<FeedPostDtoResponse> getLimitedPosts(Integer number) {
        var list =  this.postJpaGateWay.getLimitedPosts(number);

        return list.stream().map(ob -> PostJpaEntity.toModel(ob)).collect(Collectors.toList());
    }
    public List<FeedPostDtoResponse> getPostsInRange(int start, int size) {
        PageRequest pageRequest = PageRequest.of(start / size, size);
        Page<PostJpaEntity> page = this.postJpaGateWay.findAll(pageRequest);

        var list = page.getContent();

        return list.stream().map(posts -> PostJpaEntity.toModel(posts)).collect(Collectors.toList());
    }

    public void updateComents(Integer coments, Long tweetId){
        this.postJpaGateWay.updateComents(coments,tweetId);
    }

    public void updateLikes(Integer likes, Long tweetId){
        postJpaGateWay.updateLikes(likes, tweetId);
    }
    public Optional<List<PostJpaEntity>> findAllByUserId(UserJpaEntity userJpaEntity){
        return postJpaGateWay.findAllByUserId(userJpaEntity);
    }











}

package meusite.controller.likes;

import meusite.controller.likes.dto.GetUserLikesResponseDto;
import meusite.repository.likes.LikesJpaGateway;
import meusite.repository.likes.jpa.LikesRepository;
import meusite.repository.post.PostJpaGateWay;
import meusite.repository.post.jpa.PostJpaRepository;
import meusite.service.auth.AuthService;
import meusite.service.likes.implementation.LikesServiceImplementation;
import meusite.service.post.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/likes")
public class LikesController {

    @Autowired
    LikesRepository likesRepository;

    @Autowired
    PostJpaRepository postJpaRepository;

    @Autowired
    AuthService aAuthService;

    @PostMapping("/like/{postId}")
    public ResponseEntity<Void> likePost(@RequestHeader("Authorization") String header, @PathVariable("postId") Long postId){
        var aPostGateway = PostJpaGateWay.build(postJpaRepository);
        var postService = new PostService(aPostGateway);

        var cookie = header.substring(7);
        var user = aAuthService.extractUserFromToken(cookie);
        var post = postService.findPostById(postId);

        var aLikeGateWay = LikesJpaGateway.build(likesRepository);

        var aLikeService = LikesServiceImplementation.build(aLikeGateWay);



        aLikeService.likePost(user.get(),post.get());
        postService.updateLikes(post.get().getLikes() + 1, post.get().getTweetId());


        return ResponseEntity.ok().build();
    }
    @DeleteMapping("/unlike/{postId}")
    public ResponseEntity<Void> unLikePost(@RequestHeader("Authorization") String header, @PathVariable("postId") Long postId){
        var aPostGateway = PostJpaGateWay.build(postJpaRepository);
        var postService = new PostService(aPostGateway);

        var cookie = header.substring(7);
        var user = aAuthService.extractUserFromToken(cookie);
        var post = postService.findPostById(postId);

        var aLikeGateWay = LikesJpaGateway.build(likesRepository);

        var aLikeService = LikesServiceImplementation.build(aLikeGateWay);

        aLikeGateWay.delete(aLikeGateWay.findLikeByUserIdAndPostId(user.get(), post.get()).get());

        postService.updateLikes(post.get().getLikes() - 1, post.get().getTweetId());

        return ResponseEntity.ok().build();
    }

    @GetMapping("/{token}")
    public ResponseEntity<List<GetUserLikesResponseDto>> getUserLikes(@PathVariable("token") String token){
        var user = aAuthService.extractUserFromToken(token);

        var aLikeGateway = LikesJpaGateway.build(likesRepository);
        var likes = aLikeGateway.findLikesByUserIdModel(user.get());


        return ResponseEntity.ok().body(likes);
    }


}

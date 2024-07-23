package meusite.controller.comments;

import meusite.controller.comments.dto.ComentPostRequestDto;
import meusite.repository.coments.ComentsJpaGateway;
import meusite.repository.coments.jpa.ComentsJpaRepository;
import meusite.repository.likes.LikesJpaGateway;
import meusite.repository.likes.jpa.LikesRepository;
import meusite.repository.post.PostJpaGateWay;
import meusite.repository.post.jpa.PostJpaRepository;
import meusite.service.auth.AuthService;
import meusite.service.coments.implementation.ComentsServiceImplementation;
import meusite.service.likes.implementation.LikesServiceImplementation;
import meusite.service.post.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comments")
public class CommentsController {

    @Autowired
    PostJpaRepository postJpaRepository;

    @Autowired
    ComentsJpaRepository comentsJpaRepository;

    @Autowired
    LikesRepository likesRepository;

    @Autowired
    AuthService aAuthService;


    @PostMapping("/{postId}")
    public ResponseEntity<String> comentPost(@RequestHeader("Authorization") String header, @PathVariable("postId") Long postId,
                                             @RequestBody ComentPostRequestDto comentPostRequestDto){
        var aPostGateway = PostJpaGateWay.build(postJpaRepository);
        var postService = new PostService(aPostGateway);
        var user = aAuthService.extractUserFromToken(header.substring(7));
        var post = postService.findPostById(postId);

        var aCommentGateway = ComentsJpaGateway.build(comentsJpaRepository);

        var aCommentService = ComentsServiceImplementation.build(aCommentGateway);

        aCommentService.comentPost(comentPostRequestDto,post,user);

        postService.updateComents(post.get().getComents() + 1, post.get().getTweetId());

        return ResponseEntity.ok().body("Post comentado com sucesso !");
    }


    @PostMapping("/{commentId}/like")
    public ResponseEntity<Void> likeComent(@PathVariable("commentId") Long comentId,@RequestHeader("Authorization") String header){
        var aPostGateway = PostJpaGateWay.build(postJpaRepository);
        var postService = new PostService(aPostGateway);

        var aCommentGateway = ComentsJpaGateway.build(comentsJpaRepository);
        var aLikeGateway = LikesJpaGateway.build(likesRepository);
        var aLikeService = LikesServiceImplementation.build(aLikeGateway);

        var cookie = header.substring(7);
        var user = aAuthService.extractUserFromToken(cookie);
        var comment = aCommentGateway.findById(comentId);
        var post = postService.findPostById(comment.get().getPostID());


        aLikeService.likeComment(user.get(),comment.get());
        aCommentGateway.updateLikes(comment.get().getLikes() + 1, comment.get().getId());
        return ResponseEntity.ok().build();
    }
}

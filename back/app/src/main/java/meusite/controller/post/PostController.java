package meusite.controller.post;


import jakarta.servlet.http.HttpServletRequest;
import meusite.controller.post.dto.EditPostDtoRequest;
import meusite.controller.post.dto.FeedPostDtoResponse;
import meusite.controller.post.dto.NewPostDtoRequest;
import meusite.controller.post.dto.NewPostResponseDto;
import meusite.domain.post.Post;
import meusite.repository.post.PostJpaGateWay;
import meusite.repository.post.jpa.PostJpaEntity;
import meusite.repository.post.jpa.PostJpaRepository;
import meusite.repository.user.jpa.UserJpaRepository;
import meusite.service.post.PostService;
import meusite.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/post")
public class PostController {

    @Autowired
    private PostJpaRepository postJpaRepository;

    @Autowired
    private PostJpaGateWay aGateWay = PostJpaGateWay.build(postJpaRepository);

    @Autowired
    private PostService postService = new PostService(aGateWay);

    @PostMapping
    public ResponseEntity<NewPostResponseDto> createPost(
            @RequestBody NewPostDtoRequest postDtoRequest, @RequestHeader("Authorization") String authorizationheader
            ){
        var aGateWay = PostJpaGateWay.build(postJpaRepository);


        var token = authorizationheader.substring(7);
        System.out.println(token);

        //var aService = new PostService(aGateWay);


        var response = postService.createPost(postDtoRequest, token);

        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/{id}/edit")
    public ResponseEntity<String> editPost(@RequestBody EditPostDtoRequest editPostDtoRequest,
    @PathVariable("id") Long id){
        var aGateWay = PostJpaGateWay.build(postJpaRepository);

        var aService = new PostService(aGateWay);

        var tweeId = aService.findPostById(id);

        var aContent = editPostDtoRequest;

        if(tweeId.isEmpty()){
            ResponseEntity.badRequest().body("Nao existe nenhum tweet com este id");
        }if(aContent.newContent().isEmpty()){
            ResponseEntity.badRequest().body("Nao e possivel editar o tweet para ser vazio !");
        }
        aService.editPost(id,aContent);
        return ResponseEntity.ok().body("Tweet editado com sucesso !");
    }

    @GetMapping("/feed")
    public ResponseEntity<List<FeedPostDtoResponse>> getFeed() {
        var posts = postService.getAllPostsFormated();

        if(posts.isEmpty()){
        return ResponseEntity.internalServerError().body( posts
        );
        }
        return ResponseEntity.ok().body(posts);

    }

}

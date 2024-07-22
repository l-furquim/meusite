package meusite.controller.post;

import meusite.controller.post.dto.*;
import meusite.repository.coments.jpa.ComentsJpaRepository;
import meusite.repository.likes.jpa.LikesRepository;
import meusite.repository.post.PostJpaGateWay;
import meusite.repository.post.jpa.PostJpaRepository;
import meusite.service.auth.AuthService;
import meusite.service.post.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/post")
public class PostController {

    @Autowired
    AuthService aAuthService;

    @Autowired
    private PostJpaRepository postJpaRepository;

    @Autowired
    private PostJpaGateWay aGateWay = PostJpaGateWay.build(postJpaRepository);

    @Autowired
    private PostService postService = new PostService(aGateWay);

    @Autowired
    private ComentsJpaRepository comentsJpaRepository;

    @Autowired
    private LikesRepository likesRepository;


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

    @GetMapping("post/feed?page={pageNumber}")
    public ResponseEntity<List<FeedPostDtoResponse>> getLimitedFeed(@PathVariable("pageNumber") Integer pageNumber){




        var posts = postService.getPostsInRange(pageNumber * 20, (pageNumber * 20) + 20);

        return ResponseEntity.ok().body(posts);
    }

}

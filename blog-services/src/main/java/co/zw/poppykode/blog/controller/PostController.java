package co.zw.poppykode.blog.controller;

import co.zw.poppykode.blog.entity.Post;
import co.zw.poppykode.blog.payload.PostDto;
import co.zw.poppykode.blog.payload.PostResponse;
import co.zw.poppykode.blog.service.PostService;
import co.zw.poppykode.blog.utils.AppConstants;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/posts")
public class PostController {
    private PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PostDto> createPost(@Valid @RequestBody PostDto postDto){
        return  new ResponseEntity<>(postService.createPost(postDto), HttpStatus.CREATED);
    }
    @GetMapping
    public PostResponse getAllPosts(
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize",defaultValue = AppConstants.DEFAULT_PAGE_SIZE,required = false) int pageSize,
            @RequestParam(value = "sortBy",defaultValue = AppConstants.DEFAULT_SORT_BY,required = false) String sortBy,
            @RequestParam(value = "sortDir",defaultValue = AppConstants.DEFAULT_SORT_DIRECTION,required = false) String sortDir


    ){
        return postService.getAllPosts(pageNo,pageSize,sortBy,sortDir);
    }
    @GetMapping("/{id}")
    public ResponseEntity<PostDto> getPostById(@PathVariable("id") Long id){
        return ResponseEntity.ok().body(postService.getPostById(id));
    }
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PostDto> updatePostById(@RequestBody PostDto postDto,@PathVariable("id") Long id){
        PostDto updatedPost = postService.updatePostById(postDto, id);
        return ResponseEntity.ok().body(updatedPost);
    }
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deletePostById(@PathVariable("id") Long id){
        postService.deletePostById(id);
        return ResponseEntity.ok().body("Post successfully deleted.");
    }
    @GetMapping("/category/{id}")
    public ResponseEntity<List<PostDto>> getPostsByCategory(@PathVariable("id") Long categoryId){
        return ResponseEntity.ok(postService.getPostsByCategory(categoryId));
    }
}

package co.zw.poppykode.blog.service;

import co.zw.poppykode.blog.entity.Post;
import co.zw.poppykode.blog.payload.PostDto;
import co.zw.poppykode.blog.payload.PostResponse;

import java.util.List;

public interface PostService {
    PostDto createPost(PostDto postDto);
    PostResponse getAllPosts(int pageNo, int pageSize, String sortBy,String sortDir);

    PostDto getPostById(Long id);

    PostDto updatePostById(PostDto postDto, Long id);
    void deletePostById(Long id);

    Post getPost(Long id);

    List<PostDto> getPostsByCategory(Long categoryId);
}

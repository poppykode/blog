package co.zw.poppykode.blog.service.impl;

import co.zw.poppykode.blog.entity.Category;
import co.zw.poppykode.blog.entity.Post;
import co.zw.poppykode.blog.exception.ResourceNotFoundException;
import co.zw.poppykode.blog.payload.CategoryDto;
import co.zw.poppykode.blog.payload.PostDto;
import co.zw.poppykode.blog.payload.PostResponse;
import co.zw.poppykode.blog.repository.PostRepository;
import co.zw.poppykode.blog.service.CategoryService;
import co.zw.poppykode.blog.service.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final ModelMapper mapper;

    private final CategoryService categoryService;

    public PostServiceImpl(PostRepository postRepository, ModelMapper mapper, CategoryService categoryService) {
        this.mapper =mapper;
        this.postRepository = postRepository;
        this.categoryService = categoryService;
    }

    @Override
    public PostDto createPost(PostDto postDto) {
        Category category = mapper.map(categoryService.getCategory(postDto.getCategoryId()),Category.class);
        Post post = mapToEntity(postDto);
        post.setCategory(category);
        Post newPost = postRepository.save(post);
        return mapToDto(newPost);
    }

    private PostDto mapToDto(Post post){
//        PostDto response = new PostDto();
//        response.setId(post.getId());
//        response.setTitle(post.getTitle());
//        response.setDescription(post.getDescription());
//        response.setContent(post.getContent());
        return mapper.map(post,PostDto.class);
    }
    private Post mapToEntity(PostDto postDto){
//        Post response = new Post();
//        response.setId(postDto.getId());
//        response.setTitle(postDto.getTitle());
//        response.setDescription(postDto.getDescription());
//        response.setContent(postDto.getContent());
        return  mapper.map(postDto,Post.class);
    }

    @Override
    public PostResponse getAllPosts(int pageNo,int pageSize,String sortBy,String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending():Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNo,pageSize,sort);
        Page<Post> allPosts = postRepository.findAll(pageable);
//        List<Post> pageableList = allPosts.getContent();
        List<PostDto> content = allPosts.getContent().stream().map(this::mapToDto).collect(Collectors.toList());
        PostResponse postResponse = new PostResponse();
        postResponse.setContent(content);
        postResponse.setPageNo(allPosts.getNumber());
        postResponse.setPageSize(allPosts.getSize());
        postResponse.setTotalElements(allPosts.getTotalElements());
        postResponse.setTotalPages(allPosts.getTotalPages());
        postResponse.setLast(allPosts.isLast());

        return postResponse;
    }

    @Override
    public PostDto getPostById(Long id) {
        Post post = getPost(id);
        return mapToDto(post);
    }

    @Override
    public PostDto updatePostById(PostDto postDto, Long id) {
        Category category = mapper.map(categoryService.getCategory(postDto.getCategoryId()),Category.class);
        Post post = getPost(id);
        post.setTitle(postDto.getTitle());
        post.setDescription(postDto.getDescription());
        post.setContent(postDto.getContent());
        post.setCategory(category);
        Post updatedPost = postRepository.save(post);
        return mapToDto(updatedPost);
    }

    @Override
    public void deletePostById(Long id) {
        Post post = getPost(id);
        postRepository.delete(post);

    }

    @Override
    public Post getPost(Long id) {
        return postRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Post","id", id));
    }

    @Override
    public List<PostDto> getPostsByCategory(Long categoryId) {
        List<Post> posts = postRepository.findByCategoryId(categoryId);
        return posts.stream().map(this::mapToDto).collect(Collectors.toList());
    }
}

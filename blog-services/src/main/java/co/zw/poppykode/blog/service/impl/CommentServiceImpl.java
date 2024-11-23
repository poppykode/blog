package co.zw.poppykode.blog.service.impl;

import co.zw.poppykode.blog.entity.Comment;
import co.zw.poppykode.blog.entity.Post;
import co.zw.poppykode.blog.exception.BlogAPIException;
import co.zw.poppykode.blog.exception.ResourceNotFoundException;
import co.zw.poppykode.blog.payload.CommentDto;
import co.zw.poppykode.blog.repository.CommentRepository;
import co.zw.poppykode.blog.service.CommentService;
import co.zw.poppykode.blog.service.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final ModelMapper mapper;
    private final PostService postService;

    public CommentServiceImpl(CommentRepository commentRepository, ModelMapper mapper, PostService postService) {
        this.commentRepository = commentRepository;
        this.mapper = mapper;
        this.postService = postService;
    }


    @Override
    public CommentDto createComment(long postId, CommentDto commentDto) {
         Post post = postService.getPost(postId);
         Comment comment = mapToEntity(commentDto);
         comment.setPost(post);
         Comment newComment = commentRepository.save(comment);
        return mapToDto(newComment);
    }

    @Override
    public List<CommentDto> getCommentsByPostId(long postId) {
         List<Comment> comments = commentRepository.findByPostId(postId);
        return comments.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public CommentDto getCommentById(Long postId, Long commentId) {
        Comment comment = getComment(postId, commentId);
        return mapToDto(comment);
    }

    @Override
    public CommentDto updateComment(Long postId, long commentId, CommentDto commentDto) {
        Comment comment = getComment(postId, commentId);
        comment.setName(commentDto.getName());
        comment.setEmail(commentDto.getEmail());
        comment.setBody(commentDto.getBody());
        return mapToDto(commentRepository.save(comment));
    }

    @Override
    public void deleteComment(Long postId, Long commentId) {
        Comment comment = getComment(postId, commentId);
        commentRepository.delete(comment);
    }

    private Comment mapToEntity(CommentDto commentDto){
        Comment comment = mapper.map(commentDto,Comment.class);
        return comment;
    }
    private CommentDto mapToDto(Comment comment){
        CommentDto commentDto = mapper.map(comment,CommentDto.class);
        return commentDto;
    }
    private Comment getComment(Long postId, long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(()-> new ResourceNotFoundException("Comment","id", commentId));
        Post post = postService.getPost(postId);
        if(!post.getId().equals(comment.getPost().getId())){
            throw  new BlogAPIException(HttpStatus.BAD_REQUEST,"Comment does not belong to post");
        }
        return comment;
    }

}

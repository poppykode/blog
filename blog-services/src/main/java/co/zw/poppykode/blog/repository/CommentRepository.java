package co.zw.poppykode.blog.repository;

import co.zw.poppykode.blog.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Long> {
    public List<Comment>  findByPostId(Long postId);
}

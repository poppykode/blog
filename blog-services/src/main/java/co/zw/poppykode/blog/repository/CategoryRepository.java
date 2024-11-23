package co.zw.poppykode.blog.repository;

import co.zw.poppykode.blog.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category,Long> {
}

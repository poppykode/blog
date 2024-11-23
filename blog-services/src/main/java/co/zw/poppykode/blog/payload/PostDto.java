package co.zw.poppykode.blog.payload;

import co.zw.poppykode.blog.entity.Category;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PostDto {
    private Long id;
    @NotEmpty
    @Size(min = 2, message = "you need atleast 2 characters")
    private String title;
    @NotEmpty
    @Size(min = 10, message = "you need atleast 19 characters")
    private String description;
    private String content;
    private Long categoryId;
//    private String categoryName;
//    private String categoryDescription;

}

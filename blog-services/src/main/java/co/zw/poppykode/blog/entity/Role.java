package co.zw.poppykode.blog.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity

public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
}

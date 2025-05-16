package guestbook.demo.dto;

import guestbook.demo.entity.Post;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostDto {

    private Long id;
    private String name;
    private String content;
    private String imageUrl;
    private LocalDateTime createdAt;
}

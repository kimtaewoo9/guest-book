package guestbook.demo.dto;

import guestbook.demo.entity.Post;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostListResponseDto {

    private List<Post> content; // 방명록 객체 배열 ..
    private int totalPages;
    private Long totalElements;
    private int size;
    private int number;
}

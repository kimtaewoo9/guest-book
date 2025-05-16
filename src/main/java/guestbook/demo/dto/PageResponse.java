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
public class PageResponse {

    private List<Post> postList;
    private int totalPage;
    private int totalElements;
    private int size;
    private int number;
}

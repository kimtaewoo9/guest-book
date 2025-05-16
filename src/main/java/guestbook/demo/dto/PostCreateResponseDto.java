package guestbook.demo.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostCreateResponseDto {

    private Long id;
    private String name;
    private String content;
    private String filename;
    private LocalDateTime createdAt;
}

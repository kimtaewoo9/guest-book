package guestbook.demo.entity;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Table(name ="post")
@EntityListeners(AuditingEntityListener.class) // Auditing 리스너를 엔티티에 등록합니다.
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name; // 익명 가능 .

    @Column(nullable = false)
    private String content; // 필수

    private String imageUrl; // 데이터베이스에 url만 저장 .

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime createdAt;
}

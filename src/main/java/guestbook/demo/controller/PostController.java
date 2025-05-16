package guestbook.demo.controller;

import guestbook.demo.dto.PostCreateRequestDto;
import guestbook.demo.dto.PostCreateResponseDto;
import guestbook.demo.dto.PostDto;
import guestbook.demo.dto.PostListResponseDto;
import guestbook.demo.entity.Post;
import guestbook.demo.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("/api/guestbooks")
    public ResponseEntity<PostCreateResponseDto> createPost(PostCreateRequestDto requestDto){
        PostCreateResponseDto responseDto = postService.createPost(requestDto);

        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/api/guestbooks")
    public ResponseEntity<PostListResponseDto> getPostList(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "5") int size){
        Page<Post> postPage = postService.getPostList(page, size);

        PostListResponseDto responseDto = PostListResponseDto.builder()
            .content(postPage.getContent())
            .totalPages(postPage.getTotalPages())
            .totalElements(postPage.getTotalElements())
            .size(postPage.getSize()) // 현재 페이지의 크기.
            .number(postPage.getNumber()) // 현재 페이지 번호.
            .build();
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/api/guestbooks/{id}")
    public ResponseEntity<PostDto> getPost(@PathVariable Long id){

        PostDto postDto = postService.getPost(id);
        return ResponseEntity.ok(postDto);
    }
}

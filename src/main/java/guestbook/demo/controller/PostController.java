package guestbook.demo.controller;

import guestbook.demo.dto.PostCreateRequestDto;
import guestbook.demo.dto.PostCreateResponseDto;
import guestbook.demo.dto.PageResponse;
import guestbook.demo.service.PostService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
    public ResponseEntity<List<PageResponse>> getPostList(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "5") int size){
        List<PageResponse> postList = postService.getPostList(page, size);

        return ResponseEntity.ok(postList);
    }
}

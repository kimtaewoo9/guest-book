package guestbook.demo.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import guestbook.demo.dto.PostCreateRequestDto;
import guestbook.demo.dto.PostCreateResponseDto;
import guestbook.demo.dto.PostDto;
import guestbook.demo.entity.Post;
import guestbook.demo.repository.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final AmazonS3 amazonS3;
    private final PostRepository postRepository;
    
    public PostCreateResponseDto createPost(PostCreateRequestDto requestDto){

        String imageUrl = uploadFile(requestDto.getImage());

        Post post = Post.builder()
            .name(requestDto.getName())
            .content(requestDto.getContent())
            .imageUrl(imageUrl)
            .build();

        Post savedPost = postRepository.save(post);

        return PostCreateResponseDto.builder()
            .id(savedPost.getId())
            .name(savedPost.getName())
            .content(savedPost.getContent())
            .filename(savedPost.getImageUrl())
            .createdAt(savedPost.getCreatedAt())
            .build();
    }

    public String uploadFile(MultipartFile multipartFile) {
        if(multipartFile == null || multipartFile.isEmpty()){
            return null;
        }

        String originalFilename = multipartFile.getOriginalFilename();
        String uniqueFileName = createUniqueFileName(originalFilename);
        // 업로드할 객체에 대한 메타 데이터 ..
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(multipartFile.getSize());
        objectMetadata.setContentType(multipartFile.getContentType());

        try {
            amazonS3.putObject(bucket, uniqueFileName, multipartFile.getInputStream(), objectMetadata);
        } catch (IOException e) {
            throw new RuntimeException();
        }

        return amazonS3.getUrl(bucket, uniqueFileName).toString();
    }

    public String createUniqueFileName(String originalFileName) {
        String extension = getFileExtension(originalFileName);
        return UUID.randomUUID().toString() + extension;
    }

    public String getFileExtension(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "파일 이름이 유효하지 않습니다.");
        }
        try {
            return fileName.substring(fileName.lastIndexOf("."));
        } catch (StringIndexOutOfBoundsException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "잘못된 형식의 파일(" + fileName + ") 입니다. 확장자를 포함해야 합니다.");
        }
    }

    @Transactional(readOnly = true)
    public Page<Post> getPostList(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        return postRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public PostDto getPost(Long id) {
        Post post = postRepository.findById(id).orElseThrow(
            () -> new EntityNotFoundException("post not found")
        );
        return PostDto.builder()
            .id(post.getId())
            .name(post.getName())
            .content(post.getContent())
            .imageUrl(post.getImageUrl())
            .createdAt(post.getCreatedAt())
            .build();
    }
}

package guestbook.demo.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import guestbook.demo.dto.PostCreateRequestDto;
import guestbook.demo.dto.PostCreateResponseDto;
import guestbook.demo.dto.PageResponse;
import guestbook.demo.entity.Post;
import guestbook.demo.repository.PostRepository;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class PostService {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final AmazonS3 amazonS3;
    private final PostRepository postRepository;
    
    public PostCreateResponseDto createPost(PostCreateRequestDto requestDto){

        String filename = uploadFile(requestDto.getImage());

        Post post = Post.builder()
            .name(requestDto.getName())
            .content(requestDto.getContent())
            .filename(filename)
            .build();

        Post savedPost = postRepository.save(post);

        return PostCreateResponseDto.builder()
            .id(savedPost.getId())
            .name(savedPost.getName())
            .content(savedPost.getContent())
            .filename(savedPost.getFilename())
            .createdAt(savedPost.getCreateAt())
            .build();
    }

    public String uploadFile(MultipartFile multipartFile){
        if(multipartFile == null || multipartFile.isEmpty()){
            return null;
        }

        String filename = createFileName(multipartFile.getOriginalFilename());

        // 업로드할 객체에 대한 메타 데이터 ..
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(multipartFile.getSize());
        objectMetadata.setContentType(multipartFile.getContentType());

        try(InputStream inputStream = multipartFile.getInputStream()){
            // 버킷 이름, 키(파일 이름), 데이터(업로드할 파일의 실제 데이터 스트림), 객체에 대한 메타데이터
            // s3 는 .. input stream으로부터 데이터를 청킹함 .
            amazonS3.putObject(new PutObjectRequest(bucket, filename, inputStream, objectMetadata)
                .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (IOException e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "파일 업로드에 실패했습니다.");
        }

        return filename; // 데이터 버킷에 저장하고, filename 만 반환
    }

    public String createFileName(String fileName){
        return UUID.randomUUID().toString().concat(getFileExtension(fileName));
    }

    public String getFileExtension(String fileName){
        try {
            return fileName.substring(fileName.lastIndexOf("."));
        } catch (StringIndexOutOfBoundsException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "잘못된 형식의 파일" + fileName + ") 입니다.");
        }
    }

    public List<PageResponse> getPostList(int page, int size) {


    }
}

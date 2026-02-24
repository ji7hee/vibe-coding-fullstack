package com.example.vibeapp.post;

import com.example.vibeapp.post.dto.PostCreateDto;
import com.example.vibeapp.post.dto.PostListDto;
import com.example.vibeapp.post.dto.PostResponseDto;
import com.example.vibeapp.post.dto.PostUpdateDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    /** 게시글 목록 조회: GET /api/posts?page=1 */
    @GetMapping
    public ResponseEntity<Map<String, Object>> list(
            @RequestParam(defaultValue = "1") int page) {
        int pageSize = 5;
        List<PostListDto> posts = postService.findAll(page, pageSize);
        int totalPages = postService.getTotalPages(pageSize);

        return ResponseEntity.ok(Map.of(
                "posts", posts,
                "currentPage", page,
                "totalPages", totalPages
        ));
    }

    /** 게시글 상세 조회: GET /api/posts/{no} */
    @GetMapping("/{no}")
    public ResponseEntity<PostResponseDto> detail(@PathVariable Long no) {
        PostResponseDto post = postService.findById(no);
        return ResponseEntity.ok(post);
    }

    /** 게시글 등록: POST /api/posts */
    @PostMapping
    public ResponseEntity<PostResponseDto> add(@Valid @RequestBody PostCreateDto postCreateDto) {
        PostResponseDto created = postService.create(postCreateDto);
        return ResponseEntity.status(201).body(created);
    }

    /** 게시글 수정: PATCH /api/posts/{no} */
    @PatchMapping("/{no}")
    public ResponseEntity<PostResponseDto> save(
            @PathVariable Long no,
            @Valid @RequestBody PostUpdateDto postUpdateDto) {
        PostResponseDto updated = postService.update(no, postUpdateDto);
        return ResponseEntity.ok(updated);
    }

    /** 게시글 삭제: DELETE /api/posts/{no} */
    @DeleteMapping("/{no}")
    public ResponseEntity<Void> delete(@PathVariable Long no) {
        postService.delete(no);
        return ResponseEntity.noContent().build();
    }
}

package com.example.vibeapp.post;

import com.example.vibeapp.post.dto.PostCreateDto;
import com.example.vibeapp.post.dto.PostListDto;
import com.example.vibeapp.post.dto.PostResponseDto;
import com.example.vibeapp.post.dto.PostUpdateDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final PostTagRepository postTagRepository;

    public PostService(PostRepository postRepository, PostTagRepository postTagRepository) {
        this.postRepository = postRepository;
        this.postTagRepository = postTagRepository;
    }

    public List<PostListDto> findAll(int page, int size) {
        List<Post> allPosts = postRepository.findAll();

        int fromIndex = (page - 1) * size;
        if (fromIndex >= allPosts.size()) {
            return List.of();
        }

        int toIndex = Math.min(fromIndex + size, allPosts.size());
        return allPosts.subList(fromIndex, toIndex).stream()
                .map(PostListDto::from)
                .collect(Collectors.toList());
    }

    public int getTotalPages(int size) {
        int totalPosts = postRepository.count();
        return (int) Math.ceil((double) totalPosts / size);
    }

    @Transactional
    public PostResponseDto findById(Long no) {
        Post post = postRepository.findById(no)
                .orElseThrow(() -> new NoSuchElementException("게시글을 찾을 수 없습니다: " + no));
        postRepository.updateViews(no);
        post.setViews(post.getViews() + 1);
        return PostResponseDto.from(post, getTagsAsString(no));
    }

    public PostResponseDto findByIdWithoutViewCount(Long no) {
        Post post = postRepository.findById(no)
                .orElseThrow(() -> new NoSuchElementException("게시글을 찾을 수 없습니다: " + no));
        return PostResponseDto.from(post, getTagsAsString(no));
    }

    private String getTagsAsString(Long postNo) {
        List<PostTag> tags = postTagRepository.findByPostNo(postNo);
        return tags.stream()
                .map(PostTag::getTagName)
                .collect(Collectors.joining(", "));
    }

    /**
     * 게시글 등록: 게시글 INSERT + 태그 INSERT를 하나의 트랜잭션으로 처리
     */
    @Transactional
    public PostResponseDto create(PostCreateDto createDto) {
        Post post = createDto.toEntity();
        postRepository.insert(post);
        saveTags(post.getNo(), createDto.tags());
        return PostResponseDto.from(post, createDto.tags() != null ? createDto.tags().trim() : "");
    }

    /**
     * 게시글 수정: 게시글 UPDATE + 태그 DELETE/INSERT를 하나의 트랜잭션으로 처리
     * 변경 감지(Dirty Checking)에 의해 트랜잭션 커밋 시 자동 UPDATE
     */
    @Transactional
    public PostResponseDto update(Long no, PostUpdateDto updateDto) {
        Post post = postRepository.findById(no)
                .orElseThrow(() -> new NoSuchElementException("게시글을 찾을 수 없습니다: " + no));
        updateDto.updateEntity(post);
        postTagRepository.deleteByPostNo(no);
        saveTags(no, updateDto.tags());
        return PostResponseDto.from(post, getTagsAsString(no));
    }

    private void saveTags(Long postNo, String tagsString) {
        if (tagsString == null || tagsString.isBlank()) {
            return;
        }

        String[] tags = tagsString.split(",");
        for (String tagName : tags) {
            String trimmedTag = tagName.trim();
            if (!trimmedTag.isEmpty()) {
                postTagRepository.save(new PostTag(null, postNo, trimmedTag));
            }
        }
    }

    @Transactional
    public void delete(Long no) {
        postRepository.findById(no)
                .orElseThrow(() -> new NoSuchElementException("게시글을 찾을 수 없습니다: " + no));
        postRepository.delete(no);
    }
}

package com.example.vibeapp.post;

import com.example.vibeapp.post.dto.PostCreateDto;
import com.example.vibeapp.post.dto.PostListDto;
import com.example.vibeapp.post.dto.PostResponseDto;
import com.example.vibeapp.post.dto.PostUpdateDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
        return postRepository.findById(no)
                .map(post -> {
                    postRepository.updateViews(no);
                    post.setViews(post.getViews() + 1);
                    return PostResponseDto.from(post, getTagsAsString(no));
                })
                .orElse(null);
    }

    public PostResponseDto findByIdWithoutViewCount(Long no) {
        return postRepository.findById(no)
                .map(post -> PostResponseDto.from(post, getTagsAsString(no)))
                .orElse(null);
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
    public void create(PostCreateDto createDto) {
        Post post = createDto.toEntity();
        // persist() 호출 즉시 INSERT되어 post.getNo()에 생성된 PK가 채워짐 (IDENTITY 전략)
        postRepository.insert(post);
        saveTags(post.getNo(), createDto.tags());
    }

    /**
     * 게시글 수정: 게시글 UPDATE + 태그 DELETE/INSERT를 하나의 트랜잭션으로 처리
     * findById()로 가져온 엔티티는 영속 상태이므로, 필드 변경만으로
     * 트랜잭션 커밋 시 변경 감지(Dirty Checking)에 의해 자동 UPDATE됨
     */
    @Transactional
    public void update(Long no, PostUpdateDto updateDto) {
        postRepository.findById(no).ifPresent(post -> {
            updateDto.updateEntity(post); // 변경 감지: 트랜잭션 커밋 시 자동 UPDATE
            postTagRepository.deleteByPostNo(no);
            saveTags(no, updateDto.tags());
        });
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
        postRepository.delete(no);
    }
}

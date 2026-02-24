package com.example.vibeapp.post;

import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface PostMapper {
    List<Post> findAll();
    Post findById(Long no);
    void insert(Post post);
    void update(Post post);
    void delete(Long no);
    int count();
    void updateViews(Long no);
}

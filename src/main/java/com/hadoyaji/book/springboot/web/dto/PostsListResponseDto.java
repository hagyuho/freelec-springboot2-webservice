package com.hadoyaji.book.springboot.web.dto;

import com.hadoyaji.book.springboot.domain.posts.Posts;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostsListResponseDto {

    private long id;
    private String title;
    private String author;
    private LocalDateTime modifiedDate;

    public PostsListResponseDto(Posts entity) {
        this.id=entity.getId();
        this.author=entity.getAuthor();
        this.title=entity.getTitle();
        this.modifiedDate = entity.getModifiedDate();
    }
}

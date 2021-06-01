package com.hadoyaji.book.springboot.service.posts;

import com.hadoyaji.book.springboot.web.dto.PostsSaveRequestDto;
import com.hadoyaji.book.springboot.domain.posts.Posts;
import com.hadoyaji.book.springboot.domain.posts.PostsRepository;
import com.hadoyaji.book.springboot.web.dto.PostResponseDto;
import com.hadoyaji.book.springboot.web.dto.PostsUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@Service
public class PostsService {

    final PostsRepository postsRepository;

    @Transactional
    public Long save(PostsSaveRequestDto requestDto) {
        return postsRepository.save(requestDto.toEntity()).getId();
    }

    @Transactional
    public Long update(Long id, PostsUpdateRequestDto requestDto) {
        Posts posts = postsRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("해당 게시글이 없습니다. id="+id));
        posts.update(requestDto.getTitle(),requestDto.getContent());
        return id;
    }

    public PostResponseDto findById(Long id) {
        Posts entity = postsRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("해당 게시글이 없습니다. id="+id));
        return new PostResponseDto(entity);
    }
}

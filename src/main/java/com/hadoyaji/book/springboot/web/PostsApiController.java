package com.hadoyaji.book.springboot.web;

import com.hadoyaji.book.springboot.web.dto.PostResponseDto;
import com.hadoyaji.book.springboot.web.dto.PostsSaveRequestDto;
import com.hadoyaji.book.springboot.web.dto.PostsUpdateRequestDto;
import com.hadoyaji.book.springboot.service.posts.PostsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class PostsApiController {

    final PostsService postsService;

    @PostMapping("api/v1/posts")
    public Long save(@RequestBody PostsSaveRequestDto requestDto){
        return postsService.save(requestDto);
    }

    @PutMapping("api/v1/posts/{id}")
    public  Long update(@PathVariable Long id, @RequestBody PostsUpdateRequestDto requestDto){
        return postsService.update(id, requestDto);
    }

    @GetMapping("api/v1/posts/{id}")
    public PostResponseDto findById(@PathVariable Long id){
        return postsService.findById(id);
    }

}

package com.hadoyaji.bookspringboot.web;

import com.hadoyaji.bookspringboot.service.posts.PostsService;
import com.hadoyaji.bookspringboot.web.dto.PostResponseDto;
import com.hadoyaji.bookspringboot.web.dto.PostsSaveRequestDto;
import com.hadoyaji.bookspringboot.web.dto.PostsUpdateRequestDto;
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

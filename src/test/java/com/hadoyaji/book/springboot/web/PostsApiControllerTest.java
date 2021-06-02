package com.hadoyaji.book.springboot.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hadoyaji.book.springboot.domain.posts.Posts;
import com.hadoyaji.book.springboot.domain.posts.PostsRepository;
import com.hadoyaji.book.springboot.web.dto.PostsSaveRequestDto;
import com.hadoyaji.book.springboot.web.dto.PostsUpdateRequestDto;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PostsApiControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PostsRepository postsRepository;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @Before
    public void setup(){
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @After
    public void tearDown() throws Exception {
        postsRepository.deleteAll();
    }

    @Test
    @WithMockUser(roles="USER")
    public void Posts_등록된다() throws Exception {
        //given
        String title = "title";
        String content = "content";
        PostsSaveRequestDto requestDto = PostsSaveRequestDto.builder()
                .title(title)
                .content(content)
                .author("hadoyaji")
                .build();

        String url = "http://localhost:" + port + "/api/v1/posts";

        //when
        ResponseEntity<Long> responseEntity = restTemplate.postForEntity(url, requestDto, Long.class);
        mvc.perform(post(url)
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content(new ObjectMapper().writeValueAsString(requestDto)))
                    .andExpect(status().isOk());

        //then
        List<Posts> all = postsRepository.findAll();
        assertThat(all.get(0).getTitle()).isEqualTo(title);
        assertThat(all.get(0).getContent()).isEqualTo(content);
    }

    @Test
    @WithMockUser(roles="USER")
    public void Posts_수정된다() throws Exception {
        //given
        Posts savePosts = postsRepository.save(Posts.builder()
                .title("title")
                .content("content")
                .author("hadoyaji")
                .build());

        Long updateId = savePosts.getId();
        String expectedTitle = "title2";
        String expectedContent = "content2";

        PostsUpdateRequestDto requestDto = PostsUpdateRequestDto.builder()
                .title(expectedTitle)
                .content(expectedContent)
                .build();

        String url = "http://localhost:" + port + "/api/v1/posts/" + updateId;
        HttpEntity<PostsUpdateRequestDto> requestEntity = new HttpEntity<>(requestDto);

        //when
        mvc.perform(put(url)
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content(new ObjectMapper().writeValueAsString(requestDto)))
                    .andExpect(status().isOk());

        //then
        List<Posts> all = postsRepository.findAll();
        assertThat(all.get(0).getTitle()).isEqualTo(expectedTitle);
        assertThat(all.get(0).getContent()).isEqualTo(expectedContent);

    }

    @Test
    @WithMockUser(roles="USER")
    public void Posts_삭제된다() throws Exception{
        //given
        Posts savePosts = postsRepository.save(Posts.builder()
                .title("title")
                .content("content")
                .author("hadoyaji")
                .build());

        Long deleteId = savePosts.getId();
        String url = "http://localhost:" + port + "/api/v1/posts/" + deleteId;

        //when
        mvc.perform(delete(url)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(new ObjectMapper().writeValueAsString(deleteId)))
                .andExpect(status().isOk());

        //then
        List<Posts> all = postsRepository.findAll();
        assertThat(all.size()).isEqualTo(0);

    }

    @Test
    public void Post_전체조회() throws Exception {
        //given
        Posts savePosts = postsRepository.save(Posts.builder()
                .author("author")
                .title("title")
                .content("content")
                .build());

        Long id = savePosts.getId();

        String expectedTitle = "title";
        String expectedContent = "content";
        String url = "http://localhost:" + port + "/api/v1/posts/" + id;

        //when
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        List<Posts> all = postsRepository.findAll();
        assertThat(all.get(0).getTitle()).isEqualTo(expectedTitle);
        assertThat(all.get(0).getContent()).isEqualTo(expectedContent);


    }
}

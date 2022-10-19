package com.example.team6.controller;

import com.example.team6.controller.request.PostRequestDto;
import com.example.team6.controller.response.ResponseDto;
import com.example.team6.service.PostService;
import javax.servlet.http.HttpServletRequest;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

//@RequiredArgsConstructor // 추가 작업을 필요로 하는 필드에 대한 생성자를 생성

//@Controller에 @ResponseBody가 추가된 것 // 주용도는 Json 형태로 객체 데이터를 반환
//REST API를 개발할 때 주로 사용하며 객체를 ResponseEntity로 감싸서 반환
@RestController
public class PostController {

  private final PostService postService;

  /////@RequiredArgsConstructor 추가 부분
  public PostController(PostService postService) {
    this.postService = postService;
  }
  ///////////

  @ApiImplicitParams({
          @ApiImplicitParam(
                  name = "Refresh-Token",
                  required = true,
                  dataType = "string",
                  paramType = "header"
          )
  })

  @PostMapping(value = "/api/auth/post")
  public ResponseDto<?> createPost(@RequestBody PostRequestDto requestDto,
                                   HttpServletRequest request) {
    return postService.createPost(requestDto, request);
  }

  @GetMapping(value = "/api/post/{id}")
  public ResponseDto<?> getPost(@PathVariable Long id) {
    return postService.getPost(id);
  }

  @GetMapping(value = "/api/post")
  public ResponseDto<?> getAllPosts() {
    return postService.getAllPost();
  }

  @PutMapping(value = "/api/auth/post/{id}")
  public ResponseDto<?> updatePost(@PathVariable Long id, @RequestBody PostRequestDto postRequestDto,
                                   HttpServletRequest request) {
    return postService.updatePosts(id, postRequestDto, request);
  }

  @DeleteMapping(value = "/api/auth/post/{id}")
  public ResponseDto<?> deletePost(@PathVariable Long id,
                                   HttpServletRequest request) {
    return postService.deletePost(id, request);
  }

}

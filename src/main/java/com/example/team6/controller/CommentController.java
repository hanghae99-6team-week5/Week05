package com.example.team6.controller;

import com.example.team6.controller.response.ResponseDto;
import com.example.team6.controller.request.CommentRequestDto;
import com.example.team6.service.CommentService;
import javax.servlet.http.HttpServletRequest;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
//@RequiredArgsConstructor
@RestController
public class CommentController {
  private final CommentService commentService;

  public CommentController(CommentService commentService) {
    this.commentService = commentService;
  }

  @ApiImplicitParams({
          @ApiImplicitParam(
                  name = "Refresh-Token",
                  required = true,
                  dataType = "string",
                  paramType = "header"
          )
  })

  //comment 생성 API
  @PostMapping(value = "/api/auth/comment")
  public ResponseDto<?> createComment(@RequestBody CommentRequestDto requestDto,
      HttpServletRequest request) {
    return commentService.createComment(requestDto, request);
  }

  //comment 조회 API
  @GetMapping(value = "/api/comment/{id}")
  public ResponseDto<?> getAllComments(@PathVariable Long id) {
    return commentService.getAllCommentsByPost(id);
  }

  //comment 수정 API
  @PutMapping(value = "/api/auth/comment/{id}")
  public ResponseDto<?> updateComment(@PathVariable Long id, @RequestBody CommentRequestDto requestDto,
      HttpServletRequest request) {
    return commentService.updateComment(id, requestDto, request);
  }

  //comment 삭제 API
  @DeleteMapping(value = "/api/auth/comment/{id}")
  public ResponseDto<?> deleteComment(@PathVariable Long id,
      HttpServletRequest request) {
    return commentService.deleteComment(id, request);
  }
}

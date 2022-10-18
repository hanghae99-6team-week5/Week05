package com.example.intermediate.controller.response;

import java.time.LocalDateTime;
import java.util.List;

import com.example.intermediate.domain.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

//@Builder
@Getter
//@NoArgsConstructor
//@AllArgsConstructor
public class PostResponseDto {
  private Long id;
  private String title;
  private String content;
  private String author;
  private List<CommentResponseDto> commentResponseDtoList;

  //좋아요 부분
  private Long heartNum;

  private LocalDateTime createdAt;
  private LocalDateTime modifiedAt;

  public PostResponseDto(Long id, String title, String content, String author,
                         LocalDateTime createdAt, LocalDateTime modifiedAt) {
    this.id = id;
    this.title = title;
    this.content = content;
    this.author = author;
    this.createdAt = createdAt;
    this.modifiedAt = modifiedAt;

  }

  public PostResponseDto(Long id, String title, String content, String author,
                         List<CommentResponseDto> commentResponseDtoList, LocalDateTime createdAt, LocalDateTime modifiedAt) {
    this.id = id;
    this.title = title;
    this.content = content;
    this.author = author;
    this.commentResponseDtoList = commentResponseDtoList;
    this.createdAt = createdAt;
    this.modifiedAt = modifiedAt;
  }

  public PostResponseDto(Long id, String title, String content, List<CommentResponseDto> commentResponseDtoList, String author,
                         LocalDateTime createdAt, LocalDateTime modifiedAt) {
    this.id = id;
    this.title = title;
    this.content = content;
    this.commentResponseDtoList = commentResponseDtoList;
    this.author = author;
    this.createdAt = createdAt;
    this.modifiedAt = modifiedAt;

  }

  public PostResponseDto(Long id, String title, String author, String content, Long heartNum,
                         LocalDateTime createdAt, LocalDateTime modifiedAt) {
    this.id = id;
    this.title = title;
    this.author = author;
    this.content = content;
    this.heartNum = heartNum;
    this.createdAt = createdAt;
    this.modifiedAt = modifiedAt;
  }
}
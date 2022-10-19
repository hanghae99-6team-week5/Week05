package com.example.intermediate.controller.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

//@Getter
//@NoArgsConstructor
//@AllArgsConstructor
public class PostRequestDto {
  private String title; //final을 왜 붙여줘야하지?
  private String content;

  /////////////추가부분(파라미터 없는 생성자 생성)

  public PostRequestDto(String title, String content){
    this.title = title;
    this.content = content;
  }
/////////////////////////////////////////////

  public String getTitle() {
    return title;
  }

  public String getContent() {
    return content;
  }
}

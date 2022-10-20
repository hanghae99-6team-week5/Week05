package com.example.team6.controller.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

//@Getter
//@NoArgsConstructor
//@AllArgsConstructor
public class PostRequestDto {
  private String title;
  private String content;

  /////////////추가부분(생성자 생성)

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

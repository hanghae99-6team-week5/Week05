package com.example.team6.controller.response;

import com.example.team6.domain.Error;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
//@AllArgsConstructor
public class ResponseDto<T> {
  private boolean success;
  private T data;
  private Error error;


  public ResponseDto(boolean success, T data, Error error) {
    this.success = success;
    this.data = data;
    this.error = error;
  }

  public static <T> ResponseDto<T> success(T data) {
    return new ResponseDto<>(true, data, null);
  }

  public static <T> ResponseDto<T> fail(String code, String message) {
    return new ResponseDto<>(false, null, new Error(code, message));
  }

}

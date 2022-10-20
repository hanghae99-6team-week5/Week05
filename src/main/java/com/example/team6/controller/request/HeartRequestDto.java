package com.example.team6.controller.request;

//@Getter
//@NoArgsConstructor
//@AllArgsConstructor
public class HeartRequestDto {
    private Long id;

    //@AllArgsConstructor
    public HeartRequestDto(Long id) {
        this.id = id;
    }
    //@Getter 대체 부분 추가
    public Long getId() {
        return id;
    }
    //@NoArgsConstructor
    public HeartRequestDto() {
    }
}



package com.example.team6.controller;

import com.example.team6.controller.request.HeartRequestDto;
import com.example.team6.controller.response.ResponseDto;
import com.example.team6.service.PostHeartService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Validated
@RequiredArgsConstructor
@RestController
public class HeartController {

    private final PostHeartService postHeartService;
    //private final PostCommentHeartService postCommentHeartService;


    @ApiImplicitParams({
            // 스웨거에서 할당해야하는 값들을 알려주는 Description
            @ApiImplicitParam(
                    name = "Refresh-Token",
                    required = true,
                    dataType = "string",
                    paramType = "header"
            )
    })

    @RequestMapping(value = "/api/auth/post/heart/{id}", method = RequestMethod.POST)
    public ResponseDto<?> createPostHeart(@RequestBody HeartRequestDto requestDto,
                                          HttpServletRequest request) {
        return postHeartService.createPostHeart(requestDto, request);
    }
//    @RequestMapping(value = "/api/auth/postComment/heart", method = RequestMethod.POST)
//    public ResponseDto<?> createPostCommentHeart(@RequestBody HeartRequestDto requestDto,
//                                                 HttpServletRequest request) {
//        return postCommentHeartService.createPostCommentHeart(requestDto, request);
//    }

}

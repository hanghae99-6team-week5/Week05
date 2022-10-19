package com.example.intermediate.controller;

import com.example.intermediate.controller.request.HeartRequestDto;
import com.example.intermediate.controller.response.ResponseDto;
import com.example.intermediate.service.PostHeartService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    @RequestMapping(value = "/api/auth/post/heart", method = RequestMethod.POST)
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
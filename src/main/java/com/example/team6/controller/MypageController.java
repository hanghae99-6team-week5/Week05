package com.example.team6.controller;


import com.example.team6.controller.response.ResponseDto;
import com.example.team6.repository.CommentRepository;
import com.example.team6.service.MypageService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Validated
@RestController
//@AllArgsConstructor
public class MypageController {

    private final MypageService mypageService;
    private final CommentRepository commentRepository;

    //@AllArgsConstructor
    public MypageController(MypageService mypageService, CommentRepository commentRepository) {
        this.mypageService = mypageService;
        this.commentRepository = commentRepository;
    }
    @ApiImplicitParams({
            // 스웨거에서 할당해야하는 값들을 알려주는 Description
            @ApiImplicitParam(
                    name = "Refresh-Token",
                    required = true,
                    dataType = "string",
                    paramType = "header"
            )
    })
//    @ResponseStatus(value = HttpStatus.OK)
//    @GetMapping({"/api/mypage/comment"})
//    private List<Comment> getMyComment(@AuthenticationPrincipal UserDetailsImpl userDetails) {
//       // @AuthenticationPrincipal 애노테이션을 사용하면
//        // UserDetailsService에서 Return한 객체 를 파라메터로 직접 받아 사용할 수 있다.
//        if (userDetails != null) {
//            String username = userDetails.getMember().getNickname();
//            return this.MypageService.getMypageComment(username);
//        }
//        else{
//            return null;
//        }
//    }

//    @ResponseStatus(value = HttpStatus.OK)
//    @GetMapping({"/api/mypage/post"})
//    public List<Post> getMyPost(@AuthenticationPrincipal UserDetailsImpl userDetails) {
//        if(userDetails != null){
//            Long userId = userDetails.getMember().getId();
//            return this.MypageService.getMypagePost(userId);
//        }
//        else{
//            return null;
//        }
//    }

    @RequestMapping(value = "/api/auth/mypage/post", method = RequestMethod.GET)
    public ResponseDto<?> getMyPagePost(HttpServletRequest request) {
        return mypageService.getMyPagePost(request);
    }

    @ApiImplicitParams({
            // 스웨거에서 할당해야하는 값들을 알려주는 Description
            @ApiImplicitParam(
                    name = "Refresh-Token",
                    required = true,
                    dataType = "string",
                    paramType = "header"
            )
    })
    @RequestMapping(value = "/api/auth/mypage/comment", method = RequestMethod.GET)
    public ResponseDto<?> getMyPageComment(HttpServletRequest request) {
        return mypageService.getMyPageComment(request);
    }

    @RequestMapping(value = "/api/auth/mypage/heartPost", method = RequestMethod.GET)
    public ResponseDto<?> getMyPageHeartPost(HttpServletRequest request) {
        return mypageService.getMyPageHeartPost(request);
    }


}

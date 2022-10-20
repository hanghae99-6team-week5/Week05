package com.example.team6.controller;

import com.example.team6.controller.response.ResponseDto;
import com.example.team6.service.MyPageService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Validated
//@RequiredArgsConstructor
@RestController
public class MyPageController {

    private final MyPageService myPageService;

    //@Autowired
    public MyPageController(MyPageService myPageService) {
        this.myPageService = myPageService;
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

    @RequestMapping(value="/api/auth/mypage", method = RequestMethod.GET)
//    public ResponseDto<?> getMyPage(@AuthenticationPrincipal UserDetailsImpl userDetails) {
//        return myPageService.getMyPage(userDetails);
//    }
    public ResponseDto<?> getMyPage(HttpServletRequest request) {
        return myPageService.getMyPage(request);
    }
}


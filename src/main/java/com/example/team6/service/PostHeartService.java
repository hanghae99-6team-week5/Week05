package com.example.team6.service;

import com.example.team6.controller.request.HeartRequestDto;
import com.example.team6.controller.response.ResponseDto;
import com.example.team6.domain.Member;
import com.example.team6.domain.Post;
import com.example.team6.domain.PostHeart;
import com.example.team6.jwt.TokenProvider;
import com.example.team6.repository.PostHeartRepository;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

@Service
//@RequiredArgsConstructor
public class PostHeartService {

    private  final PostService postService;
    //private  final PostRepository postRepository;
    private final PostHeartRepository postHeartRepository;

    private final TokenProvider tokenProvider;

    ////@RequiredArgsConstructor 대체할 생성자 추가
    public PostHeartService(PostService postService, PostHeartRepository postHeartRepository, TokenProvider tokenProvider) {
        this.postService = postService;
        this.postHeartRepository = postHeartRepository;
        this.tokenProvider = tokenProvider;
    }
    ////////////////////////////////

    @Transactional
    public ResponseDto<?> createPostHeart(HeartRequestDto requestDto, HttpServletRequest request) {

        //로그인하지 않은 경우의 예외 HttpServletRequest / request의 header에 Refresh-Token , Authorization 값을 할당해서 주시는지 확인해주세요
        if (null == request.getHeader("Refresh-Token")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        if (null == request.getHeader("Authorization")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        Member member = validateMember(request);
        if (null == member) {
            return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
        }

        Post post = postService.isPresentPost(requestDto.getId());
        if (null == post) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 게시글 id 입니다.");
        }

        PostHeart findPostHeart = postHeartRepository.findByPostIdAndMemberId(post.getId(), member.getId());
        if ( null != findPostHeart ) {
            postHeartRepository.delete(findPostHeart);
            return  ResponseDto.success("좋아요 취소");
        }

        //builder 수정
//      PostHeart postheart =  PostHeart.builder()
//        .member(member)
//                .post(post)
//                .build();
//        postHeartRepository.save(postheart);

        //PostHeart에 member, post 타입에 맞는 생성자 추가
        PostHeart postheart = new PostHeart(member, post);
        postHeartRepository.save(postheart);
        return ResponseDto.success("게시글 좋아요 완료");
        ///////////

    }
    @Transactional
    public Member validateMember(HttpServletRequest request) {
        if (!tokenProvider.validateToken(request.getHeader("Refresh-Token"))) {
            return null;
        }
        return tokenProvider.getMemberFromAuthentication();
    }

}

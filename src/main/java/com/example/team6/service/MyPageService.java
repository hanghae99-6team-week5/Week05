package com.example.team6.service;

import com.example.team6.controller.response.MypageResponseDto;
import com.example.team6.controller.response.PostResponseDto;
import com.example.team6.controller.response.ResponseDto;
import com.example.team6.domain.Member;
import com.example.team6.domain.Post;
import com.example.team6.domain.PostHeart;
import com.example.team6.jwt.TokenProvider;
import com.example.team6.repository.PostHeartRepository;
import com.example.team6.repository.PostRepository;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

//@RequiredArgsConstructor
@Service
public class MyPageService {
    private final PostRepository postRepository;
    private final PostHeartRepository postHeartRepository;
    //private final CommentRepository commentRepository;
    //private final PostCommentHeartRepository postCommentHeartRepository;
    private final TokenProvider tokenProvider;

    ///////@RequiredArgsConstructor 추가부분
    public MyPageService(PostRepository postRepository, PostHeartRepository postHeartRepository, TokenProvider tokenProvider) {
        this.postRepository = postRepository;
        this.postHeartRepository = postHeartRepository;
        this.tokenProvider = tokenProvider;
    }
    //////////////////////////
    @Transactional
//    public ResponseDto<?> getMyPage(UserDetailsImpl userDetails) {
    public ResponseDto<?> getMyPage(HttpServletRequest request) {
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

        // 사용자가 작성한 글
        List<Post> myPostList = postRepository.findAllByMember(member);
        //List<Post> myPostList = postRepository.findAllByMember(userDetails.getMember());
        List<PostResponseDto> myPostResponseDtoList = new ArrayList<>();

        for (Post post : myPostList) {
            PostResponseDto postResponseDto = new PostResponseDto(post.getId(), post.getTitle(), post.getMember().getNickname(), post.getContent(),
                    postHeartRepository.countAllByPostId(post.getId()), post.getCreatedAt(), post.getModifiedAt());
            myPostResponseDtoList.add(postResponseDto);
//                    PostResponseDto.builder()
//                            .id(post.getId())
//                            .title(post.getTitle())
//                            .author(post.getMember().getNickname())
//                            .content(post.getContent())
//                            .heartNum(postHeartRepository.countAllByPostId(post.getId()))
//                            .createdAt(post.getCreatedAt())
//                            .modifiedAt(post.getModifiedAt())
//                            //.comment_cnt(post.getComment_cnt())
//                            .build()
//            );
        }

//        // 사용자가 작성한 댓글, 대댓글
//        List<Comment> myCommentList = commentRepository.findAllByMember(member);
//        List<CommentResponseDto> myCommentResponseDtoList = new ArrayList<>();
//
//        for (Comment comment : myCommentList) {
//            myCommentResponseDtoList.add(
//                    CommentResponseDto.builder()
//                            .id(comment.getId())
//                            .author(comment.getMember().getNickname())
//                            .content(comment.getContent())
//                            .heartNum(postCommentHeartRepository.countAllByCommentId(comment.getId()))
//                            .createdAt(comment.getCreatedAt())
//                            .modifiedAt(comment.getModifiedAt())
//                            .build()
//            );
//        }

        // 내가 좋아요한 게시글
        List<PostHeart> postHeartList = postHeartRepository.findAllByMember(member);
        List<PostResponseDto> heartedPostResponseDtoList = new ArrayList<>();

        for (PostHeart postHeart : postHeartList) {
            PostResponseDto postResponseDto = new PostResponseDto(postHeart.getPost().getId(), postHeart.getPost().getTitle(), postHeart.getPost().getMember().getNickname(),
                    postHeart.getPost().getContent(), postHeartRepository.countAllByPostId(postHeart.getPost().getId()),
                    postHeart.getPost().getCreatedAt(), postHeart.getPost().getModifiedAt());
            heartedPostResponseDtoList.add(postResponseDto);
//                    PostResponseDto.builder()
//                            .id(postHeart.getPost().getId())
//                            .title(postHeart.getPost().getTitle())
//                            .author(postHeart.getPost().getMember().getNickname())
//                            .content(postHeart.getPost().getContent())
//                            .heartNum(postHeartRepository.countAllByPostId(postHeart.getPost().getId()))
//                            .createdAt(postHeart.getPost().getCreatedAt())
//                            .modifiedAt(postHeart.getPost().getModifiedAt())
//                            //.comment_cnt(postHeart.getPost().getComment_cnt())
//                            .build()
//            );
        }

//        // 내가 좋아요한 댓글
//        List<PostCommentHeart> postCommentHeartList = postCommentHeartRepository.findAllByMember(member);
//        List<CommentResponseDto> heartedCommentResponseDtoList = new ArrayList<>();
//
//        for (PostCommentHeart postCommentHeart : postCommentHeartList) {
//            heartedCommentResponseDtoList.add(
//                    CommentResponseDto.builder()
//                            .id(postCommentHeart.getComment().getId())
//                            .author(postCommentHeart.getComment().getMember().getNickname())
//                            .content(postCommentHeart.getComment().getContent())
//                            .heartNum(postCommentHeartRepository.countAllByCommentId(postCommentHeart.getComment().getId()))
//                            .createdAt(postCommentHeart.getComment().getCreatedAt())
//                            .modifiedAt(postCommentHeart.getComment().getModifiedAt())
//                            .build()
//            );
//        }
        ///////builder 수정 (생성자 및 파라미터 추가)
        MypageResponseDto mypageResponseDto = new MypageResponseDto(myPostResponseDtoList, heartedPostResponseDtoList);
        return ResponseDto.success(mypageResponseDto);
//                MypageResponseDto.builder()
//                        .myPosts(myPostResponseDtoList)
//                        //.myComments(myCommentResponseDtoList) //댓글 좋아요기능 생략
//                        .heartedPosts(heartedPostResponseDtoList)
//                        //.heartedComments(heartedCommentResponseDtoList) //댓글 좋아요기능 생략
//                        .build()
//        );
    }
    @Transactional
    public Member validateMember(HttpServletRequest request) {
        if (!tokenProvider.validateToken(request.getHeader("Refresh-Token"))) {
            return null;
        }
        return tokenProvider.getMemberFromAuthentication();
    }
}

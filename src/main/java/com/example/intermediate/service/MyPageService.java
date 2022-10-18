package com.example.intermediate.service;

import com.example.intermediate.controller.response.CommentResponseDto;
import com.example.intermediate.controller.response.MypageResponseDto;
import com.example.intermediate.controller.response.PostResponseDto;
import com.example.intermediate.controller.response.ResponseDto;
import com.example.intermediate.domain.Member;
import com.example.intermediate.domain.Post;
import com.example.intermediate.domain.PostHeart;
import com.example.intermediate.jwt.TokenProvider;
import com.example.intermediate.repository.CommentRepository;
import com.example.intermediate.repository.PostHeartRepository;
import com.example.intermediate.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class MyPageService {
    private final PostRepository postRepository;
    private final PostHeartRepository postHeartRepository;
    private final CommentRepository commentRepository;
    //private final PostCommentHeartRepository postCommentHeartRepository;

    private final TokenProvider tokenProvider;

    @Transactional
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

        return ResponseDto.success(
                MypageResponseDto.builder()
                        .myPosts(myPostResponseDtoList)
                        //.myComments(myCommentResponseDtoList)
                        .heartedPosts(heartedPostResponseDtoList)
                        //.heartedComments(heartedCommentResponseDtoList)
                        .build()
        );
    }
    @Transactional
    public Member validateMember(HttpServletRequest request) {
        if (!tokenProvider.validateToken(request.getHeader("Refresh-Token"))) {
            return null;
        }
        return tokenProvider.getMemberFromAuthentication();
    }
}

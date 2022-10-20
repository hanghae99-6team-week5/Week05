package com.example.team6.service;


import com.example.team6.controller.response.CommentResponseDto;
import com.example.team6.controller.response.PostResponseDto;
import com.example.team6.controller.response.ResponseDto;
import com.example.team6.domain.Comment;
import com.example.team6.domain.Member;
import com.example.team6.domain.Post;
import com.example.team6.jwt.TokenProvider;
import com.example.team6.repository.CommentRepository;
import com.example.team6.repository.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Service
//@AllArgsConstructor
public class MypageService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    private final TokenProvider tokenProvider;

    //@AllArgsConstructor
    public MypageService(CommentRepository commentRepository, PostRepository postRepository, TokenProvider tokenProvider) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.tokenProvider = tokenProvider;
    }

    //    public List<Comment> getMypageComment(Member member) {
//        return this.commentRepository.findAllByUsernameOrderByCreatedAtDesc(member);
//    }
//
//    public List<Post> getMypagePost(Long userId) {
//        return this.postRepository.findAllByuserIdOrderByCreatedAtDesc(userId);
//    }
    @Transactional
    public ResponseDto<?> getMyPagePost(HttpServletRequest request) {
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
            PostResponseDto postResponseDto = new PostResponseDto(
                    post.getId(),
                    post.getTitle(),
                    post.getMember().getNickname(),
                    postHeartRepository.countAllByPostId(post.getId()),
                    post.getCreatedAt(),
                    post.getModifiedAt());
        }
        return ResponseDto.success(myPostResponseDtoList);
    }


//    for (Post post : myPostList) {
//        myPostResponseDtoList.add(
//                PostResponseDto.builder()
//                        .id(post.getId())
//                        .title(post.getTitle())
//                        .author(post.getMember().getNickname())
//                        .content(post.getContent())
//                        .heartNum(postHeartRepository.countAllByPostId(post.getId()))
//                        .createdAt(post.getCreatedAt())
//                        .modifiedAt(post.getModifiedAt())
//                        .build()
//        );
//    }
        @Transactional
        public ResponseDto<?> getMyPageComment(HttpServletRequest request){
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

            // 사용자가 작성한 댓글, 대댓글
            List<Comment> myCommentList = commentRepository.findAllByMember(member);
            List<CommentResponseDto> myCommentResponseDtoList = new ArrayList<>();
            for (Comment comment : myCommentList) {
                CommentResponseDto commentResponseDto = new CommentResponseDto(
                        comment.getId(),
                        comment.getMember().getNickname(),
                        comment.getContent(),
                        comment.getCreatedAt(),
                        comment.getModifiedAt());
                myCommentResponseDtoList.add(commentResponseDto);
            }
            return ResponseDto.success(myCommentResponseDtoList);
        }
//    for (Comment comment : myCommentList) {
//        myCommentResponseDtoList.add(
//                CommentResponseDto.builder()
//                        .id(comment.getId())
//                        .author(comment.getMember().getNickname())
//                        .content(comment.getContent())
//                        .createdAt(comment.getCreatedAt())
//                        .modifiedAt(comment.getModifiedAt())
//                        .build()
//        );
//    }

// 내가 좋아요한 게시글
        @Transactional
        public ResponseDto<?> getMyPageHeartPost(HttpServletRequest request) {
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

        List<PostHeart> postHeartList = postHeartRepository.findAllByMember(member);
        List<PostResponseDto> heartedPostResponseDtoList = new ArrayList<>(); //여기서 heartedPostResponseDtoList 생성해서 아래에서 리턴만 해주면 끝

        for (PostHeart postHeart : postHeartList) {
            PostResponseDto postResponseDto = new PostResponseDto(
                    postHeart.getPost().getId(),
                    postHeart.getPost().getTitle(),
                    postHeart.getPost().getMember().getNickname(),
                    postHeart.getPost().getContent(),
                    postHeartRepository.countAllByPostId(postHeart.getPost().getId()),
                    postHeart.getPost().getCreatedAt(),
                    postHeart.getPost().getModifiedAt());
            heartedPostResponseDtoList.add(postResponseDto);
        }

        return ResponseDto.success(heartedPostResponseDtoList);
    }
//        MypageResponseDto mypageResponseDto = new MypageResponseDto(
//                myPostResponseDtoList,
//                myCommentResponseDtoList,
//                heartedPostResponseDtoList);
//
//        return ResponseDto.success(mypageResponseDto);
//    }
//                MypageResponseDto.builder()
//                        .myPosts(myPostResponseDtoList)
//                        .myComments(myCommentResponseDtoList)
//                        .heartedPosts(heartedPostResponseDtoList)
//                        .build()
//        );

//    // 내가 좋아요한 게시글
//    List<PostHeart> postHeartList = postHeartRepository.findAllByMember(member);
//    List<PostResponseDto> heartedPostResponseDtoList = new ArrayList<>();
//
//    for (PostHeart postHeart : postHeartList) {
//        heartedPostResponseDtoList.add(
//                PostResponseDto.builder()
//                        .id(postHeart.getPost().getId())
//                        .title(postHeart.getPost().getTitle())
//                        .author(postHeart.getPost().getMember().getNickname())
//                        .content(postHeart.getPost().getContent())
//                        .heartNum(postHeartRepository.countAllByPostId(postHeart.getPost().getId()))
//                        .createdAt(postHeart.getPost().getCreatedAt())
//                        .modifiedAt(postHeart.getPost().getModifiedAt())
//                        .comment_cnt(postHeart.getPost().getComment_cnt())
//                        .build()
//        );
//    }

//     내가 좋아요한 댓글
//    List<PostCommentHeart> postCommentHeartList = postCommentHeartRepository.findAllByMember(member);
//    List<CommentResponseDto> heartedCommentResponseDtoList = new ArrayList<>();
//
//    for (PostCommentHeart postCommentHeart : postCommentHeartList) {
//        heartedCommentResponseDtoList.add(
//                CommentResponseDto.builder()
//                        .id(postCommentHeart.getComment().getId())
//                        .author(postCommentHeart.getComment().getMember().getNickname())
//                        .content(postCommentHeart.getComment().getContent())
//                        .heartNum(postCommentHeartRepository.countAllByCommentId(postCommentHeart.getComment().getId()))
//                        .createdAt(postCommentHeart.getComment().getCreatedAt())
//                        .modifiedAt(postCommentHeart.getComment().getModifiedAt())
//                        .build()
//        );
//    }


//        }
        @Transactional
        public Member validateMember (HttpServletRequest request){
            if (!tokenProvider.validateToken(request.getHeader("Refresh-Token"))) {
                return null;
            }
            return tokenProvider.getMemberFromAuthentication();
        }

    }
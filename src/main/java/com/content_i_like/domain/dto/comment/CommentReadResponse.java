package com.content_i_like.domain.dto.comment;

import com.content_i_like.domain.entity.Comment;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Builder
public class CommentReadResponse {

    private String memberNickname;
    private String profileImgUrl;
    private String commentContent;
    private Long commentPoint;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;

    public static CommentReadResponse of(Comment comment) {
        return CommentReadResponse.builder()
                .memberNickname(comment.getMember().getNickName())
                .profileImgUrl(comment.getMember().getProfileImgUrl())
                .commentContent(comment.getCommentContent())
                .commentPoint(comment.getCommentPoint())
                .createdAt(comment.getCreatedAt())
                .build();
    }


}

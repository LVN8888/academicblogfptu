package com.academicblogfptu.AcademicBlogFPTU.dtos;

import com.academicblogfptu.AcademicBlogFPTU.entities.CommentEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentDto {
    private int commentId;

    private String accountName;

    private String avatarURL;

    private String content;

    private Boolean isEdited;

    private int numOfUpvote;

    private int numOfDownvote;

    private String dateOfComment;

    private int postId;

    private Integer parent_id;

    private List<CommentDto> replyComments;
}
package com.academicblogfptu.AcademicBlogFPTU.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReportedCommentDto {
    private Integer reportId;

    private String reportDate;

    private String reportType;

    private Integer reportedCommentId;

    private String content;

    private String accountName;

    private String reasonOfReport;
}

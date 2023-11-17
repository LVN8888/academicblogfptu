package com.academicblogfptu.AcademicBlogFPTU.repositories;

import com.academicblogfptu.AcademicBlogFPTU.entities.PostEntity;
import com.academicblogfptu.AcademicBlogFPTU.entities.PostRewardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRewardRepository extends JpaRepository<PostRewardEntity, Integer> {
    @Query(value = "SELECT COUNT(*) FROM post_reward where post_id = :postId ", nativeQuery = true)
    int countNumOfReward(int postId);

    List<PostRewardEntity> findByPost(PostEntity postEntity);
}

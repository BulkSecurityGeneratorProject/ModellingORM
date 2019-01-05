package com.tomaszekem.modelling.repository;

import com.tomaszekem.modelling.domain.PostComment;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the PostComment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PostCommentRepository extends JpaRepository<PostComment, Long> {

    @Query("select post_comment from PostComment post_comment where post_comment.author.login = ?#{principal.username}")
    List<PostComment> findByAuthorIsCurrentUser();

}

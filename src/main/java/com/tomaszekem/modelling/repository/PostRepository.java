package com.tomaszekem.modelling.repository;

import com.tomaszekem.modelling.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Post entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("select post from Post post where post.author.login = ?#{principal.username}")
    List<Post> findByAuthorIsCurrentUser();

    @Query(value = "select distinct post from Post post left join fetch post.likedByUsers",
        countQuery = "select count(distinct post) from Post post")
    Page<Post> findAllWithEagerRelationships(Pageable pageable);

    @Query(value = "select distinct post from Post post left join fetch post.likedByUsers")
    List<Post> findAllWithEagerRelationships();

    @Query("select post from Post post left join fetch post.likedByUsers where post.id =:id")
    Optional<Post> findOneWithEagerRelationships(@Param("id") Long id);

    @Query(value = "select u.login, count(pu.liked_by_users_id) from jhi_user u  \n" +
        "join post p on p.author_id = u.id\n" +
        "join post_liked_by_users pu on pu.posts_id = p.id\n" +
        "where p.category like 'science'\n" +
        "group by u.id", nativeQuery = true)
    Object bla();

}

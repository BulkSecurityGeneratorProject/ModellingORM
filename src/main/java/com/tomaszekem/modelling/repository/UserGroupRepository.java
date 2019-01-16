package com.tomaszekem.modelling.repository;

import com.tomaszekem.modelling.domain.UserGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the UserGroup entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserGroupRepository extends JpaRepository<UserGroup, Long> {

    @Query(value = "select distinct user_group from UserGroup user_group left join fetch user_group.members",
        countQuery = "select count(distinct user_group) from UserGroup user_group")
    Page<UserGroup> findAllWithEagerRelationships(Pageable pageable);

    @Query(value = "select distinct user_group from UserGroup user_group left join fetch user_group.members")
    List<UserGroup> findAllWithEagerRelationships();

    @Query("select user_group from UserGroup user_group left join fetch user_group.members where user_group.id =:id")
    Optional<UserGroup> findOneWithEagerRelationships(@Param("id") Long id);

    @Query("select user_group from UserGroup user_group where user_group.name like ?1%")
    Page<UserGroup> findAllByNameLike(String name, Pageable pageable);

    @Query("select user_group from UserGroup user_group where user_group.name like ?1%")
    List<UserGroup> findAllByNameLike(String name);


}

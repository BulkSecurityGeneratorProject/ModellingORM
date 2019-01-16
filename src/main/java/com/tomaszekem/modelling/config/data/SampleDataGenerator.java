package com.tomaszekem.modelling.config.data;

import com.google.common.collect.Lists;
import com.tomaszekem.modelling.domain.Post;
import com.tomaszekem.modelling.domain.PostComment;
import com.tomaszekem.modelling.domain.User;
import com.tomaszekem.modelling.domain.UserGroup;
import com.tomaszekem.modelling.domain.enumeration.Category;
import com.tomaszekem.modelling.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class SampleDataGenerator {

    private static final Logger log = LoggerFactory.getLogger(SampleDataGenerator.class);

    private static final int GENERATED_RECORDS_SIZE = 100000;
    private static final int NUMBER_OF_COMMENTS = 100;

    private final PostRepository postRepository;
    private final UserGroupRepository userGroupRepository;
    private final UserRepository userRepository;
    private final PostCommentRepository postCommentRepository;
    private final UserFilesDataGenerator userFilesDataGenerator;
    private final EntityManager entityManager;

    //todo refactor this
    public SampleDataGenerator(PostRepository postRepository, UserRepository userRepository, UserGroupRepository userGroupRepository, FileRepository fileRepository, PostCommentRepository postCommentRepository, UserFilesDataGenerator userFilesDataGenerator, EntityManager entityManager) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.userGroupRepository = userGroupRepository;
        this.postCommentRepository = postCommentRepository;
        this.userFilesDataGenerator = userFilesDataGenerator;
        this.entityManager = entityManager;
    }

    @Transactional(rollbackFor = Exception.class)
    public void insertData() {
        log.debug("Inserting data");
        for (int i = 0; i < 3; i++) {
            insertAllData();
        }
        log.debug("Inserted data");
    }

    private void insertAllData() {
        List<User> users = userRepository.findAll();
        createGroups(users);
        try {
            userFilesDataGenerator.createUsersFiles(users);
            log.debug("Inserted files");
        } catch (IOException e) {
            log.error("Error during inserting files of users");
        }
    }

    private void createGroups(List<User> groupMembers) {
        List<UserGroup> groups = Lists.newArrayList();

        List<Post> posts = createPostsOfUser(groupMembers.get(0), groupMembers);
        addCommentsToPosts(posts, groupMembers);

        for (int i = 0; i < GENERATED_RECORDS_SIZE; i++) {
            UserGroup newGroup = new UserGroup("Test Group", Category.SCIENCE);
            newGroup.addMembers(groupMembers);
            groups.add(newGroup);
        }
        userGroupRepository.saveAll(groups);
        for (UserGroup group : groups) {
            for (Post post : posts) {
                post.setGroup(group);
            }
        }
        postRepository.saveAll(posts);

    }

    private void addCommentsToPosts(List<Post> posts, List<User> users) {
        User commentAuthor = users.get(0);
        List<PostComment> comments = Lists.newArrayList();
        for (Post post : posts) {
//            post.addComments(prepareCommentsForPost(commentAuthor,post));
            comments.addAll(prepareCommentsForPost(commentAuthor, post));
        }
        postCommentRepository.saveAll(comments);
        log.debug("Inserted post comments");
    }

    private List<Post> createPostsOfUser(User user, List<User> likedByUsers) {
        List<Post> posts = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            Post newPost = new Post("Title" + i, "Content" + i, Category.SCIENCE);
            newPost.setAuthor(user);
            posts.add(newPost);
            newPost.addLikedByUsers(likedByUsers);
        }
        return postRepository.saveAll(posts);
    }

    private List<PostComment> prepareCommentsForPost(User author, Post post) {
        List<PostComment> comments = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            PostComment comment = new PostComment("Content" + i, author, post);
            comments.add(comment);
        }
        return comments;
    }


}

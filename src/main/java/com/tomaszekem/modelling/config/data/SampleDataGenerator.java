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

import java.util.ArrayList;
import java.util.List;

@Component
public class SampleDataGenerator {

    private static final Logger log = LoggerFactory.getLogger(SampleDataGenerator.class);

    private static final int GENERATED_RECORDS_SIZE = 10000;
    private static final int NUMBER_OF_COMMENTS = 100;

    private final PostRepository postRepository;
    private final UserGroupRepository userGroupRepository;
    private final FileRepository fileRepository;
    private final UserRepository userRepository;
    private final PostCommentRepository postCommentRepository;

    //todo refactor this
    public SampleDataGenerator(PostRepository postRepository, UserRepository userRepository, UserGroupRepository userGroupRepository, FileRepository fileRepository, PostCommentRepository postCommentRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.userGroupRepository = userGroupRepository;
        this.fileRepository = fileRepository;
        this.postCommentRepository = postCommentRepository;
    }

    //todo insert users files
    @Transactional(rollbackFor = Exception.class)
    public void insertData() {
        log.debug("Inserting data");
        List<User> users = userRepository.findAll();
        createGroups(users);
        log.debug("Inserted data");
    }

    private void createGroups(List<User> groupMembers) {
        List<UserGroup> groups = Lists.newArrayList();

        List<Post> posts = createPostsOfUser(groupMembers.get(0), groupMembers);
        addCommentsAndLikesToPosts(posts, groupMembers);

        for (int i = 0; i < GENERATED_RECORDS_SIZE; i++) {
            UserGroup newGroup = new UserGroup("Test Group", Category.SCIENCE);
            newGroup.addMembers(groupMembers);
            newGroup.addPosts(posts);
            groups.add(newGroup);
        }
        userGroupRepository.saveAll(groups);



    }

    private void addCommentsAndLikesToPosts(List<Post> posts, List<User> users) {
        User commentAuthor = users.get(0);
        for (Post post : posts) {
            post.addComments(prepareCommentsForPost(commentAuthor,post));
            post.addLikedByUsers(users);
        }
    }

    private List<Post> createPostsOfUser(User user, List<User> likedByUsers) {
        List<Post> posts = new ArrayList<>();
        for (int i = 0; i < GENERATED_RECORDS_SIZE; i++) {
            Post newPost = new Post("Title" + i, "Content" + i, Category.SCIENCE);
            newPost.setAuthor(user);
            posts.add(newPost);
            newPost.addLikedByUsers(likedByUsers);
        }

        return postRepository.saveAll(posts);
    }

    private List<PostComment> prepareCommentsForPost(User author, Post post) {
        List<PostComment> comments = new ArrayList<>();
        for (int i = 0; i < NUMBER_OF_COMMENTS; i++) {
            PostComment comment = new PostComment("Content" + i, author, post);
            comments.add(comment);
        }
        return postCommentRepository.saveAll(comments);
    }


}

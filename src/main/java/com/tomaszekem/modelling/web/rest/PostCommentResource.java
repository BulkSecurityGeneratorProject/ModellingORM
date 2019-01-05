package com.tomaszekem.modelling.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.tomaszekem.modelling.domain.PostComment;
import com.tomaszekem.modelling.repository.PostCommentRepository;
import com.tomaszekem.modelling.web.rest.errors.BadRequestAlertException;
import com.tomaszekem.modelling.web.rest.util.HeaderUtil;
import com.tomaszekem.modelling.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing PostComment.
 */
@RestController
@RequestMapping("/api")
public class PostCommentResource {

    private final Logger log = LoggerFactory.getLogger(PostCommentResource.class);

    private static final String ENTITY_NAME = "postComment";

    private final PostCommentRepository postCommentRepository;

    public PostCommentResource(PostCommentRepository postCommentRepository) {
        this.postCommentRepository = postCommentRepository;
    }

    /**
     * POST  /post-comments : Create a new postComment.
     *
     * @param postComment the postComment to create
     * @return the ResponseEntity with status 201 (Created) and with body the new postComment, or with status 400 (Bad Request) if the postComment has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/post-comments")
    @Timed
    public ResponseEntity<PostComment> createPostComment(@Valid @RequestBody PostComment postComment) throws URISyntaxException {
        log.debug("REST request to save PostComment : {}", postComment);
        if (postComment.getId() != null) {
            throw new BadRequestAlertException("A new postComment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PostComment result = postCommentRepository.save(postComment);
        return ResponseEntity.created(new URI("/api/post-comments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /post-comments : Updates an existing postComment.
     *
     * @param postComment the postComment to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated postComment,
     * or with status 400 (Bad Request) if the postComment is not valid,
     * or with status 500 (Internal Server Error) if the postComment couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/post-comments")
    @Timed
    public ResponseEntity<PostComment> updatePostComment(@Valid @RequestBody PostComment postComment) throws URISyntaxException {
        log.debug("REST request to update PostComment : {}", postComment);
        if (postComment.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PostComment result = postCommentRepository.save(postComment);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, postComment.getId().toString()))
            .body(result);
    }

    /**
     * GET  /post-comments : get all the postComments.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of postComments in body
     */
    @GetMapping("/post-comments")
    @Timed
    public ResponseEntity<List<PostComment>> getAllPostComments(Pageable pageable) {
        log.debug("REST request to get a page of PostComments");
        Page<PostComment> page = postCommentRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/post-comments");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /post-comments/:id : get the "id" postComment.
     *
     * @param id the id of the postComment to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the postComment, or with status 404 (Not Found)
     */
    @GetMapping("/post-comments/{id}")
    @Timed
    public ResponseEntity<PostComment> getPostComment(@PathVariable Long id) {
        log.debug("REST request to get PostComment : {}", id);
        Optional<PostComment> postComment = postCommentRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(postComment);
    }

    /**
     * DELETE  /post-comments/:id : delete the "id" postComment.
     *
     * @param id the id of the postComment to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/post-comments/{id}")
    @Timed
    public ResponseEntity<Void> deletePostComment(@PathVariable Long id) {
        log.debug("REST request to delete PostComment : {}", id);

        postCommentRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}

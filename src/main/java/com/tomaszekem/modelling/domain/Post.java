package com.tomaszekem.modelling.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.collect.Sets;
import com.tomaszekem.modelling.domain.enumeration.Category;
import com.tomaszekem.modelling.domain.enumeration.PostCategory;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


@Entity
@Table(name = "post")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Post implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @NotNull
    @Column(name = "content", nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(name = "category")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "author_id")
    @JsonIgnoreProperties("")
    private User author;

    @OneToMany(mappedBy = "post")
    private Set<PostComment> comments = Sets.newHashSet();

    @ManyToOne
    @JoinColumn(name = "group_id", nullable = false)
    private UserGroup group;

    public Post(@NotNull String title, @NotNull String content, Category category) {
        this.title = title;
        this.content = content;
        this.category = category;
    }

    public Post() {

    }

    public UserGroup getGroup() {
        return group;
    }

    public void setGroup(UserGroup group) {
        this.group = group;
    }

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "post_liked_by_users",
               joinColumns = @JoinColumn(name = "posts_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "liked_by_users_id", referencedColumnName = "id"))
    private Set<User> likedByUsers = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public Post title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public Post content(String content) {
        this.content = content;
        return this;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Category getCategory() {
        return category;
    }

    public Post category(Category category) {
        this.category = category;
        return this;
    }

    public Post author(User user) {
        this.author = user;
        return this;
    }

    public void setAuthor(User user) {
        this.author = user;
    }

    public void addLikedByUsers(Collection<User> users) {
        this.likedByUsers.addAll(users);
    }

    public void addComments(Collection<PostComment> comments) {
        this.comments.addAll(comments);
        comments.forEach(c -> c.setPost(this));
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Post post = (Post) o;
        if (post.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), post.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Post{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", content='" + getContent() + "'" +
            ", category='" + getCategory() + "'" +
            "}";
    }
}

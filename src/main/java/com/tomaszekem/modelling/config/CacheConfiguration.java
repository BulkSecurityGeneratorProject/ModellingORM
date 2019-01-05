package com.tomaszekem.modelling.config;

import java.time.Duration;

import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;

import io.github.jhipster.config.jcache.BeanClassLoaderAwareJCacheRegionFactory;
import io.github.jhipster.config.JHipsterProperties;

import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.*;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        BeanClassLoaderAwareJCacheRegionFactory.setBeanClassLoader(this.getClass().getClassLoader());
        JHipsterProperties.Cache.Ehcache ehcache =
            jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(Object.class, Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                .build());
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            cm.createCache(com.tomaszekem.modelling.repository.UserRepository.USERS_BY_LOGIN_CACHE, jcacheConfiguration);
            cm.createCache(com.tomaszekem.modelling.repository.UserRepository.USERS_BY_EMAIL_CACHE, jcacheConfiguration);
            cm.createCache(com.tomaszekem.modelling.domain.User.class.getName(), jcacheConfiguration);
            cm.createCache(com.tomaszekem.modelling.domain.Authority.class.getName(), jcacheConfiguration);
            cm.createCache(com.tomaszekem.modelling.domain.User.class.getName() + ".authorities", jcacheConfiguration);
            cm.createCache(com.tomaszekem.modelling.domain.Post.class.getName(), jcacheConfiguration);
            cm.createCache(com.tomaszekem.modelling.domain.Post.class.getName() + ".likedByUsers", jcacheConfiguration);
            cm.createCache(com.tomaszekem.modelling.domain.PostComment.class.getName(), jcacheConfiguration);
            cm.createCache(com.tomaszekem.modelling.domain.UserGroup.class.getName(), jcacheConfiguration);
            cm.createCache(com.tomaszekem.modelling.domain.UserGroup.class.getName() + ".members", jcacheConfiguration);
            cm.createCache(com.tomaszekem.modelling.domain.UserGroup.class.getName() + ".posts", jcacheConfiguration);
            cm.createCache(com.tomaszekem.modelling.domain.File.class.getName(), jcacheConfiguration);
            // jhipster-needle-ehcache-add-entry
        };
    }
}

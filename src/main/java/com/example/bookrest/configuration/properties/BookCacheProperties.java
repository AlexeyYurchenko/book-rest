package com.example.bookrest.configuration.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@ConfigurationProperties(prefix = "book.cache")
public class BookCacheProperties {

    private final List<String> cacheNames = new ArrayList<>();
    private final Map<String, CacheProperties> caches = new HashMap<>();
    private CacheType cacheType;

    @Data
    public static class CacheProperties {
        private Duration expiry = Duration.ZERO;
    }

    public interface CacheNames {
        String ENTITIES_FIND_ALL = "entitiesFindAll";
        String FIND_BY_CATEGORY_NAME= "findByCategoryName";
        String FIND_BY_BOOK_NAME = "findByBookName";
        String FIND_BY_BOOK_AUTHOR = "findByBookAuthor";
        String ENTITY_SAVE = "entitySave";
        String ENTITY_UPDATE = "entityUpdate";
    }

    public enum CacheType {
        IN_MEMORY, REDIS;
    }
}
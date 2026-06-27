package com.JIMS.integration.config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;

import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RateLimiterService {

    // In-memory storage for all rate limit buckets
    private final Map<String, Bucket> cache = new ConcurrentHashMap<>();

    // Create a new bucket with capacity + time window
    private Bucket createBucket(int capacity, int minutes) {
        Bandwidth limit = Bandwidth.simple(capacity, Duration.ofMinutes(minutes));

        return Bucket.builder()
                .addLimit(limit)
                .build();
    }

    // Check and consume 1 token from bucket
    public boolean tryConsume(String key, int capacity, int minutes) {

        // Get bucket if exists, else create new one
        Bucket bucket = cache.computeIfAbsent(
                key,
                k -> createBucket(capacity, minutes)
        );

        // Try to consume 1 token
        return bucket.tryConsume(1);
    }
}
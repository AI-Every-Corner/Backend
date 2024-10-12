package com.aieverywhere.backend.config;

import org.springframework.stereotype.Component;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class JwtBlackList {
    private final ConcurrentHashMap<String, Boolean> blacklist = new ConcurrentHashMap<>();

    public void addToBlacklist(String token) {
        blacklist.put(token, true);
    }

    public boolean isBlacklisted(String token) {
        return blacklist.containsKey(token);
    }
}

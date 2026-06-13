package com.pii.pii_back.service;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AuthSessionService {

    private final Map<String, Long> sessions = new ConcurrentHashMap<>();

    public String createSession(Long administrateurId) {
        String token = UUID.randomUUID().toString();
        sessions.put(token, administrateurId);
        return token;
    }

    public boolean isValidToken(String token) {
        return token != null && sessions.containsKey(token);
    }

    public void invalidateSession(String token) {
        if (token != null) {
            sessions.remove(token);
        }
    }
}

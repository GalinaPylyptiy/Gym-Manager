package com.epam.gym.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class AuthenticationFailureListener implements ApplicationListener<AuthenticationFailureBadCredentialsEvent> {

    private final Duration BLOCK_DURATION = Duration.ofMinutes(5);
    private static final int MAX_ATTEMPTS = 3;
    private static Logger LOG = LoggerFactory.getLogger(AuthenticationFailureListener.class);
    private Map<String, Long> blockedUsers = new ConcurrentHashMap<>();
    private Map<String, Integer> attemptsCount = new ConcurrentHashMap<>();


    @Override
    public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent event) {
        String username = extractUsername(event);
        handleFailedLoginAttempts(username);
    }

    private void handleFailedLoginAttempts(String username) {
        increaseFailedLogin(username);
        if (getAttemptsCount(username) >= MAX_ATTEMPTS) {
            blockUser(username);
            resetAttempts(username);
        }
    }

    private void increaseFailedLogin(String username) {
        int previousAttempts = attemptsCount.getOrDefault(username, 0);
        previousAttempts++;
        if (previousAttempts == 1) {
            attemptsCount.put(username, previousAttempts);
            return;
        }
        LOG.info("***INCREASE FAIL AUTH ATTEMPT FOR USER " + username + " TO " + previousAttempts);
        attemptsCount.replace(username, previousAttempts);
    }

    private void resetAttempts(String username) {
        attemptsCount.remove(username);
    }

    private String extractUsername(AuthenticationFailureBadCredentialsEvent event) {
        return (String) event.getAuthentication().getPrincipal();
    }

    private int getAttemptsCount(String username) {
        return attemptsCount.get(username);
    }

    private void blockUser(String username) {
        LOG.info("***PUT USER WITH USERNAME " + username + " TO BLOCK");
        blockedUsers.put(username, System.currentTimeMillis() + BLOCK_DURATION.toMillis());
    }

    public boolean isBlocked(String username) {
        unblockIfExpired(username);
        return blockedUsers.containsKey(username);
    }

    private void unblockIfExpired(String username) {
        if (blockedUsers.containsKey(username)) {
            Long unblockTime = blockedUsers.get(username);
            if (unblockTime <= System.currentTimeMillis())
                blockedUsers.remove(username);
        }
    }

    public long extractMinutesLeft(String username) {
        Long unblockTime = blockedUsers.get(username);
        return Duration.ofMillis(unblockTime - System.currentTimeMillis()).toMinutes();
    }
}

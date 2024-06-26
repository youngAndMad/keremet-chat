package danekerscode.keremetchat.service.impl;

import danekerscode.keremetchat.core.AppConstant;
import danekerscode.keremetchat.model.UserActivity;
import danekerscode.keremetchat.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserStatusServiceImpl implements UserStatusService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final Supplier<LocalDateTime> currentDateTime = LocalDateTime::now;

    @Override
    public UserActivity getUserActivity(Long userId) {
        return Optional.ofNullable(
                        this.redisTemplate.opsForHash().get(AppConstant.USER_ACTIVITY_REDIS_HASH.getValue(), userId)
                )
                .map(UserActivity.class::cast)
                .orElse(UserActivity.defaultUserActivityForUserId(userId));
    }

    @Override
    public void setOnlineStatus(Long userId) {
        this.clearUserActivity(userId);

        var userActivity = UserActivity
                .defaultUserActivityForUserId(userId)
                .withLastActive(currentDateTime.get())
                .withOnline(true);

        this.saveUserActivity(userActivity);

        log.info("User {} is online", userId);
    }

    @Override
    public void setOfflineStatus(Long userId) {
        this.clearUserActivity(userId);

        var userActivity = UserActivity
                .defaultUserActivityForUserId(userId)
                .withLastActive(currentDateTime.get());

        this.saveUserActivity(userActivity);

        log.info("User {} is offline", userId);
    }

    @Override
    public Long getOnlineUsersCount() {
        return this.redisTemplate.opsForHash()
                .size(AppConstant.USER_ACTIVITY_REDIS_HASH.getValue());
    }

    private void clearUserActivity(Long userId) {
        log.info("Clearing user activity for user id: {}", userId);
        this.redisTemplate.opsForHash()
                .delete(AppConstant.USER_ACTIVITY_REDIS_HASH.getValue(), userId);
    }

    private void saveUserActivity(UserActivity userActivity) {
        log.info("Saving user activity: {}", userActivity);
        this.redisTemplate.opsForHash()
                .put(AppConstant.USER_ACTIVITY_REDIS_HASH.getValue(), userActivity.getUserId(), userActivity);
    }
}

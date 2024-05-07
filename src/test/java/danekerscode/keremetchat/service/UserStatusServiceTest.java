package danekerscode.keremetchat.service;

import danekerscode.keremetchat.core.AppConstants;
import danekerscode.keremetchat.model.UserActivity;
import danekerscode.keremetchat.service.impl.UserStatusServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserStatusServiceTest {

    @Mock
    private RedisTemplate<String, Object> redisTemplate;
    @InjectMocks
    private UserStatusServiceImpl userStatusService;
    @Mock
    private HashOperations<String, Object, Object> hashOperations;

    @BeforeEach
    void setUp() {
        lenient().when(redisTemplate.opsForHash())
                .thenReturn(hashOperations);
    }

    @Test
    @DisplayName("getUserActivity: Non-existent user activity should return default activity")
    void getUserActivity_NonExistentUserActivity_ReturnsDefaultActivity() {
        var userId = 1L;

        when(redisTemplate.opsForHash().get(AppConstants.USER_ACTIVITY_REDIS_HASH.getValue(), userId)).
                thenReturn(null);

        var actualUserActivity = userStatusService.getUserActivity(userId);

        assertEquals(userId, actualUserActivity.getUserId());
        assertFalse(actualUserActivity.isOnline());
        assertNull(actualUserActivity.getLastActive());
    }

    @Test
    void setOnlineStatus_UserIsOnline() {
        var userId = 1L;

        userStatusService.setOnlineStatus(userId);

        when(this.hashOperations.get(eq(AppConstants.USER_ACTIVITY_REDIS_HASH.getValue()), any()))
                .thenReturn(UserActivity
                        .defaultUserActivityForUserId(userId)
                        .withLastActive(LocalDateTime.now())
                        .withOnline(true));

        var actualUserActivity = userStatusService.getUserActivity(userId);

        assertEquals(userId, actualUserActivity.getUserId());
        assertTrue(actualUserActivity.isOnline());
        assertNotNull(actualUserActivity.getLastActive());

        verify(hashOperations).get(eq(AppConstants.USER_ACTIVITY_REDIS_HASH.getValue()), eq(userId));
        verify(hashOperations).delete(eq(AppConstants.USER_ACTIVITY_REDIS_HASH.getValue()), eq(userId));
        verify(hashOperations).put(eq(AppConstants.USER_ACTIVITY_REDIS_HASH.getValue()), eq(userId),any());
    }

}
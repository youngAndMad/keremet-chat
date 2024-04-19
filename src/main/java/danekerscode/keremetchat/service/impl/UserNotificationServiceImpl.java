package danekerscode.keremetchat.service.impl;

import danekerscode.keremetchat.model.dto.request.websocket.DeliverNotificationRequest;
import danekerscode.keremetchat.model.entity.UserNotification;
import danekerscode.keremetchat.service.UserNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.Serializable;

@Service
@RequiredArgsConstructor
public class UserNotificationServiceImpl implements UserNotificationService {

    private final RedisTemplate<String,Object> redisTemplate;

    @Override
    public <Content extends Serializable> UserNotification save(
            DeliverNotificationRequest<Content> deliverNotificationRequest
    ) {
        return null;
    }
}

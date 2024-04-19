package danekerscode.keremetchat.service;

import danekerscode.keremetchat.model.dto.request.websocket.DeliverNotificationRequest;
import danekerscode.keremetchat.model.entity.UserNotification;

import java.io.Serializable;

public interface UserNotificationService {

    <Content extends Serializable> UserNotification save(DeliverNotificationRequest<Content >  deliverNotificationRequest);

}

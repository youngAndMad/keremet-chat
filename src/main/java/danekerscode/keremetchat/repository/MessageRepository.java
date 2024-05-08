package danekerscode.keremetchat.repository;

import org.springframework.data.repository.query.Param;


public interface MessageRepository {

    Long insertMessage(@Param("content") String content,
                      @Param("chatId") Long chatId,
                      @Param("senderId") Long senderId,
                      @Param("parentId") Long parentId);

    void insertMessageFile(Long messageId, Long fileEntityId);
}
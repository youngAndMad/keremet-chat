package danekerscode.keremetchat.repository;

import danekerscode.keremetchat.model.entity.Message;
import danekerscode.keremetchat.repository.common.CommonRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends CommonRepository<Message, Long> {

    @Query(nativeQuery = true,value = """
            insert into message (created_date, sent_at, content, chat_id, sender_id, parent_id, deleted, edited)
            values(now(),now(),?1, ?2,?3,?4, false,false) returning id;
            """)
    @Modifying
    long insertMessage(String content, Long chatId,Long senderId,Long parentId);

    @Modifying
    @Query(nativeQuery = true,value = """
            insert into message_files(message_id, file_entity_id)
            values(?1,?2);
             """)
    void insertMessageFile(Long messageId,Long fileEntityId);
}
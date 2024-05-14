package danekerscode.keremetchat.repository;

import danekerscode.keremetchat.model.entity.Chat;
import danekerscode.keremetchat.model.projection.ChatProjection;
import danekerscode.keremetchat.repository.common.CommonRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatRepository extends CommonRepository<Chat, Long> {
    @Query(nativeQuery = true, value = """
            insert into chat_avatars(chat_id, file_entity_id)
            values(?1, ?2)
            """)
    @Modifying
    void addAvatar(Long chatId, Long fileEntityId);

    @Query(nativeQuery = true, value = """
            delete from chat_avatars where chat_id = ?1 and file_entity_id = ?2
            """)
    @Modifying
    void deleteAvatar(Long chatId, Long fileEntityId);

    Optional<ChatProjection> findChatById(Long chatId);
}
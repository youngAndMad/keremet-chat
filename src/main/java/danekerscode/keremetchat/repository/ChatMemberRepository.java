package danekerscode.keremetchat.repository;

import danekerscode.keremetchat.model.entity.ChatMember;
import danekerscode.keremetchat.model.projection.ChatMemberProjection;
import danekerscode.keremetchat.repository.common.CommonRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatMemberRepository extends CommonRepository<ChatMember, Long> {

    @Query(nativeQuery = true, value = """
            select cm.user_id from chat_member cm where chat_id = ?1
            """)
    List<Long> findChatMemberUsersId(Long chatId);

    Optional<ChatMember> findByUserIdAndChatId(Long userId, Long chatId);

    @Query(nativeQuery = true, value = """
            update chat_member set last_received_notification_id = :updatedValue where id = :memberId;
            """)
    @Modifying
    void updateLastNotificationState(Long memberId, Long updatedValue);
}
package danekerscode.keremetchat.repository;

import danekerscode.keremetchat.model.entity.ChatMember;
import danekerscode.keremetchat.repository.common.CommonRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMemberRepository extends CommonRepository<ChatMember, Long> {

    @Query(nativeQuery = true, value = """
                select id from chat_member where chat_id=?1 and user_id=?2
            """)
    Long findByChatIdAndUserId(Long chatId, Long userId);

    @Query(nativeQuery = true, value = """
            select cm.user_id from chat_member cm where chat_id = ?1
            """)
    List<Long> findChatMemberUsersId(Long chatId);

}
package danekerscode.keremetchat.repository;

import danekerscode.keremetchat.model.entity.Chat;
import danekerscode.keremetchat.repository.common.CommonRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepository extends CommonRepository<Chat, Long> {
    @Override
    default Class<Chat> getEntityClass() {
        return Chat.class;
    }

    @Query("SELECT cm.user.id FROM ChatMember cm WHERE cm.chat.id = :chatId")
    List<Long> getMemberUsersIdList(Long chatId);

}
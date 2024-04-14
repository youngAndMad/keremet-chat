package danekerscode.keremetchat.repository;

import danekerscode.keremetchat.model.entity.Chat;
import danekerscode.keremetchat.repository.common.CommonRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRepository extends CommonRepository<Chat, Long> {
    @Override
    default Class<Chat> getEntityClass() {
        return Chat.class;
    }

//    @Query(nativeQuery = true,
//            """
//""")
//    boolean isPrivateChatExistsFor(String ownerEmail, String memberEmail);
}
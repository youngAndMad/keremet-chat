package danekerscode.keremetchat.repository;

import danekerscode.keremetchat.model.entity.ChatMember;
import danekerscode.keremetchat.repository.common.CommonRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatMemberRepository extends CommonRepository<ChatMember, Long> {
    @Override
    default Class<ChatMember> getEntityClass() {
        return ChatMember.class;
    }
}
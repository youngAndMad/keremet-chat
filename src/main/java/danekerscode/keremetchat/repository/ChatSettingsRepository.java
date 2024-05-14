package danekerscode.keremetchat.repository;

import danekerscode.keremetchat.model.entity.ChatSettings;
import danekerscode.keremetchat.repository.common.CommonRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatSettingsRepository extends CommonRepository<ChatSettings, Long> {

    Optional<ChatSettings> findByChatId(Long chatId);

}
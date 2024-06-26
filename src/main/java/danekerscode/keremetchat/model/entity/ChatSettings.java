package danekerscode.keremetchat.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class ChatSettings extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    private Chat chat;

    private Boolean everyoneCanInviteMembers;
    private Boolean membersListIsAvailable;
    private Boolean adminsCanEditSettings;

    public static ChatSettings defaultSettingsForChat(Chat chat){
        var chatSettings = new ChatSettings();
        chatSettings.setChat(chat);
        chatSettings.setAdminsCanEditSettings(false);
        chatSettings.setEveryoneCanInviteMembers(true);
        chatSettings.setMembersListIsAvailable(true);
        return chatSettings;
    }
}

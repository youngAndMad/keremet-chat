package danekerscode.keremetchat.model.projection;

import danekerscode.keremetchat.model.enums.AuthType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserProjection {
    private String email;
    private String username;
    private Long id;
    private AuthType authType;
    private boolean isActive = true;;
}

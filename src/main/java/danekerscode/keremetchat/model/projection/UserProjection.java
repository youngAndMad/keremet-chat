package danekerscode.keremetchat.model.projection;

import danekerscode.keremetchat.model.entity.AuthType;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserProjection {
    private String email;
    private String username;
    private Long id;
    private AuthType authType;
    private boolean isActive = true;;
}

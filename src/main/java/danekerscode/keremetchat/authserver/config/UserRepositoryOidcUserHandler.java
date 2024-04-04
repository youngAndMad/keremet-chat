package danekerscode.keremetchat.authserver.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import java.util.function.Consumer;

@Slf4j
public class UserRepositoryOidcUserHandler implements Consumer<OidcUser> {

    @Override
    public void accept(OidcUser oidcUser) {
        log.debug("Saving first-time user: name={}, claims={}, authorities={}", oidcUser.getName(), oidcUser.getClaims(), oidcUser.getAuthorities());
    }
}

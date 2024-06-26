package danekerscode.keremetchat.security.oauth2;

import danekerscode.keremetchat.model.entity.User;
import danekerscode.keremetchat.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CustomOidcUserService extends OidcUserService {
    private final UserRepository userRepository;

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        OidcUser oidcUser = super.loadUser(userRequest);
        Set<GrantedAuthority> mappedAuthorities = mapAuthorities(oidcUser);

        oidcUser = new DefaultOidcUser(mappedAuthorities, oidcUser.getIdToken(), oidcUser.getUserInfo());
        updateUser(oidcUser);

        return oidcUser;
    }

    private void updateUser(OidcUser oidcUser) {
        var attributes = oidcUser.getAttributes();
        String email = (String) attributes.get("login");

        User user = userRepository.findByEmail(email)
                .orElseGet(User::new);
        user.setEmail(email);
        userRepository.save(user);
    }

    private Set<GrantedAuthority> mapAuthorities(OidcUser oidcUser) {
        return new HashSet<>(); //todo
    }
}
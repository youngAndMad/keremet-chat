package danekerscode.keremetchat.authserver.model.auth;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.io.Serializable;
import java.security.Principal;
import java.util.Map;
import java.util.Set;

@Getter
@Setter
public class KeremetChatPrincipal implements OAuth2User, Serializable, Principal {
    private Set<GrantedAuthority> authorities;
    private Map<String, Object> attributes;
    private String name;

    public KeremetChatPrincipal(Set<GrantedAuthority> authorities, Map<String, Object> attributes, String name) {
        this.authorities = authorities;
        this.attributes = attributes;
        this.name = name;
    }

    private Boolean emailVerified;
    private String email;
    private String imageUrl;
}

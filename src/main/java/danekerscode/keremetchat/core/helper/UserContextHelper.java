package danekerscode.keremetchat.core.helper;

import danekerscode.keremetchat.core.mapper.UserMapper;
import danekerscode.keremetchat.model.dto.response.UserResponseDto;
import danekerscode.keremetchat.model.entity.User;
import danekerscode.keremetchat.repository.UserRepository;
import danekerscode.keremetchat.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

@Component
@Slf4j
@RequiredArgsConstructor
public class UserContextHelper implements InitializingBean {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final BeanFactory beanFactory;

    // please never do this
    // simple way to avoid @Cacheable self-invocation
    private Supplier<UserContextHelper> self;

    @Override
    // also we can use @PostConstruct
    public void afterPropertiesSet() throws Exception {
        this.self = () -> beanFactory.getBean(this.getClass());
    }

    @Cacheable(value = "userPrincipal", key = "#authentication.principal")
    public User extractUser(Authentication authentication) {
        var currentAuthPrincipal = authentication.getPrincipal();
        if (currentAuthPrincipal instanceof OAuth2AuthenticationToken oauth2Token) {
            var username = oauth2Token.getPrincipal().getName();
            return userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        }


        if (currentAuthPrincipal instanceof UsernamePasswordAuthenticationToken usernamePasswordToken) {
            var email = usernamePasswordToken.getName();
            return userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        }

        if (currentAuthPrincipal instanceof String email) {
            return userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        }

        if (currentAuthPrincipal instanceof DefaultOAuth2User defaultOAuth2User) {
            var username = defaultOAuth2User.getName();
            return userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        }

        if (currentAuthPrincipal instanceof CustomUserDetails customUserDetails) {
            return customUserDetails.user();
        }


        throw new RuntimeException("Not supported authentication type for user extraction %s".formatted(currentAuthPrincipal.getClass().getName()));
    }

    public UserResponseDto asResponseDto(Authentication authentication) {
        return userMapper.toResponseDto(self.get().extractUser(authentication));
    }

}

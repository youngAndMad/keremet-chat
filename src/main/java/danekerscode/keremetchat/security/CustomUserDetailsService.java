package danekerscode.keremetchat.security;

import danekerscode.keremetchat.model.exception.AuthProcessingException;
import danekerscode.keremetchat.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User with such username was not found")); // todo use own custom exception

        // use Boolean.FALSE#equals to avoid null pointer exception
        if (Boolean.FALSE.equals(user.getEmailConfirmed())) {
            throw new AuthProcessingException("Email not confirmed. Please verify email and try to login", HttpStatus.FORBIDDEN);
        }

        return new CustomUserDetails(user);
    }
}

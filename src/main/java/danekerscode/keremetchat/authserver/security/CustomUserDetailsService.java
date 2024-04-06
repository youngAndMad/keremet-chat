package danekerscode.keremetchat.authserver.security;

import danekerscode.keremetchat.authserver.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User with such username was not found"));

        return new User(
                user.getEmail(),
                user.getPassword(),
                user.getIsActive(),
                user.getIsActive(),
                user.getIsActive(),
                user.getIsActive(),
                List.of(new SimpleGrantedAuthority(user.getRole().name()))
        );
    }
}
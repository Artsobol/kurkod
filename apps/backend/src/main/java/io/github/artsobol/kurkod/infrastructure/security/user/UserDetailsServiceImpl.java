package io.github.artsobol.kurkod.infrastructure.security.user;

import io.github.artsobol.kurkod.feature.user.entity.User;
import io.github.artsobol.kurkod.feature.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @NullMarked
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsernameAndIsActiveTrue(username).orElseThrow(
                () -> new UsernameNotFoundException("User with username: " + username + " not found")
        );
        return new UserDetailsImpl(user);
    }
}

package qyinm.blog.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import qyinm.blog.domain.User.Role;
import qyinm.blog.domain.User.User;
import qyinm.blog.domain.User.UserRepository;
import qyinm.blog.dto.UserDto;

@RequiredArgsConstructor
@Service
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public User signup(UserDto userDto) {
        userRepository.findByEmail(userDto.getEmail())
                .ifPresent(m -> {
                    throw new IllegalArgumentException("already signin username");
                });

        User user = User.builder()
                .email(userDto.getEmail())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .role(Role.USER)
                .build();
        return userRepository.save(user);
    }

    public User signin(UserDto userDto) {
        User user = userRepository.findByEmail(userDto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("empty signin username"));

        if (!passwordEncoder.matches(userDto.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("unmatch user password");
        }

        return user;
    }

    public String getUserEmailByUserId(Long userId) {
        User findUser = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("없는 유저입니다"));

        return findUser.getEmail();
    }
}

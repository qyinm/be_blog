package qyinm.blog.common.util.auth;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import qyinm.blog.service.UserService;
import qyinm.blog.domain.User.User;

@RequiredArgsConstructor
@Component
public class AuthUtils {

    private final UserService userService;

    private String getUsernameInSecurityHolder() {
        return ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
    }

    public User getUserIdInSecurityContextHolder() {
        return userService.getUserByEmail(getUsernameInSecurityHolder());
    }
}

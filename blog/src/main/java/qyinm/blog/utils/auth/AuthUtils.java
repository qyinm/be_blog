package qyinm.blog.utils.auth;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import qyinm.blog.domain.User.PrincipalDetails;
import qyinm.blog.domain.User.User;

@Component
public class AuthUtils {

    public User getUserInSecurityHolder() {
        return ((PrincipalDetails) (SecurityContextHolder.getContext().getAuthentication().getDetails())).getUser();
    }
}

package qyinm.blog.domain.User;

import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Table(name = "users")
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long userId;

    private String email;

    private String password;

    private String nickname;

    private boolean activated;

    public String getPassword() {
        return this.password;
    }

    public String getEmail() {
        return this.email;
    }

    public UserDetails toUserDetails() {
        return new PrincipalDetails(this);
    }
}

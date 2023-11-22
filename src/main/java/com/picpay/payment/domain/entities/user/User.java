package com.picpay.payment.domain.entities.user;

import com.picpay.payment.domain.dto.user.UserDTO;
import com.picpay.payment.domain.entities.auth.Role;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity(name = "tb_users") @Table(name = "tb_users")
@AllArgsConstructor @NoArgsConstructor
@EqualsAndHashCode(of = "id")
@Getter @Setter
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String firstName;
    private String lastName;

    @Column(unique = true)
    private String email;
    private String password;

    @Column(unique = true)
    private String document;
    private BigDecimal balance;
    @Enumerated(EnumType.STRING)
    private UserType userType;

    @Enumerated
    private Role role;
    @OneToMany(mappedBy = "user")
    private List<Token> tokens;


    public static User from(UserDTO dto) {
        var user = new User();
        BeanUtils.copyProperties(dto, user);

        return user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role.getAuthorities();
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

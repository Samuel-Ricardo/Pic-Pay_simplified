package com.picpay.payment.domain.entities.user;

import com.picpay.payment.domain.dto.user.UserDTO;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;

@Entity(name = "users") @Table(name = "users")
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    @Column(unique = true)
    private String document;
    @Column(unique = true)
    private String email;
    private String password;
    private BigDecimal balance;
    @Enumerated(EnumType.STRING)
    private UserType userType;

    static User from(UserDTO dto) {
        var user = new User();
        BeanUtils.copyProperties(dto, user);

        return user;
    }
}

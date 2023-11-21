package com.picpay.payment.domain.entities.auth;

import static com.picpay.payment.domain.entities.auth.TokenType.*;
import com.picpay.payment.domain.entities.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data @Builder
@NoArgsConstructor @AllArgsConstructor
@Entity @Table(name = "tb_tokens")
public class Token {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(unique = true)
    private String hash;

    @Enumerated(EnumType.STRING)
    private TokenType type = BEARER;

    private boolean revoked;
    private boolean expired;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}

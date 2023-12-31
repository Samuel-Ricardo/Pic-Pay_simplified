package com.picpay.payment.domain.entities.transaction;

import com.picpay.payment.domain.entities.user.User;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "tb_transactions") @Table(name = "tb_transactions")
@AllArgsConstructor @NoArgsConstructor
@EqualsAndHashCode(of = "id")
@Getter @Setter
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private BigDecimal amount;
    @ManyToOne
    @JoinColumn(name = "sender_id")
    private User sender;
    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private User receiver;
    @CreatedDate
    private LocalDateTime createdAt;
}

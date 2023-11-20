package com.picpay.payment.policy.transaction;

import com.picpay.payment.domain.entities.user.UserType;
import com.picpay.payment.domain.policy.transaction.MerchantUserCantTransactPolicy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
public class MerchantUserCantTransactPolicyTest {

    @Autowired
    private MerchantUserCantTransactPolicy policy;

    @Test
    @DisplayName("[UNIT] | Should: [TRANSACT] because not is [MERCHANT]")
    void executePolicyCase1() {
        assertThat(policy.execute(UserType.COMMON)).isFalse();
    }

    @Test
    @DisplayName("[UNIT] | Should: not [TRANSACT] because is [MERCHANT]")
    void executePolicyCase2() {
        assertThat(policy.execute(UserType.MERCHANT)).isTrue();
    }

}

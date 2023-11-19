package com.picpay.payment.application.policy.transaction;

import com.picpay.payment.domain.entities.user.UserType;
import static com.picpay.payment.domain.entities.user.UserType.MERCHANT;
import com.picpay.payment.domain.policy.transaction.MerchantUserCantTransactPolicy;
import org.springframework.stereotype.Component;

@Component
public class MerchantUserCantTransactPolicyImpl implements MerchantUserCantTransactPolicy {
    @Override
    public boolean execute(UserType user) {
        return user == MERCHANT;
    }
}

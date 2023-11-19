package com.picpay.payment.domain.policy.transaction;

import com.picpay.payment.domain.entities.user.UserType;

public interface MerchantUserCantTransactPolicy {
    boolean execute(UserType userType);
}

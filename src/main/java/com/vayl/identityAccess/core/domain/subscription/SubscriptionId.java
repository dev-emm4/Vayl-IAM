package com.vayl.identityAccess.core.domain.subscription;

import com.vayl.identityAccess.core.domain.common.DomainErrors.InvalidValueException;
import com.vayl.identityAccess.core.domain.common.IdValidator;

public class SubscriptionId {
    private String id;

    public SubscriptionId(String anId) {
        this.setId(anId);
    }

    public void setId(String anId) {
        this.throwErrorOnInvalidId(anId);
        this.id = anId;
    }

    private void throwErrorOnInvalidId(String anId) {
        if (!IdValidator.isValid(anId)) {
            throw new InvalidValueException("SUBSCRIPTION_ID_CREATION", anId);
        }
    }

    @Override
    public String toString() {
        return this.id;
    }

    @Override
    public boolean equals(Object anObject) {

        boolean isEqual = false;
        if (anObject != null && this.getClass() == anObject.getClass()) {
        SubscriptionId typedObject = (SubscriptionId) anObject;
        isEqual = typedObject.toString().equals(this.toString());
        }

        return isEqual;
    }

    @Override
    public int hashCode() {
        return this.id.hashCode();
    }
}

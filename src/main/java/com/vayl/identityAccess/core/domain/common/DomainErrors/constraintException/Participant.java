package com.vayl.identityAccess.core.domain.common.DomainErrors.constraintException;


public class Participant {
    private final String role;
    private final String type;
    private final String id;

    public Participant(String role, String type, String id){
        this.role = role;
        this.type = type;
        this.id = id;
    }

    @Override
    public String toString() {
        return "{" + this.role + this.type + ", " + this.id + "}";
    }

    @Override
    public boolean equals(Object object) {

        boolean isEqual = false;
        if (object != null && this.getClass() == object.getClass()) {
            Participant typedObject = (Participant) object;
            isEqual = typedObject.toString().equals(this.toString());
        }

        return isEqual;
    }

    @Override
    public int hashCode() {
        return this.toString().hashCode();
    }


}

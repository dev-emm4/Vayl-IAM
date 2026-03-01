package com.vayl.identityAccess.core.domain.common.DomainException;

import tools.jackson.databind.node.ObjectNode;

public interface DomainException {
    ObjectNode toMap();
}

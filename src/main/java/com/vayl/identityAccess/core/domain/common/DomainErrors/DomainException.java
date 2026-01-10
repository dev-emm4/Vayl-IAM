package com.vayl.identityAccess.core.domain.common.DomainErrors;

import tools.jackson.databind.node.ObjectNode;

public interface DomainException {
    ObjectNode toMap();
}

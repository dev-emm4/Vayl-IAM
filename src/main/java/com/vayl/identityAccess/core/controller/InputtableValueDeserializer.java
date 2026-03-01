//package com.vayl.identityAccess.core.controller;
//
//import com.vayl.identityAccess.core.domain.common.inputtableValue.*;
//import com.vayl.identityAccess.core.domain.common.DomainException.InvalidValueException;
//import com.vayl.identityAccess.core.domain.common.DomainException.ExceptionReason;
//import tools.jackson.core.JsonParser;
//import tools.jackson.databind.DeserializationContext;
//import tools.jackson.databind.JsonNode;
//import tools.jackson.databind.ValueDeserializer;
//import com.vayl.identityAccess.core.domain.fieldConfiguration.FieldType;
//
///**
// * Deserializes JSON into an {@link InputtableValue}.
// *
// * Behavior:
// * - If the JSON token is a plain string (e.g. "hello") it returns a {@link StringInput}.
// * - If the JSON token is an object it expects a `type` field matching {@link FieldType}
// *   and a `value` field carrying the payload.
// */
//public class InputtableValueDeserializer extends ValueDeserializer<InputtableValue> {
//
//    @Override
//    public InputtableValue deserialize(JsonParser p, DeserializationContext ctxt) {
//        JsonNode node = p.readValueAsTree();
//        return fromJsonNode(node);
//    }
//
//    /**
//     * Parse an InputtableValue from a JsonNode. Exposed for tests and programmatic use so callers
//     * don't need to register a module on an ObjectMapper.
//     */
//    public static InputtableValue fromJsonNode(JsonNode node) {
//        if (node == null || node.isNull()) {
//            return null;
//        }
//
//        if (node.isString()) {
//            return StringInput.of(node.asString());
//        }
//
//        if (node.isObject()) {
//            JsonNode typeNode = node.get("type");
//            if (typeNode == null || !typeNode.isString()) {
//                throw new InvalidValueException(ExceptionReason.INVALID_FIELD_TYPE, node.toString());
//            }
//
//            String typeText = typeNode.asString();
//            FieldType type;
//            try {
//                type = FieldType.valueOf(typeText);
//            } catch (IllegalArgumentException ex) {
//                throw new InvalidValueException(ExceptionReason.INVALID_FIELD_TYPE, typeText);
//            }
//
//            JsonNode valueNode = node.get("value");
//            switch (type) {
//                case STRING:
//                    // value may be text or something convertible to text
//                    String text = valueNode == null || valueNode.isNull() ? null : valueNode.asString(null);
//                    return StringInput.of(text);
//
//                case INTEGER:
//                    if (valueNode == null || valueNode.isNull()) {
//                        return new IntegerInput((Integer) null);
//                    }
//                    if (valueNode.isInt() || valueNode.isLong()) {
//                        return new IntegerInput(valueNode.asInt());
//                    }
//                    if (valueNode.isTextual()) {
//                        String s = valueNode.asText();
//                        try {
//                            return new IntegerInput(Integer.parseInt(s));
//                        } catch (NumberFormatException nfe) {
//                            throw new InvalidValueException(ExceptionReason.INVALID_INTEGER_INPUT, s);
//                        }
//                    }
//                    throw new InvalidValueException(ExceptionReason.INVALID_INTEGER_INPUT, valueNode.toString());
//
//                case ADDRESS:
//                    // treat address as a simple textual wrapper for now
//                    String addr = valueNode == null || valueNode.isNull() ? null : valueNode.asText(null);
//                    // Here we expect address components; in absence, pass-through as street-only with placeholders
//                    return new AddressInput(addr, addr, addr);
//
//                case DATE:
//                    // Date constructor will validate format and throw on invalid values
//                    String dateText = valueNode == null || valueNode.isNull() ? null : valueNode.asText(null);
//                    return dateText == null ? null : new DateInput(dateText);
//
//                case USERNAME:
//                    String uname = valueNode == null || valueNode.isNull() ? null : valueNode.asText(null);
//                    return new UsernameInput(uname);
//
//                case PHONE:
//                    String phone = valueNode == null || valueNode.isNull() ? null : valueNode.asText(null);
//                    // Expect phone to be represented as { "countryCode": "+1", "subscriber": "2025550125" }
//                    if (valueNode != null && valueNode.has("countryCode") && valueNode.has("subscriber")) {
//                        String cc = valueNode.get("countryCode").asText(null);
//                        String sub = valueNode.get("subscriber").asText(null);
//                        return new PhoneInput(cc, sub);
//                    }
//                    // fallback: try parsing as unified string
//                    return new PhoneInput("+", phone == null ? "" : phone);
//
//                case EMAIL:
//                    String email = valueNode == null || valueNode.isNull() ? null : valueNode.asText(null);
//                    return new EmailInput(email);
//
//                case PASSCODE:
//                    String pass = valueNode == null || valueNode.isNull() ? null : valueNode.asText(null);
//                    return new PasscodeInput(pass);
//
//                default:
//                    throw new InvalidValueException(ExceptionReason.INVALID_FIELD_TYPE, type == null ? null : type.toString());
//            }
//        }
//
//        // For other JSON types (number, boolean, array) you can decide mapping rules. For now, consider them unsupported.
//        throw new InvalidValueException(ExceptionReason.INVALID_FIELD_TYPE, node.toString());
//    }
//}
//

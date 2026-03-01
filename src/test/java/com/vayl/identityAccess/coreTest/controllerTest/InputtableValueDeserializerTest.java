//package com.vayl.identityAccess.coreTest.controllerTest;
//
//import tools.jackson.databind.ObjectMapper;
//import com.vayl.identityAccess.core.domain.common.inputtableValue.InputtableValue;
//import com.vayl.identityAccess.core.controller.InputtableValueDeserializer;
//import com.vayl.identityAccess.core.domain.common.inputtableValue.StringInput;
//import org.junit.jupiter.api.Test;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//public class InputtableValueDeserializerTest {
//
//    private ObjectMapper mapperWithModule() {
//        // The project's ObjectMapper (tools.jackson) does not expose registerModule the same way in all
//        // repackaged versions. For tests, we parse into a JsonNode and use the static helper on the
//        // deserializer to avoid module registration.
//        return new ObjectMapper();
//    }
//
//    @Test
//    public void deserialize_plainString_to_StringInput() throws Exception {
//        ObjectMapper mapper = mapperWithModule();
//        // parse to tree and delegate to helper
//        InputtableValue v =
//            InputtableValueDeserializer.fromJsonNode(mapper.readTree("\"hello world\""));
//        assertNotNull(v);
//        assertTrue(v instanceof StringInput);
//        assertEquals("hello world", ((StringInput) v).value());
//    }
//
//    @Test
//    public void deserialize_objectWithType_STRING_to_StringInput() throws Exception {
//        ObjectMapper mapper = mapperWithModule();
//        String json = "{\"type\":\"STRING\",\"value\":\"value text\"}";
//        InputtableValue v = InputtableValueDeserializer.fromJsonNode(mapper.readTree(json));
//        assertNotNull(v);
//        assertTrue(v instanceof StringInput);
//        assertEquals("value text", ((StringInput) v).value());
//    }
//
//    @Test
//    public void deserialize_objectWithUnknownType_throws() throws Exception {
//        ObjectMapper mapper = mapperWithModule();
//        String json = "{\"type\":\"UNKNOWN\",\"value\":\"value text\"}";
//        Exception ex = assertThrows(Exception.class, () -> InputtableValueDeserializer.fromJsonNode(mapper.readTree(json)));
//        assertNotNull(ex.getMessage());
//    }
//}
//

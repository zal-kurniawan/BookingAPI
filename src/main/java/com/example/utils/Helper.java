package com.example.utils;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.cdimascio.dotenv.Dotenv;
import io.restassured.response.Response;

public class Helper {
    public static Dotenv dotenv;
    private static ObjectMapper objectMapper, mapper = new ObjectMapper();
    private static final String DATA_PATH = "src/test/java/resources/";

    public static Dotenv loaDotenv() {
        if (dotenv == null) {
            dotenv = Dotenv.load();
        }
        return dotenv;
    }

    public static String getKey(String key) {
        String value = loaDotenv().get(key);
        if (value == null) {
            System.out.println("Key not found in .env, try to get from System.getenv()");
            value = System.getenv(key);
        }

        if (value == null) {
            throw new RuntimeException("Key not found: " + key);
        }

        return value;
    }

    public static <T> T convertResponseToObject(Response response, Class<T> clazz) {
        // ObjectMapper objectMapper = new ObjectMapper();
        // ResponseAddBooks responseAddBooks =
        // objectMapper.readValue(response.asString(), ResponseAddBooks.class);
        objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(response.asString(), clazz);
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert response to class: " + clazz.getSimpleName(), e);
        }
    }

    /**
     * Convert raw JSON string to POJO class.
     */
    public static <T> T fromJson(String json, Class<T> clazz) {
        try {
            return mapper.readValue(json, clazz);
        } catch (Exception e) {
            throw new RuntimeException("Failed to map JSON to class " + clazz.getSimpleName(), e);
        }
    }

    /**
     * Read JSON file and convert to POJO class.
     */
    public static <T> T fromJsonFile(String filePath, Class<T> clazz) {
        try {
            System.out.println(DATA_PATH + filePath);
            return mapper.readValue(new File(DATA_PATH + filePath), clazz);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read JSON file: " + filePath, e);
        }
    }

    /**
     * Read JSON file contain multiple data
     */
    public static <T> List<T> fromJsonFileArray(String filePath, Class<T> clazz) {
        try {
            return mapper.readValue(
                    new File(DATA_PATH + filePath),
                    mapper.getTypeFactory().constructCollectionType(List.class, clazz));
        } catch (Exception e) {
            throw new RuntimeException(DATA_PATH + filePath, e);
        }
    }

    /**
     * Get specific payload from JSON file contain multiple data
     * meaning <T> return type is generic
     * 
     * @param filePath
     */
    public static <T> T findPayloadByUseCase(String filePath, String usecase, Class<T> clazz) {
        try {
            JsonNode rootNode = mapper.readTree(new File(DATA_PATH + filePath));
            for (JsonNode node : rootNode) {
                if (node.get("usecase").asText().equals(usecase)) {
                    JsonNode payloadNode = node.get("payload");
                    return mapper.treeToValue(payloadNode, clazz);
                }
            }
            throw new RuntimeException("Usecase not found: " + usecase);
        } catch (Exception e) {
            throw new RuntimeException("Failed to get payload from file: " + filePath, e);
        }
    }

    /**
     * Get specific payload from JSON file contain multiple data
     * meaning <T> return type is generic
     * 
     * @param filePath
     */
    public static <T> T findResponseByUseCase(String filePath, String usecase, Class<T> clazz) {
        try {
            JsonNode rootNode = mapper.readTree(new File(DATA_PATH + filePath));
            for (JsonNode node : rootNode) {
                if (node.get("usecase").asText().equals(usecase)) {
                    JsonNode payloadNode = node.get("response");
                    return mapper.treeToValue(payloadNode, clazz);
                }
            }
            throw new RuntimeException("Usecase not found: " + usecase);
        } catch (Exception e) {
            throw new RuntimeException("Failed to get response from file: " + filePath, e);
        }
    }
}

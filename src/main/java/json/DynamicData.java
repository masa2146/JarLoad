package json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author fatih
 */
public class DynamicData {
    public static String json = "{\n" +
            "\n" +
            "   \"FIELD_1\": 0,\n" +
            "\n" +
            "   \"FIELD_2\": \"VALUE_1\",\n" +
            "\n" +
            "   \"FIELD_3\": 1.1,\n" +
            "\n" +
            "   \"FIELD_4\": true,\n" +
            "\n" +
            "   \"RELATED_RECORDS\": {\n" +
            "\n" +
            "      \"children\": [\n" +
            "\n" +
            "         {\n" +
            "\n" +
            "            \"CH_FIELD_1\": 1,\n" +
            "\n" +
            "            \"CH_FIELD_2\": \"VALUE_2\"\n" +
            "\n" +
            "         },\n" +
            "\n" +
            "         {\n" +
            "\n" +
            "            \"CH_FIELD_1\": 2,\n" +
            "\n" +
            "            \"CH_FIELD_2\": \"VALUE_3\"\n" +
            "\n" +
            "         }\n" +
            "\n" +
            "      ]\n" +
            "\n" +
            "   }\n" +
            "\n" +
            "}";

    public static void main(String[] args) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

//        System.out.println(json);
        JsonNode jsonNode = objectMapper.readTree(json);

        jsonNode.fieldNames().forEachRemaining(s -> {
            System.out.print(s + " --> ");
            System.out.println(jsonNode.get(s).getNodeType());
        });
        System.out.println(jsonNode.at("/RELATED_RECORDS/children").getNodeType());
//        System.out.println(jsonNode.get("cars").get(1));
    }
}

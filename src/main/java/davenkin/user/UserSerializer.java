package davenkin.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import davenkin.utils.DefaultObjectMapper;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;

public class UserSerializer implements Serializer<User> {

    private final ObjectMapper objectMapper = new DefaultObjectMapper();

    @Override
    public byte[] serialize(String topic, User data) {
        try {
            return objectMapper.writeValueAsBytes(data);
        } catch (Exception e) {
            throw new SerializationException("Error when serializing User.");
        }
    }
}

package davenkin;

import davenkin.user.User;
import davenkin.utils.KafkaJsonSerializer;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.*;
import org.junit.jupiter.api.Test;

import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.IntStream;

@Slf4j
public class VanillaKafkaTest {
    @Test
    public void produce_message_with_minimum_configuration() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");

        Producer<String, String> producer = new KafkaProducer<>(props);

        IntStream.range(0, 10)
                .mapToObj(index -> new ProducerRecord<>("test-topic", Integer.toString(index), Integer.toString(index)))
                .forEach(producer::send);

        producer.flush();
        producer.close();
    }

    @Test
    public void produce_json_form_object() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaJsonSerializer.class);

        Producer<String, User> producer = new KafkaProducer<>(props);
        producer.send(new ProducerRecord<>("produce_json_form_object", new User("123", "Mike")));
        producer.flush();
        producer.close();
    }

    @Test
    public void send_synchronously() throws ExecutionException, InterruptedException {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");

        Producer<String, String> producer = new KafkaProducer<>(props);

        Future<RecordMetadata> future = producer.send(new ProducerRecord<>("send_synchronously", "Hello"));
        producer.flush();
        RecordMetadata recordMetadata = future.get();
        log.info("Send message with offset[{}] and partition[{}].", recordMetadata.offset(), recordMetadata.partition());
        producer.close();
    }

    @Test
    public void send_asynchronously() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");

        Producer<String, String> producer = new KafkaProducer<>(props);

        producer.send(new ProducerRecord<>("send_synchronously", "Hello"),
                (metadata, exception) -> log.info("Send message with offset[{}] and partition[{}].", metadata.offset(), metadata.partition()));
        producer.flush();
        producer.close();
    }
}

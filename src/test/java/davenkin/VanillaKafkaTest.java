package davenkin;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.Test;

import java.util.Properties;
import java.util.stream.IntStream;

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
}

package davenkin;

import davenkin.user.User;
import davenkin.utils.KafkaJsonSerializer;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.IntStream;

@Slf4j
public class VanillaKafkaTest {
    @Test
    public void produce_message_with_minimum_configuration() {
        //Producer的最小化配置只需要以下3个配置项
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

        producer.send(new ProducerRecord<>("send_asynchronously", "Hello"),
                (metadata, exception) -> log.info("Send message with offset[{}] and partition[{}].", metadata.offset(), metadata.partition()));
        producer.flush();
        producer.close();
    }

    @Test
    public void consume_message_with_minimum_configuration() throws InterruptedException {
        this.produce_message_with_minimum_configuration();//准备数据

        //Consumer的最小化配置只需要以下4个配置项
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "consume_message_with_minimum_configuration1");
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);

        consumer.subscribe(List.of("test-topic"));
        int count = 0;
        while (count < 10) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
            for (ConsumerRecord<String, String> record : records) {
                log.info("=====Consumed message with topic[{}],partition[{}],offset[{}]", record.topic(), record.partition(), record.offset());
            }
            count++;
        }
        consumer.close();
    }

}

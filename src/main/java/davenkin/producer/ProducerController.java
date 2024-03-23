package davenkin.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/producer")
public class ProducerController {
    private final KafkaTemplate<String, String> kafkaTemplate;

    @GetMapping
    public void produce() {
        kafkaTemplate.send("test-topic", Instant.now().toString());
    }

}

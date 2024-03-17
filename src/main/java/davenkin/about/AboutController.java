package davenkin.about;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZonedDateTime;

import static java.time.ZonedDateTime.now;

@Slf4j
@Validated
@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/about")
public class AboutController {
    private final ZonedDateTime deployTime = now();
    private final Environment environment;

    @GetMapping
    public QAboutInfo about() {
        String environment = this.environment.getActiveProfiles()[0];
        String deployTime = this.deployTime.toString();
        log.info("Accessed about controller.");

        return QAboutInfo.builder()
                .deployTime(deployTime)
                .environment(environment)
                .build();
    }

}

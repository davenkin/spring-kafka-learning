package davenkin;

import davenkin.utils.DefaultObjectMapper;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.EncoderConfig;
import io.restassured.config.LogConfig;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.parallel.Execution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static com.fasterxml.jackson.databind.SerializationFeature.INDENT_OUTPUT;
import static io.restassured.RestAssured.config;
import static io.restassured.http.ContentType.JSON;
import static org.junit.jupiter.api.parallel.ExecutionMode.CONCURRENT;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@Execution(CONCURRENT)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class BaseIntegrationTest {
    @LocalServerPort
    protected int port;

    @Autowired
    protected DefaultObjectMapper objectMapper;


    public static RequestSpecification given() {
        return RestAssured.given()
                .config(config()
                        .objectMapperConfig(new ObjectMapperConfig().jackson2ObjectMapperFactory(
                                (type, s) -> new DefaultObjectMapper()))
                        .encoderConfig(new EncoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false))
                        .logConfig(LogConfig.logConfig().enableLoggingOfRequestAndResponseIfValidationFails()));
    }


    @BeforeEach
    public void setUp() {
        objectMapper.enable(INDENT_OUTPUT);
        RestAssured.port = port;
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
        RestAssured.requestSpecification = new RequestSpecBuilder()
                .setContentType(JSON)
                .setAccept(JSON)
                .build();
    }

    @AfterEach
    public void cleanUp() {
    }


}

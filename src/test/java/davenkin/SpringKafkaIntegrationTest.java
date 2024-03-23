package davenkin;

import org.junit.jupiter.api.Test;

public class SpringKafkaIntegrationTest extends BaseIntegrationTest {
    @Test
    public void send_message() {
        given()
                .when()
                .get("/producer")
                .then()
                .statusCode(200);
    }
}

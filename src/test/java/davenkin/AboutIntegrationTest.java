package davenkin;

import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.containsString;

public class AboutIntegrationTest extends BaseIntegrationTest {
    @Test
    public void should_get_about() {
        given()
                .when()
                .get("/about")
                .then()
                .statusCode(200)
                .body("deployTime", containsString("Asia/Shanghai"));
    }
}

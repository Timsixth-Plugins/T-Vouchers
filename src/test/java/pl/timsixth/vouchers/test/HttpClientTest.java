package pl.timsixth.vouchers.test;

import com.google.gson.JsonObject;
import org.junit.Test;
import pl.timsixth.vouchers.util.HttpClient;

import java.io.IOException;
import java.util.Collections;

import static org.junit.Assert.*;

public class HttpClientTest {

    @Test
    public void shouldDoGetRequest() throws IOException {
        HttpClient.Response response = HttpClient.create("https://timsixth.pl/api/plugins/T-TheTag/currentVersion")
                .get();

        assertNotNull(response.getContent());
        assertEquals(200, response.getStatus());
        assertNotNull(response.getAsJsonObject().get("currentVersion").getAsString());
    }

    @Test(expected = RuntimeException.class)
    public void shouldNotDoGetRequestWithoutCorrectURL() throws IOException {
        HttpClient.create("https:/timsixth.pl/api/plugins/T-TheTag/currentVersion")
                .get();
    }

    @Test
    public void shouldDoPostRequestWithoutCorrectURL() throws IOException {
        JsonObject jsonObject1 = new JsonObject();
        jsonObject1.addProperty("year", 2019);
        jsonObject1.addProperty("price", 1849.99);
        jsonObject1.addProperty("CPU model", "Intel Core i9");
        jsonObject1.addProperty("Hard disk size", "1 TB");

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", "Apple MacBook Pro 16");
        jsonObject.add("data", jsonObject1);

        HttpClient.Response response = HttpClient.create("https://api.restful-api.dev/objects")
                .headers(Collections.singletonMap("Content-Type", "application/json"))
                .post(jsonObject);

        assertTrue(response.isJson());
        assertEquals(200, response.getStatus());
        assertNotNull(response.getAsJsonObject());
    }
}

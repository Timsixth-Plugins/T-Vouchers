package pl.timsixth.vouchers.util;


import com.google.gson.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

@RequiredArgsConstructor
public final class HttpClient {

    private final URL url;
    private Map<String, String> headers = new LinkedHashMap<>();
    private static Gson gson;

    private HttpClient() {
        this.url = null;
    }

    public static HttpClient create(String urlAsString) {
        try {
            gson = new Gson();

            return new HttpClient(new URL(urlAsString));
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public static HttpClient create(URL url) {
        gson = new Gson();

        return new HttpClient(url);
    }

    public HttpClient headers(Map<String, String> headers) {
        this.headers = headers;

        return this;
    }

    public Response post(JsonElement body) throws IOException {
        HttpURLConnection connection = connect("POST");

        if (body != null) {
            connection.setDoOutput(true);

            String json = gson.toJson(body);

            try (OutputStream outputStream = connection.getOutputStream()) {
                byte[] input = json.getBytes(StandardCharsets.UTF_8);
                outputStream.write(input, 0, input.length);
            }
        }

        return getResponseBody(connection);
    }

    public Response post() throws IOException {
        return post(null);
    }

    public Response get() throws IOException {
        HttpURLConnection connection = connect("GET");

        return getResponseBody(connection);
    }

    private HttpURLConnection connect(String method) throws IOException {
        if (url == null) {
            throw new NullPointerException("URL can not be null");
        }

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(method);
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);
        connection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:56.0) Gecko/20100101 Firefox/56.0");

        for (Map.Entry<String, String> headers : headers.entrySet()) {
            connection.addRequestProperty(headers.getKey(), headers.getValue());
        }

        return connection;
    }

    private static Response getResponseBody(HttpURLConnection connection) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader reader;

        if (connection.getResponseCode() == 200) {
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        } else {
            reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
        }

        String line;
        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line);
        }

        reader.close();

        String responseContent = stringBuilder.toString();

        Response response = new Response(connection.getResponseCode(), responseContent);

        try {
            response.jsonElement = gson.fromJson(responseContent, JsonElement.class);

            return response;
        } catch (JsonSyntaxException exception) {
            return response;
        }
    }

    @RequiredArgsConstructor
    @Getter
    public static class Response {

        private final int status;
        private final String content;
        private JsonElement jsonElement;

        private Response() {
            this.status = 0;
            this.content = "";
        }

        public boolean isJson() {
            return jsonElement != null;
        }

        public JsonObject getAsJsonObject() {
            return jsonElement.getAsJsonObject();
        }

        public JsonArray getAsJsonArray() {
            return jsonElement.getAsJsonArray();
        }
    }

}
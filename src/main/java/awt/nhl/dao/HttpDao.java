package awt.nhl.dao;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

import static java.net.HttpURLConnection.HTTP_OK;

public class HttpDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpDao.class);
    private static final Jsonb JSONB = JsonbBuilder.create();

    private static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
        .followRedirects(HttpClient.Redirect.ALWAYS)
        .connectTimeout(Duration.ofSeconds(20))
        .build();

    public <T> T get(final String url, final Class<T> clazz) throws IOException {
        try {
            LOGGER.info("Getting information from url '{}'", url);
            final HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();
            final HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
            final String body = response.body();
            if (response.statusCode() == HTTP_OK) {
                return JSONB.fromJson(body, clazz);
            } else {
                throw new IOException(url + " returned " + response.statusCode()
                    + " with a response of " + body);
            }
        } catch (InterruptedException exception) {
            throw new IOException(exception);
        }
    }
}

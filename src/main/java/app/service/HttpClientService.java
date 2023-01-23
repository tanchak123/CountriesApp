package app.service;

import app.data.exceptions.HttpException;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class HttpClientService {

    private static final int MAX_IDLE_CONNECTIONS = 200;
    private static final int KEEP_ALIVE_DURATION = 10;
    private static final int CONNECTION_TIMEOUT_S = 3;
    private static final int READ_TIMEOUT_S = 5;
//    private static final MediaType JSON_MEDIA_TYPE = MediaType.parse("application/json");


    private static OkHttpClient getInstance() {
        return new OkHttpClient.Builder()
                .connectionPool(new ConnectionPool(MAX_IDLE_CONNECTIONS, KEEP_ALIVE_DURATION, TimeUnit.SECONDS))
                .connectTimeout(CONNECTION_TIMEOUT_S, TimeUnit.SECONDS)
                .readTimeout(READ_TIMEOUT_S, TimeUnit.SECONDS).build();
    }

    public String doGet(String url) {
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        return executeRequest(request);
    }

    public String executeRequest(Request request) {
        val id = UUID.randomUUID();
        try {
            log.trace("Executing request [{}]: {}.", id, request);

            Response response = getInstance().newCall(request).execute();
            log.trace("Received response [{}]: {}", id, response);
            String responseBodyString;

            if (response.body() == null) {
                throw new HttpException("Empty response body " + id);
            } else {
                responseBodyString = Objects.requireNonNull(response.body()).string();
            }

            log.trace("Extracted response body [{}]: {}", id, responseBodyString);

            return responseBodyString;
        } catch (HttpException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Request failed. " + request.url() + " " + id, e);
        }
    }

}

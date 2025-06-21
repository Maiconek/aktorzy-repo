package pl.baranowski.marcin.aktorzy;

import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.core5.util.TimeValue;
import org.apache.hc.core5.util.Timeout;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;


@Configuration
public class RestClientConfig {

    @Bean
    public RestClient.Builder restClientBuilder() {
        // Tworzenie menedżera połączeń
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(1000); // Całkowita pula połączeń
        connectionManager.setDefaultMaxPerRoute(800); // Dla jednego hosta

        // Konfiguracja timeoutów
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(Timeout.ofSeconds(40))   // max czas na połączenie
                .setResponseTimeout(Timeout.ofSeconds(60))  // max czas oczekiwania na odpowiedź
                .build();

        // Tworzenie klienta Apache HttpClient
        CloseableHttpClient httpClient = HttpClients.custom()
                .setConnectionManager(connectionManager)
                .setDefaultRequestConfig(requestConfig)
                .evictIdleConnections(TimeValue.ofSeconds(30)) // zamykanie nieużywanych połączeń
                .build();

        // Adapter do Springa
        HttpComponentsClientHttpRequestFactory requestFactory =
                new HttpComponentsClientHttpRequestFactory(httpClient);

        // Zwracamy builder, nie gotowy klient
        return RestClient.builder()
                .requestFactory(requestFactory);
    }
}



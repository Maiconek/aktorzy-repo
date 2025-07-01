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
    public RestClient restClient() {
        // Tworzenie menedżera połączeń
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(1600); // dla wszystkich hostow
        connectionManager.setDefaultMaxPerRoute(1400); // dla pojedynczego hosta

        // Konfiguracja timeoutów
        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(Timeout.ofSeconds(90))
                .setResponseTimeout(Timeout.ofSeconds(120))
                .build();

        // Tworzenie klienta Apache
        CloseableHttpClient httpClient =
                HttpClients.custom()
                        .setConnectionManager(connectionManager)
                        .setDefaultRequestConfig(config)
                        .evictIdleConnections(TimeValue.ofSeconds(60))
                        .build();

        // Adapter do Springa (tu używamy Apache HttpClient!)
        HttpComponentsClientHttpRequestFactory requestFactory =
                new HttpComponentsClientHttpRequestFactory(httpClient);

        // Tworzenie RestClient z naszą fabryką
        return RestClient.builder()
                .requestFactory(requestFactory)
                .baseUrl("https://vt-app-serviceapp-dmbthkaqe6a3ffh0.polandcentral-01.azurewebsites.net")
                .build();
    }
}



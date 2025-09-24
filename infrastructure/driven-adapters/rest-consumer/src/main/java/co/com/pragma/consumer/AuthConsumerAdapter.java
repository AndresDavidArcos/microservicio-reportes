package co.com.pragma.consumer;

import co.com.pragma.consumer.dto.LoginRequestDTO;
import co.com.pragma.consumer.dto.LoginResponseDTO;
import co.com.pragma.consumer.dto.UserDTO;
import co.com.pragma.model.reporte.gateways.AdminGateway;
import co.com.pragma.secretsprovider.SecretsProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class AuthConsumerAdapter implements AdminGateway {

    private final WebClient webClient;
    private final SecretsProvider secretsProvider;
    private static final String LOGIN_ENDPOINT = "/api/v1/login";
    private static final String GET_ADMINS_ENDPOINT = "/api/v1/usuarios/rol/ADMIN";


    public AuthConsumerAdapter(WebClient.Builder webClientBuilder, SecretsProvider secretsProvider,
                               @Value("${adapter.restconsumer.url}") String baseUrl) {
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
        this.secretsProvider = secretsProvider;
    }

    @Override
    public Flux<String> obtenerCorreosAdmin() {
        return getToken()
                .flatMapMany(token -> getAdmins(token))
                .map(UserDTO::getCorreoElectronico);
    }

    private Mono<String> getToken() {
        LoginRequestDTO request = LoginRequestDTO.builder()
                .correo(secretsProvider.getServiceUserEmail())
                .password(secretsProvider.getServiceUserPassword())
                .build();

        return webClient.post()
                .uri(LOGIN_ENDPOINT)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(LoginResponseDTO.class)
                .map(LoginResponseDTO::getToken);
    }

    private Flux<UserDTO> getAdmins(String token) {
        return webClient.get()
                .uri(GET_ADMINS_ENDPOINT)
                .headers(h -> h.setBearerAuth(token))
                .retrieve()
                .bodyToFlux(UserDTO.class);
    }
}

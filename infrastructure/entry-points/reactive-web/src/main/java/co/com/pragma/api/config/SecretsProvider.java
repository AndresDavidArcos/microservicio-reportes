package co.com.pragma.api.config;
import co.com.bancolombia.secretsmanager.api.GenericManagerAsync;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
@Getter
public class SecretsProvider {

    private final GenericManagerAsync secretsManager;
    private final Gson gson = new Gson();

    @Value("${aws.secretName}")
    private String secretName;

    private String jwtSecret;

    @SneakyThrows
    @PostConstruct
    public void loadSecrets() {
        String secretValueJson = secretsManager.getSecret(secretName).block();
        log.info("Cargando secretos desde AWS Secrets Manager...");
        JsonObject secretJson = gson.fromJson(secretValueJson, JsonObject.class);

        this.jwtSecret = secretJson.get("JWT_SECRET").getAsString();

        log.info("Secretos cargados exitosamente.");
    }

}

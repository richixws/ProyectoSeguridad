package pe.gob.bcrp.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class RedisTokenService {

    private final RedisTemplate<String, String> redisTemplate;

    @Autowired
    public RedisTokenService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }


    // Almacenar el último token válido para un usuario
    public void storeLatestToken(String username, String token) {
        // Almacenar el token con una clave única por usuario
        redisTemplate.opsForValue().set(
                "latest_token:" + username,
                token,
                Duration.ofMinutes(30)  // Tiempo de expiración configurable
        );
    }

    // Verificar si el token actual es el último token válido para el usuario
    public boolean isLatestToken(String username, String token) {
        String latestToken = redisTemplate.opsForValue().get("latest_token:" + username);
        return latestToken != null && latestToken.equals(token);
    }

    // Método para invalidar tokens anteriores
    public void invalidatePreviousTokens(String username, String newToken) {
        storeLatestToken(username, newToken);
    }

}

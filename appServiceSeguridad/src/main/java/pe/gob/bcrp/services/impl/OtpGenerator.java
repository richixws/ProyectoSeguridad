package pe.gob.bcrp.services.impl;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;

@Service
public class OtpGenerator {

    private static final Integer EXPIRE_MIN = 3;
    private LoadingCache<String, Integer> otpCache;
    // Caché para almacenar los intentos fallidos
    private LoadingCache<String, Integer> failedAttemptsCache;

    /**
     * Constructor configuration.
     */
    public OtpGenerator(){
        super();
        otpCache = CacheBuilder.newBuilder()
                .expireAfterWrite(EXPIRE_MIN, TimeUnit.MINUTES)
                .build(new CacheLoader<String, Integer>() {
                    @Override
                    public Integer load(String s) throws Exception {
                        return 0;
                    }
                });

        // Configuración del caché para los intentos fallidos
        failedAttemptsCache = CacheBuilder.newBuilder()
                .expireAfterWrite(EXPIRE_MIN, TimeUnit.MINUTES)
                .build(new CacheLoader<String, Integer>() {
                    @Override
                    public Integer load(String s) {
                        return 0; // Valor por defecto
                    }
                });
    }

    /**
     * Method for generating OTP and put it in cache.
     *
     * @param key - cache key
     * @return cache value (generated OTP number)
     */
    public String generateOTP(String key)
    {
       // Random random = new Random();
       // int OTP = 100000 + random.nextInt(900000);
        SecureRandom random = new SecureRandom();
        int OTP = 100000 + random.nextInt(900000);
        otpCache.put(key, OTP);

        String output = Integer.toString(OTP);

        while (output.length() < 6) {
            output = "0" + output;
        }



        return output;

      // return OTP;
    }

    /**
     * Method for getting OTP value by key.
     *
     * @param key - target key
     * @return OTP value
     */
    public Integer getOPTByKey(String key)
    {
        return otpCache.getIfPresent(key);
    }

    /**
     * Method for removing key from cache.
     *
     * @param key - target key
     */
    public void clearOTPFromCache(String key) {
        otpCache.invalidate(key);
    }

    /**
     * Obtener intentos fallidos por usuario.
     */
    public Integer getFailedAttempts(String username) {
        return failedAttemptsCache.getIfPresent(username);
    }

    /**
     * Actualizar los intentos fallidos.
     */
    public void updateFailedAttempts(String username, Integer attempts) {
        failedAttemptsCache.put(username, attempts);
    }

    /**
     * Limpiar intentos fallidos.
     */
    public void clearFailedAttempts(String username) {
        failedAttemptsCache.invalidate(username);
    }



}

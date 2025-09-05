package co.com.pragma.jwtgenerator;

import co.com.pragma.model.jwt.gateways.TokenRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;


@Component
public class JwtGenerator implements TokenRepository {


    private final JwtProperties properties;
    private static final Logger log = Logger.getLogger(JwtGenerator.class.getName());


    public JwtGenerator(JwtProperties properties) {
        this.properties = properties;
    }

    @Override
    public Mono<String> generarToken(String correo, String rol) {
        return Mono.fromCallable(() -> {
            byte[] keyBytes = properties.getSecretKey().getBytes(StandardCharsets.UTF_8);

            long expirationMillis = properties.getExpiration() * 1000L;

            return Jwts.builder()
                    .setSubject(correo)
                    .claim("rol", "ROLE_"+rol)
                    .claim("email", correo)
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + expirationMillis))
                    .signWith(new SecretKeySpec(keyBytes, SignatureAlgorithm.HS256.getJcaName()))
                    .compact();
        });
    }

    @Override
    public Mono<Boolean> validarToken(String token) {
        return Mono.fromCallable(() -> {
            try {
                Jws<Claims> claimsJws = Jwts.parserBuilder()
                        .setSigningKey(getKey(properties.getSecretKey()))
                        .build()
                        .parseClaimsJws(token);

                Date expiration = claimsJws.getBody().getExpiration();
                return expiration == null || expiration.after(new Date()); // true si no está expirado
            } catch (ExpiredJwtException e) {
                log.info("Token expirado");
                return false;
            } catch (UnsupportedJwtException e) {
                log.info("Token no soportado");
                return false;
            } catch (MalformedJwtException e) {
                log.info("Token mal formado");
                return false;
            } catch (SignatureException e) {
                log.info("Firma inválida");
                return false;
            } catch (IllegalArgumentException e) {
                log.info("Token vacío o nulo");
                return false;
            }
        });
    }

    @Override
    public String extractUserEmail(String token) {
        Claims claims = obtenerClaims(token);
        return claims.getSubject();
    }

    @Override
    public String extractRole(String token) {
        Claims claims = obtenerClaims(token);
        return claims.get("rol", String.class); // ROLE_ADMIN, ROLE_USER
    }


    private Claims obtenerClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getKey(properties.getSecretKey()))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }


    private SecretKey getKey(String secretKey) {
        byte[] secretBytes  = secretKey.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(secretBytes);
    }
}



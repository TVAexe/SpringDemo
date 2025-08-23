package demo.backend.tuto.demo.utils;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;


@Service
public class SecurityUtils {

    public static final MacAlgorithm JWT_ALGORITHM = MacAlgorithm.HS512;
    private final JwtEncoder jwtEncoder;

    public SecurityUtils(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }
    @Value("${demo.jwt.base64-secret}")
    private String jwtKey;

    @Value("${demo.jwt.token-validity-in-seconds}")
    private Long jwtTokenValidity;

    public String createToken(Authentication authentication) {
        Instant now = Instant.now();
        Instant validity = now.plus(this.jwtTokenValidity, ChronoUnit.SECONDS);

        // @formatter:off
        JwtClaimsSet claims = JwtClaimsSet.builder()
                                            .issuedAt(now)
                                            .expiresAt(validity)
                                            .subject(authentication.getName())
                                            .claim("Hoi dan IT", authentication)
                                            .build();
        JwsHeader jwsHeader = JwsHeader.with(JWT_ALGORITHM).build();
        return this.jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();
    }
}

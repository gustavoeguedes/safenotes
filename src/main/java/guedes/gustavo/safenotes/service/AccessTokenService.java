package guedes.gustavo.safenotes.service;

import guedes.gustavo.safenotes.config.JwtConfig;
import guedes.gustavo.safenotes.entity.Role;
import guedes.gustavo.safenotes.entity.Scope;
import guedes.gustavo.safenotes.entity.User;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AccessTokenService {
    private final JwtConfig jwtConfig;
    private final JwtEncoder jwtEncoder;

    public AccessTokenService(JwtConfig jwtConfig,
                              JwtEncoder jwtEncoder) {
        this.jwtConfig = jwtConfig;
        this.jwtEncoder = jwtEncoder;
    }

    public Jwt generateAccessToken(User user, UUID familyId) {
        var roles = getRoles(user);

        var scopes = getScopes(user);
        var tokenVersion = user.getTokenVersion();

        var claims = JwtClaimsSet.builder()
                .id(UUID.randomUUID().toString())
                .subject(user.getId().toString())
                .issuer(jwtConfig.getIssuer())
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(jwtConfig.getExpiresIn()))
                .claim("username", user.getUsername())
                .claim("family_id", familyId)
                .claim("version", tokenVersion)
                .claim("roles", roles)
                .claim("scope", scopes)
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims));
    }

    private Set<String> getScopes(User user) {
        Set<Scope> mergedScopes = user.getRoles().stream()
                .map(Role::getScopes)
                .flatMap(Collection::stream)
                .collect(Collectors.toCollection(HashSet::new));

        mergedScopes.addAll(user.getScopes());
        return mergedScopes
                .stream()
                .map(Scope::getName)
                .collect(Collectors.toSet());
    }

    private Set<String> getRoles(User user) {
        return user.getRoles()
                .stream()
                .map(Role::getName)
                .collect(Collectors.toSet());
    }
}

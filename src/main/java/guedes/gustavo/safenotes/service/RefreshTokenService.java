package guedes.gustavo.safenotes.service;

import guedes.gustavo.safenotes.config.JwtConfig;
import guedes.gustavo.safenotes.config.OpaqueToken;
import guedes.gustavo.safenotes.entity.RefreshToken;
import guedes.gustavo.safenotes.entity.User;
import guedes.gustavo.safenotes.repository.RefreshTokenRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class RefreshTokenService {
    private final RefreshTokenRepository  refreshTokenRepository;
    private final JwtConfig jwtConfig;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository,
                               JwtConfig jwtConfig) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.jwtConfig = jwtConfig;
    }

    @Transactional
    public String generateRefreshToken(User  user, UUID familyId) {
        var opaqueToken = OpaqueToken.generate();
        var opaqueTokenHash = OpaqueToken.generateOpaqueHash(opaqueToken);

        refreshTokenRepository.save(
                new RefreshToken(
                        familyId,
                        opaqueTokenHash,
                        user,
                        Instant.now(),
                        Instant.now().plusSeconds(jwtConfig.getRefreshExpiresIn())
                )
        );
        return opaqueToken;
    }
}

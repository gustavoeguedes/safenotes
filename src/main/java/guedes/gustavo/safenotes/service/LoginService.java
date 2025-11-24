package guedes.gustavo.safenotes.service;

import guedes.gustavo.safenotes.config.JwtConfig;
import guedes.gustavo.safenotes.controller.dto.LoginResponse;
import guedes.gustavo.safenotes.entity.User;
import guedes.gustavo.safenotes.repository.RefreshTokenRepository;
import guedes.gustavo.safenotes.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class LoginService {

    private final UserRepository userRepository;
    private final PasswordEncoder bCryptPasswordEncoder;

    private final RefreshTokenService refreshTokenService;
    private final AccessTokenService accessTokenService;
    private final JwtConfig jwtConfig;


    public LoginService(UserRepository userRepository,
                        PasswordEncoder bCryptPasswordEncoder,
                         RefreshTokenService refreshTokenService,
                        AccessTokenService accessTokenService,
                        JwtConfig jwtConfig) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.refreshTokenService = refreshTokenService;
        this.accessTokenService = accessTokenService;
        this.jwtConfig = jwtConfig;
    }

    @Transactional
    public LoginResponse login(String username, String password) {
        var user = validateUser(username, password);

        var familyId = UUID.randomUUID();

        var opaqueToken = refreshTokenService.generateRefreshToken(user, familyId);

        var accessToken = accessTokenService.generateAccessToken(user, familyId);
        return new LoginResponse(
                accessToken.getTokenValue(),
                jwtConfig.getExpiresIn(),
                opaqueToken,
                jwtConfig.getRefreshExpiresIn(),
                accessToken.getClaimAsStringList("scope")

        );

    }

    private User validateUser(String username, String password) {
        var user = userRepository.findByUsername(username).orElseThrow(RuntimeException::new);

        var isPasswordValid = bCryptPasswordEncoder.matches(password, user.getPassword());

        if (!isPasswordValid) {
            throw new RuntimeException("Invalid password");
        }

        return user;
    }
}

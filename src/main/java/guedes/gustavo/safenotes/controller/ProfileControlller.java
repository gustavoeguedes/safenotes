package guedes.gustavo.safenotes.controller;

import guedes.gustavo.safenotes.controller.dto.ProfileResponse;
import guedes.gustavo.safenotes.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/me")
public class ProfileControlller {

    private final UserService userService;

    public ProfileControlller(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('SCOPE_profile:read')")
    public ResponseEntity<ProfileResponse> profile(@AuthenticationPrincipal Jwt jwt) {
        var resp = userService.readProfile(jwt);
        return ResponseEntity.ok(resp);
    }
}

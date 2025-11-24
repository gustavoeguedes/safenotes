package guedes.gustavo.safenotes.controller;

import guedes.gustavo.safenotes.controller.dto.LoginRequest;
import guedes.gustavo.safenotes.controller.dto.LoginResponse;
import guedes.gustavo.safenotes.service.LoginService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/auth")
public class AuthController {
    private final LoginService loginService;

    public AuthController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest req) {
        var resp = loginService.login(req.username(), req.password());

        return ResponseEntity.ok(resp);

    }
}

package guedes.gustavo.safenotes.config;

import guedes.gustavo.safenotes.entity.Role;
import guedes.gustavo.safenotes.entity.Scope;
import guedes.gustavo.safenotes.entity.User;
import guedes.gustavo.safenotes.repository.RoleRepository;
import guedes.gustavo.safenotes.repository.ScopeRepository;
import guedes.gustavo.safenotes.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Set;

@Configuration
public class RBACSeed implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final ScopeRepository scopeRepository;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public RBACSeed(RoleRepository roleRepository,
                    ScopeRepository scopeRepository,
                    UserRepository userRepository) {
        this.roleRepository = roleRepository;
        this.scopeRepository = scopeRepository;
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = new BCryptPasswordEncoder();
    }

    @Override
    public void run(String... args) throws Exception {

        var noteRead = ensureScope("note:read");
        var noteWrite = ensureScope("note:write");
        var profileRead = ensureScope("profile:read");

        var roleViewer = ensureRole("VIEWER", Set.of(profileRead));
        var roleUser = ensureRole("USER", Set.of(noteRead, noteWrite, profileRead));

        ensureUser("bruno", "senha", roleUser);
        ensureUser("gustavo", "senha", roleUser);
        ensureUser("ana", "senha", roleViewer);
    }

    private Scope ensureScope(String name) {
        return scopeRepository.findByName(name)
                .orElseGet(() -> scopeRepository.save(new Scope(name)));
    }

    private Role ensureRole(String name, Set<Scope> scopes) {
        return roleRepository.findByName(name)
                .map(role -> {
                    role.setScopes(scopes);
                    return roleRepository.save(role);
                })
                .orElseGet(() -> roleRepository.save(new Role(name, scopes)));
    }

    private User ensureUser(String username, String password, Role role) {

        var passwordEncoded = bCryptPasswordEncoder.encode(password);

        return userRepository.findByUsername(username)
                .map(user -> {
                    user.setPassword(passwordEncoded);
                    user.setRoles(Set.of(role));
                    return userRepository.save(user);
                })
                .orElseGet(() -> userRepository.save(new User(username, passwordEncoded, Set.of(role), 1)));
    }
}

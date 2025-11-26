package guedes.gustavo.safenotes.config;


import guedes.gustavo.safenotes.repository.NoteRepository;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component("noteAuthz")
public class NoteAuthz {

    private final NoteRepository noteRepository;
    public NoteAuthz(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    @Transactional(readOnly = true)
    public boolean hasAccess(Long id,  Jwt jwt) {

        var userId = Long.valueOf(jwt.getSubject());

        return noteRepository.existsByIdAndOwnerId(id, userId);
    }
}

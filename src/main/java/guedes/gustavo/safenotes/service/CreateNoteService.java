package guedes.gustavo.safenotes.service;

import guedes.gustavo.safenotes.entity.Note;
import guedes.gustavo.safenotes.entity.User;
import guedes.gustavo.safenotes.repository.NoteRepository;
import guedes.gustavo.safenotes.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class CreateNoteService {
    private final NoteRepository noteRepository;
    private final UserRepository userRepository;
    public CreateNoteService(NoteRepository noteRepository,
                             UserRepository userRepository) {
        this.userRepository = userRepository;
        this.noteRepository = noteRepository;
    }

    public Note createNote(String content,
                           String title, Long ownerId) {
        var note = new guedes.gustavo.safenotes.entity.Note();
        var user = findUserById(ownerId);
        note.setContent(content);
        note.setOwner(user);
        note.setTitle(title);
        return noteRepository.save(note);
    }

    private User findUserById(Long ownerId) {
        return userRepository.findById(ownerId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}

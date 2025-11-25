package guedes.gustavo.safenotes.service;

import guedes.gustavo.safenotes.controller.dto.UpdateNoteRequest;
import guedes.gustavo.safenotes.repository.NoteRepository;
import org.springframework.stereotype.Service;

@Service
public class UpdateNoteService {

    private final NoteRepository noteRepository;

    public UpdateNoteService(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    public void updateNote(Long id, Long userId, UpdateNoteRequest req) {
        var note = noteRepository.findByIdAndOwnerId(id, userId)
                .orElseThrow(() -> new RuntimeException("Note not found"));

        if (req.title() != null) {
            note.setTitle(req.title());
        }

        if (req.content() != null) {
            note.setContent(req.content());
        }


        noteRepository.save(note);

    }
}

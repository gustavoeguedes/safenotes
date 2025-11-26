package guedes.gustavo.safenotes.service;

import guedes.gustavo.safenotes.controller.dto.UpdateNoteRequest;
import guedes.gustavo.safenotes.repository.NoteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UpdateNoteService {

    private final NoteRepository noteRepository;

    public UpdateNoteService(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    @Transactional
    public void updateNote(Long id, UpdateNoteRequest req) {
        var note = noteRepository.getReferenceById(id);

        if (req.title() != null) {
            note.setTitle(req.title());
        }

        if (req.content() != null) {
            note.setContent(req.content());
        }


        noteRepository.save(note);

    }
}

package guedes.gustavo.safenotes.service;

import guedes.gustavo.safenotes.repository.NoteRepository;
import org.springframework.stereotype.Service;

@Service
public class DeleteNoteService {
    private final NoteRepository noteRepository;

    public DeleteNoteService(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    public void deleteNote(Long id) {
        noteRepository.deleteById(id);
    }
}

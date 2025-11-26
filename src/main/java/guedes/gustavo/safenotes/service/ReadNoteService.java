package guedes.gustavo.safenotes.service;

import guedes.gustavo.safenotes.entity.Note;
import guedes.gustavo.safenotes.repository.NoteRepository;
import org.springframework.stereotype.Service;

@Service
public class ReadNoteService {
    private NoteRepository noteRepository;
    public ReadNoteService(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    public Note readNote(Long id) {
        return noteRepository.getReferenceById(id);

    }
}

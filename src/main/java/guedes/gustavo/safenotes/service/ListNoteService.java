package guedes.gustavo.safenotes.service;

import guedes.gustavo.safenotes.controller.dto.NoteResponse;
import guedes.gustavo.safenotes.entity.Note;
import guedes.gustavo.safenotes.repository.NoteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListNoteService {
    private final NoteRepository noteRepository;

    public ListNoteService(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    public List<NoteResponse> listNotes(Long userId) {
        return noteRepository.findAllByOwnerId(userId)
                .stream()
                .map(note -> new NoteResponse(note.getId(), note.getTitle(), note.getContent()))
                .toList();
    }

}

package guedes.gustavo.safenotes.controller;

import guedes.gustavo.safenotes.controller.dto.NoteRequest;
import guedes.gustavo.safenotes.controller.dto.NoteResponse;
import guedes.gustavo.safenotes.controller.dto.UpdateNoteRequest;
import guedes.gustavo.safenotes.service.CreateNoteService;
import guedes.gustavo.safenotes.service.ListNoteService;
import guedes.gustavo.safenotes.service.UpdateNoteService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(path = "/notes")
public class NoteController {

    private final ListNoteService listNoteService;
    private final CreateNoteService createNoteService;
    private final UpdateNoteService updateNoteService;

    public NoteController(ListNoteService listNoteService,
                          CreateNoteService createNoteService,
                          UpdateNoteService updateNoteService) {
        this.createNoteService = createNoteService;
        this.listNoteService = listNoteService;
        this.updateNoteService = updateNoteService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('SCOPE_note:read')")
    public ResponseEntity<List<NoteResponse>> listAllNotes(@AuthenticationPrincipal Jwt jwt) {
        var notes = listNoteService.listNotes(Long.valueOf(jwt.getSubject()));

        return ResponseEntity.ok(notes);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('SCOPE_note:write')")
    public ResponseEntity<Void> createNote(@AuthenticationPrincipal Jwt jwt,
                                           @RequestBody NoteRequest noteRequest) {
        var note = createNoteService.createNote(noteRequest.content(),
                noteRequest.title(),
                Long.valueOf(jwt.getSubject()));

        return ResponseEntity.created(URI.create("/notes/" + note.getId())).build();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_note:write')")
    public ResponseEntity<Void> updateNote(@PathVariable Long id,
                                           @AuthenticationPrincipal Jwt jwt,
                                           @RequestBody UpdateNoteRequest noteRequest) {
        updateNoteService.updateNote(id, Long.valueOf(jwt.getSubject()), noteRequest);

        return ResponseEntity.noContent().build();
    }
}

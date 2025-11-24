package guedes.gustavo.safenotes.controller;

import guedes.gustavo.safenotes.controller.dto.NoteRequest;
import guedes.gustavo.safenotes.controller.dto.NoteResponse;
import guedes.gustavo.safenotes.service.CreateNoteService;
import guedes.gustavo.safenotes.service.ListNoteService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/notes")
public class NoteController {

    private final ListNoteService listNoteService;
    private final CreateNoteService createNoteService;

    public NoteController(ListNoteService listNoteService,
                          CreateNoteService createNoteService) {
        this.createNoteService = createNoteService;
        this.listNoteService = listNoteService;
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
        createNoteService.createNote(noteRequest.content(),
                noteRequest.title(),
                Long.valueOf(jwt.getSubject()));
        return ResponseEntity.ok().build();
    }
}

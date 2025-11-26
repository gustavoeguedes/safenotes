package guedes.gustavo.safenotes.controller;

import guedes.gustavo.safenotes.controller.dto.NoteRequest;
import guedes.gustavo.safenotes.controller.dto.NoteResponse;
import guedes.gustavo.safenotes.controller.dto.UpdateNoteRequest;
import guedes.gustavo.safenotes.service.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.parameters.P;
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
    private final DeleteNoteService deleteNoteService;
    private final ReadNoteService readNoteService;

    public NoteController(ListNoteService listNoteService,
                          CreateNoteService createNoteService,
                          UpdateNoteService updateNoteService,
                          DeleteNoteService deleteNoteService,
                          ReadNoteService readNoteService) {
        this.createNoteService = createNoteService;
        this.readNoteService = readNoteService;
        this.listNoteService = listNoteService;
        this.updateNoteService = updateNoteService;
        this.deleteNoteService = deleteNoteService;
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
    @PreAuthorize("hasAuthority('SCOPE_note:write') and @noteAuthz.hasAccess(#id, #jwt)")
    public ResponseEntity<Void> updateNote(
                                           @P("id") @PathVariable("id") Long id,
                                           @P("jwt") @AuthenticationPrincipal Jwt jwt,
                                           @RequestBody UpdateNoteRequest noteRequest) {
        updateNoteService.updateNote(id, noteRequest);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_note:write') and @noteAuthz.hasAccess(#id, #jwt)")
    public ResponseEntity<Void> deleteNote(@P("id") @PathVariable("id") Long id,
                                           @P("jwt") @AuthenticationPrincipal Jwt jwt) {
        deleteNoteService.deleteNote(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_note:read') and @noteAuthz.hasAccess(#id, #jwt)")
    public ResponseEntity<NoteResponse> getNote(@P("id") @PathVariable("id") Long id,
                                                @P("jwt") @AuthenticationPrincipal Jwt jwt) {
        var note = readNoteService.readNote(id);

        return ResponseEntity.ok(new NoteResponse(note.getId(), note.getTitle(), note.getContent()));
    }

}

package guedes.gustavo.safenotes.repository;

import guedes.gustavo.safenotes.entity.Note;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoteRepository extends JpaRepository<Note, Long> {
    List<Note> findAllByOwnerId(Long ownerId);
}

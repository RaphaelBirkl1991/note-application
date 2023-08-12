package io.getarrays.repository;

import io.getarrays.domain.Note;
import io.getarrays.enumeration.Level;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoteRepo extends JpaRepository<Note,Long> {
    List<Note> findByLevel(Level level);
    void deleteNoteById(Long id);
}

package io.getarrays.service;

import io.getarrays.domain.HttpResponse;
import io.getarrays.domain.Note;
import io.getarrays.enumeration.Level;
import io.getarrays.exception.NoteNotFoundException;
import io.getarrays.repository.NoteRepo;
import io.getarrays.util.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.singletonList;
import static java.util.Optional.ofNullable;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class NoteService {
    private final NoteRepo noteRepo;

    // Method to get all the Notes
    public HttpResponse<Note> getNotes() {
        log.info("Fetching all the Notes from the Database");
        return HttpResponse.<Note>builder()
                .notes(noteRepo.findAll())
                .message(noteRepo.count() > 0 ? noteRepo.count() + "notes retrieved" : "No notes to display")
                .status(OK)
                .statusCode(OK.value())
                .timestamp(LocalDateTime.now().format(DateUtil.dateTimeFormatter()))
                .build();
    }

    // Method to filter Notes
    public HttpResponse<Note> filterNotes(Level level) {
        List<Note> notes = noteRepo.findByLevel(level);
        log.info("Fetching all the notes by Level {}", level);
        return HttpResponse.<Note>builder()
                .notes(notes)
                .message(notes.size() + " notes are of " + level + " importance")
                .status(OK)
                .statusCode(OK.value())
                .timestamp(LocalDateTime.now().format(DateUtil.dateTimeFormatter()))
                .build();
    }

    // Method to save a new Note
    public HttpResponse<Note> saveNote(Note note) {
        log.info("Saving new Note to the Database");
        note.setCreatedAt(LocalDateTime.now());
        return HttpResponse.<Note>builder()
                .notes(singletonList(noteRepo.save(note)))
                .message("Note created successfully")
                .status(CREATED)
                .statusCode(CREATED.value())
                .timestamp(LocalDateTime.now().format(DateUtil.dateTimeFormatter()))
                .build();
    }


    // Method to update a Note
    public HttpResponse<Note> updateNote(Note note) throws NoteNotFoundException {
        log.info("Changing Note in the Database");
        Optional<Note> optionalNote = ofNullable(noteRepo.findById(note.getId())
                .orElseThrow(() -> new NoteNotFoundException("The Note was not found on the Server")));
        Note updateNote = optionalNote.get();
        updateNote.setId(note.getId());
        updateNote.setTitle(note.getTitle());
        updateNote.setDescription(note.getDescription());
        updateNote.setLevel(note.getLevel());
        noteRepo.save(updateNote);
        return HttpResponse.<Note>builder()
                .notes(singletonList(updateNote))
                .message("Note updated successfully")
                .status(OK)
                .statusCode(OK.value())
                .timestamp(LocalDateTime.now().format(DateUtil.dateTimeFormatter()))
                .build();
    }

    // Method to delete a Note
    public HttpResponse<Note> deleteNote(Long id) throws NoteNotFoundException {
        log.info("Deleting Note from the Database by id: {}", id);
        Optional<Note> optionalNote = ofNullable(noteRepo.findById(id)
                .orElseThrow(() -> new NoteNotFoundException("The Note was not found on the Server")));
        optionalNote.ifPresent(noteRepo::delete);
        return HttpResponse.<Note>builder()
                .notes(singletonList(optionalNote.get()))
                .message("Note deleted successfullly")
                .status(OK)
                .statusCode(OK.value())
                .timestamp(LocalDateTime.now().format(DateUtil.dateTimeFormatter()))
                .build();
    }

}

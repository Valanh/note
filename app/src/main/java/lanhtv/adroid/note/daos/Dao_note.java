package lanhtv.adroid.note.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import lanhtv.adroid.note.model.Note;

@Dao
public interface Dao_note{
    @Insert
    void insert(Note note);

    @Delete
    int delete(Note note);

    @Query("UPDATE notes SET title = :title WHERE uid = :uid")
    void updateTitleNoteByUid(int uid, String title);

    @Query("UPDATE notes SET content = :content WHERE uid = :uid")
    void updateContentNoteByUid(int uid, String content);

    @Query("SELECT * FROM notes")
    List<Note> getAllNotes();

    @Query("SELECT * FROM notes WHERE uid = :uid")
    Note findByUid(int uid);
}

package lanhtv.adroid.note.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import lanhtv.adroid.note.daos.Dao_note;
import lanhtv.adroid.note.model.Note;

@Database(entities = {Note.class}, version = 1, exportSchema = false)
public abstract class DatabaseRoom extends RoomDatabase {
    private static final String Database_name = "note.db";
    private static DatabaseRoom instance;

    public static synchronized DatabaseRoom getInstance(Context context){
        if (instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),DatabaseRoom.class,Database_name).allowMainThreadQueries().build();
        }
        return instance;
    }

    public abstract Dao_note Dao();
}

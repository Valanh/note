package lanhtv.adroid.note;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

import lanhtv.adroid.note.adapters.AdapterNote;
import lanhtv.adroid.note.daos.Dao_note;
import lanhtv.adroid.note.database.DatabaseRoom;
import lanhtv.adroid.note.interfaces.Deletenote;
import lanhtv.adroid.note.interfaces.OpenNoteDetail;
import lanhtv.adroid.note.model.Note;

public class MainActivity extends AppCompatActivity implements Deletenote, OpenNoteDetail {

    FloatingActionButton add_note;
    TextInputEditText search_note;
    RecyclerView listView;

    private List<Note> noteList;
    private AdapterNote adapter;
    Context context = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Dao_note dao_note;
        dao_note = DatabaseRoom.getInstance(this).Dao();
        findID();

        //tìm kiếm
        search_note.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        // thêm note
        add_note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(dao_note);
            }
        });
        noteList = new ArrayList<Note>();
        listView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AdapterNote(this, this::deleteNote,this::OpenNoteDetail);
        noteList = dao_note.getAllNotes();
        adapter.setData(noteList);
        listView.setAdapter(adapter);
    }

    void findID() {
        // ánh xạ biến
        add_note = findViewById(R.id.but_add_note);
        search_note = findViewById(R.id.input_text_search);
        listView = findViewById(R.id.list_view_note);
    }

    void showDialog(Dao_note dao) {
        // Tạo dialog builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Inflate layout cho dialog
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.cusstom_dialog_add_note, null);
        builder.setView(dialogView);
        // Tạo dialog từ builder
        AlertDialog dialog = builder.create();
        // Lấy các view từ dialog layout
        TextView tvTitle = dialogView.findViewById(R.id.tvTitle);
        TextInputEditText etInput = dialogView.findViewById(R.id.etInput);
        Button btnOk = dialogView.findViewById(R.id.btnOk);
        Button btnCancel = dialogView.findViewById(R.id.btnCancel);
        // Xử lý sự kiện khi nhấn nút OK
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Note note = new Note();
                note.title = etInput.getText().toString();
                dao.insert(note);
                resfresh(dao);
                dialog.dismiss();
            }
        });
        // Xử lý sự kiện khi nhấn nút Cancel
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        // Hiển thị dialog
        dialog.show();
    }

    @Override
    public void deleteNote(Note note, Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Xóa note")
                .setMessage("Bạn có chắc muốn xóa")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Dao_note dao_note = DatabaseRoom.getInstance(context).Dao();
                        dao_note.delete(note);
                        resfresh(dao_note);
                        dialog.dismiss(); // Đóng dialog
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    void resfresh(Dao_note dao) {
        noteList.clear();
        noteList.addAll(dao.getAllNotes());
        adapter.notifyDataSetChanged();
    }

    @Override
    public void OpenNoteDetail(Note note) {
        Intent intent = new Intent(MainActivity.this, DetailNoteActivity.class);
        intent.putExtra("uid",note.uid+"");
        startActivity(intent);
    }
}
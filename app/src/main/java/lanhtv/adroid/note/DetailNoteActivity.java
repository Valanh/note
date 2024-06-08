package lanhtv.adroid.note;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;


import java.util.Objects;

import lanhtv.adroid.note.daos.Dao_note;
import lanhtv.adroid.note.database.DatabaseRoom;
import lanhtv.adroid.note.model.Note;

public class DetailNoteActivity extends AppCompatActivity {

    TextView edit, title_tv;
    EditText content;
    ImageView back;
    RelativeLayout sc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_note);
        Intent intent = getIntent();
        String uid = intent.getStringExtra("uid");

        Dao_note dao_note = DatabaseRoom.getInstance(this).Dao();
        Note note;
        note = dao_note.findByUid(Integer.parseInt(uid));

        finID();
        title_tv.setText(note.title);
        content.setText(note.content);
        sc.setOnClickListener(view -> {
            content.requestFocus();
            content.setSelection(content.getText().length());
        });

        content.setOnFocusChangeListener((view, b) -> {
            if (content.hasFocus()){
                edit.setText("Done");
            }else {
                edit.setText("Edit");
            }
        });

        edit.setOnClickListener(view -> {
            if (content.hasFocus()) {
                content.clearFocus();
                dao_note.updateContentNoteByUid(Integer.parseInt(uid), content.getText().toString());
            } else {
                showDialog(dao_note, title_tv.getText().toString(), Integer.parseInt(uid));
            }
        });

        back.setOnClickListener(view -> {
            finish();
            Intent intent1 = new Intent(DetailNoteActivity.this, MainActivity.class);
            startActivity(intent1);
        });
    }

    void finID() {
        edit = findViewById(R.id.edit_title_detail);
        title_tv = findViewById(R.id.title_detail_note);
        content = findViewById(R.id.content_note);
        back = findViewById(R.id.back_sc);
        sc = findViewById(R.id.sc_detail);
    }

    void showDialog(Dao_note dao, String title, int uid) {
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

        tvTitle.setText("Sửa tiêu đề");
        etInput.setText(title);
        // Xử lý sự kiện khi nhấn nút OK
        btnOk.setOnClickListener(v -> {
            Note note = new Note();
            note.title = Objects.requireNonNull(etInput.getText()).toString();
            dao.updateTitleNoteByUid(uid, note.title);
            title_tv.setText(note.title);
            dialog.dismiss();
        });
        // Xử lý sự kiện khi nhấn nút Cancel
        btnCancel.setOnClickListener(v -> dialog.dismiss());
        // Hiển thị dialog
        dialog.show();
    }
}
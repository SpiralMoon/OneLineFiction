package com.dsm.onelinefiction;

import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class EditActivity extends AppCompatActivity {

    private Database database;
    private EditText edtTitle;
    private EditText edtContent;
    private Button btnSubmit;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        //load view
        edtTitle = (EditText) findViewById(R.id.edt_title);
        edtContent = (EditText) findViewById(R.id.edt_content);
        btnSubmit = (Button) findViewById(R.id.btn_submit);
        getSupportActionBar().setTitle("의식의 흐름대로 일기 쓰기");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //load databse
        firebaseAuth = FirebaseAuth.getInstance();
        database = Database.getInstance(firebaseAuth.getCurrentUser().getUid());
        firebaseAuth = FirebaseAuth.getInstance();

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Page page = new Page(edtTitle.getText().toString(), edtContent.getText().toString());
                database.createNewBook(firebaseAuth.getCurrentUser().getUid(), page);
                Toast.makeText(EditActivity.this, "의식의 흐름대로 작성한 일기가\n" +
                        "정상적으로 등록되었습니다.", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

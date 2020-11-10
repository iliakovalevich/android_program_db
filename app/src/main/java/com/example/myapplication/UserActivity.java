package com.example.myapplication;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class UserActivity extends AppCompatActivity {

    private EditText nameBox;
    private EditText yearBox;
    private Button delButton;
    private Button saveButton;

    private DatabaseAdapter databaseAdapterdapter;
    private long userId=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        nameBox = (EditText) findViewById(R.id.name);
        yearBox = (EditText) findViewById(R.id.year);
        delButton = (Button) findViewById(R.id.deleteButton);
        saveButton = (Button) findViewById(R.id.saveButton);
        databaseAdapterdapter = new DatabaseAdapter(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            userId = extras.getLong("id");
        }
        // если 0, то добавление
        if (userId > 0) {
            // получаем элемент по id из бд
            databaseAdapterdapter.open();
            User user = databaseAdapterdapter.getUser(userId);
            nameBox.setText(user.getName());
            yearBox.setText(String.valueOf(user.getYear()));
            databaseAdapterdapter.close();
        } else {
            // скрываем кнопку удаления
            delButton.setVisibility(View.GONE);
        }
    }

    public void save(View view){
        String name = nameBox.getText().toString();
        int year = Integer.parseInt(yearBox.getText().toString());
        User user = new User(userId, name, year);

        databaseAdapterdapter.open();
        if (userId > 0) {
            databaseAdapterdapter.update(user);
        } else {
            databaseAdapterdapter.insert(user);
        }
        databaseAdapterdapter.close();
        goHome();
    }
    public void delete(View view){

        databaseAdapterdapter.open();
        databaseAdapterdapter.delete(userId);
        databaseAdapterdapter.close();
        goHome();
    }
    private void goHome(){
        // переход к главной activity
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }
}
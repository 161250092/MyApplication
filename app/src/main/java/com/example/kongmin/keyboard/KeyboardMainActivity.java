package com.example.kongmin.keyboard;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.example.kongmin.myapplication.R;
import android.view.View;

public class KeyboardMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keyboard_main);
        findViewById(R.id.btn_unresolved).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(KeyboardMainActivity.this, UnresolvedActivity.class));
            }
        });
        findViewById(R.id.btn_resolved).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(KeyboardMainActivity.this, ResolvedActivity.class));
            }
        });
    }

}

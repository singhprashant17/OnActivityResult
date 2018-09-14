package com.onactivityresultdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class Main2Activity extends AppCompatActivity {
    public static final int RESULT_CODE1 = 100;
    public static final int RESULT_CODE2 = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);


        final Button button1 = findViewById(R.id.button1);
        final Button button2 = findViewById(R.id.button2);
        final Button button3 = findViewById(R.id.button3);

        button1.setText("resultCode = " + RESULT_CODE1);
        button2.setText("resultCode = " + RESULT_CODE2);
        button3.setText("resultCode (NOT HANDLED)");

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent intent = new Intent();
                intent.putExtra("message", "Message from the future, resultCode = " + RESULT_CODE1);
                setResult(RESULT_CODE1, intent);
                finish();
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent intent = new Intent();
                intent.putExtra("message", "Message from the future, resultCode = " + RESULT_CODE2);
                setResult(RESULT_CODE2, intent);
                finish();
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}

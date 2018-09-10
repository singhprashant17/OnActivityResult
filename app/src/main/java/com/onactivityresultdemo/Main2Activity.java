package com.onactivityresultdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.prashant.onactivityresult.annotation.OnActivityResult;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_CANCELED, new Intent());
                finish();
            }
        });
    }

    @OnActivityResult(requestCode = 2, resultCode = RESULT_OK)
    void action1(Intent data) {
        Toast.makeText(this, "" + data, Toast.LENGTH_SHORT).show();
    }

    @OnActivityResult(requestCode = 3, resultCode = RESULT_OK)
    void action2(Intent data) {
        Toast.makeText(this, "" + data, Toast.LENGTH_SHORT).show();
    }

    @OnActivityResult(requestCode = 4, resultCode = RESULT_OK)
    void action3(Intent data) {
        Toast.makeText(this, "" + data, Toast.LENGTH_SHORT).show();
    }
}

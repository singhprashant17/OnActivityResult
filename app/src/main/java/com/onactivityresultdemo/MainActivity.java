package com.onactivityresultdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.prashant.onactivityresult.ActivityResults;
import com.prashant.onactivityresult.annotation.OnActivityResult;

import static com.onactivityresultdemo.Main2Activity.RESULT_CODE1;
import static com.onactivityresultdemo.Main2Activity.RESULT_CODE2;

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_CODE1 = 1;
    public static final int REQUEST_CODE2 = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button button1 = findViewById(R.id.button1);
        final Button button2 = findViewById(R.id.button2);

        button1.setText("requestCode = " + REQUEST_CODE1);
        button2.setText("requestCode = " + REQUEST_CODE2);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(MainActivity.this, Main2Activity.class), REQUEST_CODE1);
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(MainActivity.this, Main2Activity.class), REQUEST_CODE2);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ActivityResults.hook(this, requestCode, resultCode, data);
    }

    @OnActivityResult(requestCode = REQUEST_CODE1, resultCode = RESULT_CODE1)
    void method1(Intent data) {
        String msg = "requestCode = " + REQUEST_CODE1 + " , resultCode = " + RESULT_CODE1;
        if (data != null) {
            msg += "\n" + data.getStringExtra("message");
        }
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    @OnActivityResult(requestCode = REQUEST_CODE1, resultCode = RESULT_CODE2)
    void method2(Intent data) {
        String msg = "requestCode = " + REQUEST_CODE1 + " , resultCode = " + RESULT_CODE2;
        if (data != null) {
            msg += "\n" + data.getStringExtra("message");
        }
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    @OnActivityResult(requestCode = REQUEST_CODE2, resultCode = RESULT_CODE1)
    void method3(Intent data) {
        String msg = "requestCode = " + REQUEST_CODE2 + " , resultCode = " + RESULT_CODE1;
        if (data != null) {
            msg += "\n" + data.getStringExtra("message");
        }
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    @OnActivityResult(requestCode = REQUEST_CODE2, resultCode = RESULT_CODE2)
    void method4(Intent data) {
        String msg = "requestCode = " + REQUEST_CODE2 + " , resultCode = " + RESULT_CODE2;
        if (data != null) {
            msg += "\n" + data.getStringExtra("message");
        }
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }
}

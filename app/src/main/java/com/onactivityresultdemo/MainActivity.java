package com.onactivityresultdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.prashant.onactivityresult.ActivityResults;
import com.prashant.onactivityresult.annotation.OnActivityResult;

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_CODE = 12;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(MainActivity.this, Main2Activity.class), REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ActivityResults.hook(this, requestCode, resultCode, data);
    }

    @OnActivityResult(resultCode = RESULT_OK, requestCode = REQUEST_CODE)
    void action1(Intent data) {
        Toast.makeText(this, "action1 called with " + data, Toast.LENGTH_SHORT).show();
    }

    @OnActivityResult(resultCode = RESULT_CANCELED, requestCode = REQUEST_CODE)
    void action2(Intent data) {
        Toast.makeText(this, "action2 called with " + data, Toast.LENGTH_SHORT).show();
    }

    @OnActivityResult(resultCode = RESULT_OK, requestCode = REQUEST_CODE)
    void action3(Intent data) {
        Toast.makeText(this, "action3 called with " + data, Toast.LENGTH_SHORT).show();
    }
}

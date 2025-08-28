package io.mastercoding.rvcapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Intents: facilitates communication bet. different components of an app,
        //          as well as bet. different applications.

        // types of intents:
        // 1- Explicit Intents
        Button btn = findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToSecondActivity();
            }
        });

        // 2- Implicit Intents
        Button btn2 = findViewById(R.id.openBrowser);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openEVSCameraHal();
            }
        });

    }
    public void goToSecondActivity(){
        Intent intent = new Intent(this, SecondActivity.class );
        startActivity(intent);
    }

    public void openEVSCameraHal(){
        Uri path = Uri.parse("");

        Intent intent = new Intent(Intent.ACTION_VIEW, path);

        startActivity(intent);

    }

}
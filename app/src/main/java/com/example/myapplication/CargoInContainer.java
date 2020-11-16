package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class CargoInContainer extends AppCompatActivity {
    ConstraintLayout cL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cargo_in_container);
        cL = (ConstraintLayout) findViewById(R.id.constraintLayout);

    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = getIntent();
     //   String id = intent.getStringExtra("objectId");
        cL = (ConstraintLayout) findViewById(R.id.constraintLayout);
        cL.addView(new CargoButton(this,"",975,1375));
    }

    public void OpenCargoPage(View view)
    {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }
}
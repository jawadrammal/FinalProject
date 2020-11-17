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
    protected void onStart()
    {
        super.onStart();
        Intent intent = getIntent();
        boolean addButton = intent.getBooleanExtra("AddToContainer?",false);
        if (addButton==true)
        {
            cL = (ConstraintLayout) findViewById(R.id.constraintLayout);
            for (int i=0;i<MainActivity.CargoList.size();i++)
            {
                Cargo tempCr = MainActivity.CargoList.get(i);
                if (tempCr.Selected==true)
                cL.addView(new CargoButton(this,tempCr.objectid , 975, 1375));
            }
        }
    }

    public void OpenCargoPage(View view)
    {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }
}
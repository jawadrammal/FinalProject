package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CargoInContainer extends AppCompatActivity {
    ConstraintLayout cL;
    public static Button deleteButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cargo_in_container);
        cL = (ConstraintLayout) findViewById(R.id.constraintLayout);
        deleteButton = (Button) findViewById(R.id.deletebutton);
        deleteButton.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        Intent intent = getIntent();
        boolean addButton = intent.getBooleanExtra("AddToContainer?",false);
        if (addButton==true)
        {
            deleteButton.setVisibility(View.INVISIBLE);
            cL = (ConstraintLayout) findViewById(R.id.constraintLayout);
            for (int i=0;i<MainActivity.CargoList.size();i++)
            {
                Cargo tempCr = MainActivity.CargoList.get(i);
                if (tempCr.Selected==true) {
                    CargoButton newButton = new CargoButton(this, tempCr.objectid, 975, 1375);
                    CargoTablePage.buttons.add(newButton);
                    cL.addView(newButton);
                }
            }
        }
    }

    public void OpenCargoPage(View view)
    {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }

    public void deleteCargoButton(View view)
    {
        int i;
        for (i=0;i<CargoTablePage.buttons.size();i++)
        {
            if(CargoTablePage.buttons.get(i).objectId.equals(CargoButton.selected))
            {
                cL.removeView(CargoTablePage.buttons.get(i));
                CargoTablePage.buttons.remove(i);
                i=i-1;
            }

        }
        deleteButton.setVisibility(View.INVISIBLE);
    }
}
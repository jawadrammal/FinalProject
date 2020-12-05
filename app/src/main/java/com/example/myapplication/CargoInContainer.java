package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

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
                    MainActivity.MainInfo.totalWeight+=MainActivity.CargoList.get(i).weight;
                    ((EditText)findViewById((R.id.TotalWeight))).setText("Approximate container weight: " + (int)MainActivity.MainInfo.totalWeight);
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
                for (int j = 0; j < MainActivity.CargoList.size(); j++) {
                    Cargo tempCr = MainActivity.CargoList.get(j);
                    if (tempCr.objectid == CargoTablePage.buttons.get(i).objectId) {
                        MainActivity.MainInfo.totalWeight -= MainActivity.CargoList.get(j).weight;
                        ((EditText) findViewById((R.id.TotalWeight))).setText("Approximate container weight: " + (int) MainActivity.MainInfo.totalWeight);
                    }
                    cL.removeView(CargoTablePage.buttons.get(i));
                    CargoTablePage.buttons.remove(i);
                    i = i - 1;
                }


        }
        deleteButton.setVisibility(View.INVISIBLE);
    }
    
    public void rotate(View view)
    {
        boolean flag=false;
        CargoButton cargoButton = null;
        Cargo tempCr = null;
        for (int i=0;i<MainActivity.CargoList.size();i++) {
            tempCr = MainActivity.CargoList.get(i);
            if (tempCr.Selected == true) {
                for (int j=0;j<CargoTablePage.buttons.size();j++)
                {
                    if (CargoTablePage.buttons.get(i).objectId.equals(tempCr.objectid))
                    {
                        cargoButton= CargoTablePage.buttons.get(i);
                        flag=true;
                        break;
                    }
                }
            }
            if (flag==true)
                break;
        }


        ConstraintLayout.LayoutParams lp = new ConstraintLayout.LayoutParams( cargoButton.getLayoutParams().height,cargoButton.getLayoutParams().width);
        //this.setpa
        lp.setMargins(10, 10, 10, 10);
        cargoButton.setLayoutParams(lp);
        int temp=cargoButton.width1;
        cargoButton.setWidth(cargoButton.length1);
        cargoButton.setLength1(temp);
    }

    public void automaticSolution(View view)
    {
       CargoTablePage.buttons = AutoInitialSolution();
    }

    public ArrayList<CargoButton> autoButtons = new ArrayList<>();
    public ArrayList<CargoButton> AutoInitialSolution( )
    {
       //ConstraintLayout cL2 = (ConstraintLayout) findViewById(R.id.layoutIn);
        int x = 975, y = 1375;
        ArrayList<CargoButton> cargoButtonArrayList =new ArrayList<CargoButton>();
        for (int i=0;i<MainActivity.CargoList.size();i++) {
            Cargo tempCr = MainActivity.CargoList.get(i);

            CargoButton newButton =new CargoButton(this, tempCr.objectid, 0,0);
            Pair<Integer,Integer> coordinates = getSpaceCoordinates(newButton);
            if (coordinates!= null) {
                x = coordinates.first;
                y = coordinates.second;


                autoButtons.add(newButton);
                cL.addView(newButton);
            }

        }
        return autoButtons;
    }
  Pair<Integer, Integer> getSpaceCoordinates(CargoButton newButton)
    {
        float x = 0,y = 0;
        boolean hooffem=false;
        x=newButton.getX();
        y=newButton.getY();
        CargoButton cargoButton1 = null;

        //check space algorithm
        int size=autoButtons.size();
        if (size==0)
            return  new Pair<>(0,0);
       // for (int i=0;i<size;i++)
       //     cargoButton1=autoButtons.get(i);

     //  hooffem = cargoButton1.checkhooffeem(newButton);
       //while (hooffem==true) {
        //for on y
        for (int j=0; j< 456 ; j++) { //for on x
            for (int i = 0; i < 234; i++) {
                if (i+newButton.width1>234)
                    break;
                newButton.setX(i);
                newButton.setY(j);
                for (int k=0;k<size;k++) {
                    cargoButton1=autoButtons.get(k);
                    hooffem = cargoButton1.checkhooffeem(newButton);
                    if (hooffem == true)
                        break;
                }
                if (hooffem == false)
                    break;
            }

            if (hooffem == false)
                break;
        }
           /*if (cargoButton1.getLayoutParams().width + cargoButton1.getX() + 1 < 234) {
               x = newButton.getX() + cargoButton1.getX();
               newButton.setX(x);
           } else {
               newButton.setX(0);
               x = 0;
               y= cargoButton1.getY();
           }*/



        return new Pair<>(1,1);
    }
}
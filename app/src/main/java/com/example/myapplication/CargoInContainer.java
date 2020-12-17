package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Pair;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

public class CargoInContainer extends AppCompatActivity {
    ConstraintLayout cL;
    public static ImageButton deleteButton;
    public static EditText totalWeightText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_cargo_in_container);
        cL = (ConstraintLayout)  findViewById(R.id.constraintLayout);
        deleteButton = (ImageButton) findViewById(R.id.deletebutton);
        deleteButton.setVisibility(View.INVISIBLE);
        ((TextView)findViewById(R.id.DialogView)).setMovementMethod(new ScrollingMovementMethod());
        MainActivity.MainInfo.Dialogbox=((TextView)findViewById(R.id.DialogView));
        CargoTablePage.containerWidth = dpToPx(200, this.getApplicationContext());
        CargoTablePage.containerLength = dpToPx(502, this.getApplicationContext());
        totalWeightText = ((EditText) findViewById((R.id.TotalWeight)));
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    protected void onResume()
    {
        super.onResume();

        Intent intent = getIntent();
        boolean addButton = intent.getBooleanExtra("AddToContainer?", false);

        if (addButton == true)
        {
            deleteButton.setVisibility(View.INVISIBLE);

            cL = (ConstraintLayout) findViewById(R.id.constraintLayout);

            for (int i = 0; i < MainActivity.CargoList.size(); i++)
            {
                Cargo tempCr = MainActivity.CargoList.get(i);
                if (tempCr.Selected == true)
                {
                    if (!(tempCr.isInCargoPage()))
                    {
                        CargoButton newButton = new CargoButton(this, tempCr.objectid, MainActivity.MainInfo.screenWidth * MainActivity.MainInfo.buttonWidthPercentage, MainActivity.MainInfo.screenHeight * MainActivity.MainInfo.buttonHeightPercentage);
                        CargoTablePage.buttons.add(newButton);
                        cL.addView(newButton);
                        tempCr.setInCargoPage(true);
                        MainActivity.MainInfo.totalWeight += MainActivity.CargoList.get(i).weight;
                        ((EditText) findViewById((R.id.TotalWeight))).setText("container weight: " + (int) MainActivity.MainInfo.totalWeight);
                    }
                }
            }
        }

        CargoTablePage.containerX = ((View) findViewById(R.id.Container)).getX(); //44 //44
        CargoTablePage.containerY = ((View) findViewById(R.id.Container)).getY();//517
    }

    @Override
    protected void onNewIntent(Intent intent)
    {
        super.onNewIntent(intent);
        setIntent(intent);
    }


    public void OpenCargoPage(View view)
    {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
       // intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        startActivity(intent);
    }

    public void deleteCargoButton(View view) {
        int i;
        CargoButton temp;

        for (i = 0; i < CargoTablePage.buttons.size(); i++) {
            temp = CargoTablePage.buttons.get(i);
            if (temp.objectId.equals(CargoButton.selected)) {
                if (temp.up.isEmpty()) {
                    if (temp.insideContainer == true) {
                        MainActivity.MainInfo.totalWeight -= temp.cargo.weight;
                        ((EditText) findViewById((R.id.TotalWeight))).setText("container weight: " + (int) MainActivity.MainInfo.totalWeight);
                    }
                    temp.cargo.setInCargoPage(false);
                    if (temp.down != null) {
                        for (int j = 0; j < temp.down.up.size(); j++) {
                            if (temp.down.up.get(j).objectId.equals(temp.objectId)) {
                                temp.down.up.remove(j);
                                j = j - 1;
                            }
                        }
                    }
                    cL.removeView(temp);
                    CargoTablePage.buttons.remove(i);
                    i = i - 1;
                }
                else
                    MainActivity.MainInfo.Dialogbox.setText("Alert!: You are trying to delete an object which has objects on top of it!");
            }
        }
        deleteButton.setVisibility(View.INVISIBLE);
    }

    public void rotate(View view) {
        CargoButton cargoButton = null;

        for (int i = 0; i < CargoTablePage.buttons.size(); i++) {
            cargoButton = CargoTablePage.buttons.get(i);
            if (cargoButton.objectId.equals(CargoButton.selected)) {
                if(cargoButton.up.isEmpty())
               cargoButton.rotate();
                else
                    MainActivity.MainInfo.Dialogbox.setText("Alert!: You are trying to rotate an object which has objects on top of it!");

            }
        }
    }

   /* public void automaticSolution(View view) {
        float x1, y1;

        x1 = ((View) findViewById(R.id.Container)).getX(); //44
        y1 = ((View) findViewById(R.id.Container)).getY();//517

        for (int i = 0; i < MainActivity.CargoList.size(); i++)
        {
            Cargo tempCr = MainActivity.CargoList.get(i);
            if (!(tempCr.isInCargoPage()))
            {
                CargoButton newButton = new CargoButton(this, tempCr.objectid, x1, y1);

                autoMoveButton(newButton);

                MainActivity.MainInfo.totalWeight += newButton.cargo.weight;
                ((EditText) findViewById((R.id.TotalWeight))).setText("container weight: " + (int) MainActivity.MainInfo.totalWeight);

                CargoTablePage.buttons.add(newButton);
                cL.addView(newButton);
                tempCr.setInCargoPage(true);
            }
        }
    }*/


 /*void autoMoveButton(CargoButton newButton) {
     float minY = 0;
     float x = 0, y = 0, x1, y1;
     boolean hooffem = false, first = true, rotated = false, onFloor = false, inOkPlace = false;

     CargoButton cargoButton1 = null;

     x1 = ((View) findViewById(R.id.Container)).getX(); //44
     y1 = ((View) findViewById(R.id.Container)).getY();//517

     CargoTablePage.containerX = ((View) findViewById(R.id.Container)).getX(); //44 //44
     CargoTablePage.containerY = ((View) findViewById(R.id.Container)).getY();//517

     x = newButton.getX();
     y = newButton.getY();

     minY = y1;

     //check space algorithm
     int size = CargoTablePage.buttons.size();

     //for on y
     for (float j = y1; j < dpToPx(502, this.getApplicationContext()) + y1; j++)
     {
         rotated = false;

         //for on x
         for (float i = x1; i < dpToPx(200, this.getApplicationContext()) + x1; i++)
         {
             if (i + newButton.width1 > dpToPx(200, this.getApplicationContext()) + x1)
             {
                    if (rotated==false)
                    {
                        newButton.rotate();
                        i=x1;
                        rotated=true;
                    }
                    else
                    {
                        newButton.rotate();
                        j = minY + 1;
                    }
                    first = true;
                    break;
             }
             if (j + newButton.length1 > dpToPx(502, this.getApplicationContext()) + y1)
                 break;


             newButton.setX(i);
             newButton.setY(j);

             for (int k = 0; k < size; k++)
             {
                 cargoButton1 = CargoTablePage.buttons.get(k);
                 //  if (cargoButton1.objectId.equals(newButton.objectId)==false) {
                 //  if (cargoButton1.objectId!=newButton.objectId){
                 if (cargoButton1!=newButton)
                 {
                    hooffem = cargoButton1.checkhooffeem(newButton);
                    if (hooffem == true)
                    {
                        i = cargoButton1.getX() + cargoButton1.width1;
                        if (first == true)
                        {
                            minY = cargoButton1.getY() + cargoButton1.length1;
                            first = false;
                        }
                        else
                        {
                            if (cargoButton1.getY() + cargoButton1.length1 < minY)
                            minY = (cargoButton1.getY() + cargoButton1.length1);
                        }
                     break;
                    }
                 }
                 //}
             }
             if (hooffem == false) {
                 inOkPlace = true;
                 break;
             }

         }
         if (hooffem == false) {
             inOkPlace = true;
             break;
         }
     }


     if (hooffem == true) {
         //check if we can put it on another cargo
         newButton.setX(x1);
         newButton.setY(y1);

         CargoButton other;
         size = CargoTablePage.buttons.size();
         for (float j = y1; j < dpToPx(502, this.getApplicationContext()) + y1; j++) { //for on x
             rotated = false;
             for (float i = x1; i < dpToPx(200, this.getApplicationContext()) + x1; i++) {
                 if (i + newButton.width1 > dpToPx(200, this.getApplicationContext()) + x1) {
                     j = minY + 1;
                     first = true;
                     break;
                 }
                 if (j + newButton.length1 > dpToPx(502, this.getApplicationContext()) + y1)
                     break;

                 newButton.setX(i);
                 newButton.setY(j);

                 for (int k1 = 0; k1 < size; k1++) {
                     other = CargoTablePage.buttons.get(k1);
                     //if (newButton.objectId.equals(other.objectId)==false) {
                     // if (cargoButton1.objectId!=newButton.objectId){
                     if (newButton.checkhooffeem(other) == true) {
                         //check if we can put it above this cargo
                         if (newButton.canPutOnOther(other) == true) {
                             float oldX = newButton.getX();
                             float oldY = newButton.getY();
                             if(newButton.putItOnOther(other)==true) {
                                 inOkPlace = true;
                                 break;
                             }
                             else{
                                 newButton.setX(oldX);
                                 newButton.setY(oldY);

                                 i = other.getX() + other.width1;
                                 if (first == true) {
                                     minY = other.getY() + other.length1;
                                     first = false;
                                 } else {
                                     if (other.getY() + other.length1 < minY)
                                         minY = (other.getY() + other.length1);
                                 }

                             }
                         }
                     }
                 }
                 if (inOkPlace==true)
                     break;
             }
             if (inOkPlace==true)
                 break;
         }

         //}

     }
     if (inOkPlace == false) {
         newButton.insideContainer=false;
         newButton.setX(MainActivity.MainInfo.screenWidth * MainActivity.MainInfo.buttonWidthPercentage);
         newButton.setY(MainActivity.MainInfo.screenHeight * MainActivity.MainInfo.buttonHeightPercentage);
     }
     else
         newButton.insideContainer=true;
 }*/

    //numeric
    public void automaticSolution(View view) {
        float x1, y1;

        x1 = ((View) findViewById(R.id.Container)).getX(); //44
        y1 = ((View) findViewById(R.id.Container)).getY();//517

        for (int i = 0; i < MainActivity.CargoList.size(); i++)
        {
            Cargo tempCr = MainActivity.CargoList.get(i);
            if (!(tempCr.isInCargoPage()))
            {
                CargoButton newButton = new CargoButton(this, tempCr.objectid, 0, 0);

                autoMoveButton(newButton);

                MainActivity.MainInfo.totalWeight += newButton.cargo.weight;
                ((EditText) findViewById((R.id.TotalWeight))).setText("container weight: " + (int) MainActivity.MainInfo.totalWeight);

                CargoTablePage.buttons.add(newButton);
                cL.addView(newButton);
                tempCr.setInCargoPage(true);
            }
        }
    }


    void autoMoveButton(CargoButton newButton) {
        float minY = 0, minZ=0;
        float x = 0, y = 0, x1, y1;
        boolean hooffem = false, first = true, rotated = false, onFloor = false, inOkPlace = false;

        CargoButton cargoButton1 = null;

        x1 = ((View) findViewById(R.id.Container)).getX(); //44
        y1 = ((View) findViewById(R.id.Container)).getY();//517

        CargoTablePage.containerX = ((View) findViewById(R.id.Container)).getX(); //44 //44
        CargoTablePage.containerY = ((View) findViewById(R.id.Container)).getY();//517

        x = newButton.getX();
        y = newButton.getY();

        minY = 0;

        //check space algorithm
        int size = CargoTablePage.buttons.size();
        float i ,j;
        //for on Z
      //  for (int k=0 ; k<259 ; k++) {
            //for on y
            for (j = 0; j < 586; j++) {
                rotated = false;

                //for on x
                for (i = 0; i < 234; i++) {
                    if (i + newButton.widthInCm > 234) {
                        if (rotated == false) {
                            newButton.rotate();
                            i = -1;
                            rotated = true;
                            continue;
                        }
                        else {
                            newButton.rotate();
                            j = minY + 1;
                        }
                        first = true;
                        break;
                    }
                    if (j + newButton.lengthInCm > 586) {
                        /*if (rotated == false) {
                            newButton.rotate();
                            i = 0;
                            rotated = true;
                            first = true;
                            continue;
                        }*/
                        break;
                    }

                    //check if can put here(float x , float y)
                    newButton.setxInContainer(i);
                    newButton.setyInContainer(j);
                    newButton.setX(x1 + (int) Math.ceil(dpToPx(newButton.xInContainer, getApplicationContext()) * MainActivity.MainInfo.CargoPercentagecontainer));
                    newButton.setY(y1 + (int) Math.ceil(dpToPx(newButton.yInContainer, getApplicationContext()) * MainActivity.MainInfo.CargoPercentagecontainer));

                    //check if not conflict with other if yes move i width and minY length
                    for (int i1 = 0; i1 < size; i1++) {
                        cargoButton1 = CargoTablePage.buttons.get(i1);
                        if (cargoButton1 != newButton) {
                                hooffem = cargoButton1.checkhooffeem(newButton);
                                if (hooffem == true) {
                                    i = cargoButton1.xInContainer + cargoButton1.widthInCm;
                                    if (first == true) {
                                        minY = cargoButton1.yInContainer + cargoButton1.lengthInCm;
                                        first = false;
                                    } else {
                                        if (cargoButton1.yInContainer + cargoButton1.lengthInCm < minY)
                                            minY = (cargoButton1.yInContainer + cargoButton1.lengthInCm);
                                    }
                                    break;
                                }

                        }
                        //}
                    }
                    //if no conlict stop
                    if (hooffem == false) {
                        inOkPlace = true;
                        break;
                    }

                }
                //if no conflict stop
                if (hooffem == false) {
                    inOkPlace = true;
                    break;
                }
            }
        //}

        //if there is a conflict put on other if you can
        //else put it outside the container
        if (hooffem == true) {
            //check if we can put it on another cargo

            CargoButton other;
            size = CargoTablePage.buttons.size();

            for (int k1 = 0; k1 < size; k1++) {
                other = CargoTablePage.buttons.get(k1);
                if (newButton.objectId.equals(other.objectId) == false) {
                    // if (cargoButton1.objectId!=newButton.objectId){
                    //  if (newButton.checkhooffeem(other) == true) {

                    //check if we can put it above this cargo
                    if (newButton.canPutOnOther(other) == true) {
                        //float oldX = newButton.xInContainer;
                        //float oldY = newButton.yInContainer;
                        //try to put it above other
                        if (newButton.AutoPutOnOther(other) == true) {
                            inOkPlace = true;
                            break;
                        } else {
                            //if cant return it to old position
                            //newButton.setxInContainer(oldX);
                            //newButton.setyInContainer(oldY);
                            newButton.insideContainer = false;
                            newButton.setyInContainer(-1);
                            newButton.setxInContainer(-1);
                            newButton.setX(MainActivity.MainInfo.screenWidth * MainActivity.MainInfo.buttonWidthPercentage);
                            newButton.setY(MainActivity.MainInfo.screenHeight * MainActivity.MainInfo.buttonHeightPercentage);
                        }
                    }

                    if (inOkPlace == true)
                        break;
                    //      }
                    //  if (inOkPlace==true)
                    // break;
                }
            }
            //}
        }
        if (inOkPlace == false) {
            newButton.insideContainer=false;
            newButton.setxInContainer(-1);
            newButton.setyInContainer(-1);
            newButton.setX(MainActivity.MainInfo.screenWidth * MainActivity.MainInfo.buttonWidthPercentage);
            newButton.setY(MainActivity.MainInfo.screenHeight * MainActivity.MainInfo.buttonHeightPercentage);
        }
        else
            newButton.insideContainer=true;
    }

    public static int dpToPx(float dp, Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }
}
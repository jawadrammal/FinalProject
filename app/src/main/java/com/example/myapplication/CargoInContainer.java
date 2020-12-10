package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import java.util.ArrayList;

public class CargoInContainer extends AppCompatActivity {
    ConstraintLayout cL;
    public static ImageButton deleteButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cargo_in_container);
        cL = (ConstraintLayout) findViewById(R.id.constraintLayout);
        deleteButton = (ImageButton) findViewById(R.id.deletebutton);
        deleteButton.setVisibility(View.INVISIBLE);
        CargoTablePage.containerWidth = dpToPx(200,this.getApplicationContext());
        CargoTablePage.containerLength = dpToPx(502,this.getApplicationContext());
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
                    CargoButton newButton = new CargoButton(this, tempCr.objectid, MainActivity.MainInfo.screenWidth*MainActivity.MainInfo.buttonWidthPercentage, MainActivity.MainInfo.screenHeight*MainActivity.MainInfo.buttonHeightPercentage);
                    CargoTablePage.buttons.add(newButton);
                    cL.addView(newButton);
                    MainActivity.MainInfo.totalWeight+=MainActivity.CargoList.get(i).weight;
                    ((EditText)findViewById((R.id.TotalWeight))).setText("container weight: " + (int)MainActivity.MainInfo.totalWeight);
                }
            }
        }
        CargoTablePage.containerX = ((View)findViewById(R.id.Container)).getX(); //44 //44
        CargoTablePage.containerY = ((View)findViewById(R.id.Container)).getY();//517
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
                        ((EditText) findViewById((R.id.TotalWeight))).setText("container weight: " + (int) MainActivity.MainInfo.totalWeight);
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
        //lp.setMargins(10, 10, 10, 10);
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
        float x1,y1;
        float x,y;
       x1= ((View)findViewById(R.id.Container)).getX(); //44
       y1= ((View)findViewById(R.id.Container)).getY();//517

        ArrayList<CargoButton> cargoButtonArrayList =new ArrayList<CargoButton>();
        for (int i=0;i<MainActivity.CargoList.size();i++) {
            Cargo tempCr = MainActivity.CargoList.get(i);

            CargoButton newButton =new CargoButton(this, tempCr.objectid, x1,y1);
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
  Pair<Integer, Integer> getSpaceCoordinates(CargoButton newButton) {
      float minY = 0;
      float x = 0, y = 0, x1, y1;
      boolean hooffem = false, first = true, rotated = false, putOnOther = false, onFloor = false, inOkPlace = false;
      x = newButton.getX();
      y = newButton.getY();
      CargoButton cargoButton1 = null;
      x1 = ((View) findViewById(R.id.Container)).getX(); //44
      y1 = ((View) findViewById(R.id.Container)).getY();//517
      CargoTablePage.containerX = ((View) findViewById(R.id.Container)).getX(); //44 //44
      CargoTablePage.containerY = ((View) findViewById(R.id.Container)).getY();//517
      minY = y1;
      //check space algorithm
      int size = autoButtons.size();
      if (size == 0)
          return new Pair<>(0, 0);
      // for (int i=0;i<size;i++)
      //     cargoButton1=autoButtons.get(i);

      //  hooffem = cargoButton1.checkhooffeem(newButton);
      //while (hooffem==true) {
      //for on y
      for (float j = y1; j < dpToPx(502, this.getApplicationContext()) + y1; j++) { //for on x
          rotated = false;
          for (float i = x1; i < dpToPx(200, this.getApplicationContext()) + x1; i++) {
              if (i + newButton.width1 > dpToPx(200, this.getApplicationContext()) + x1) {
                   /* if (rotated==false) {
                        ConstraintLayout.LayoutParams lp = new ConstraintLayout.LayoutParams(newButton.getLayoutParams().height, newButton.getLayoutParams().width);
                        //this.setpa
                        //lp.setMargins(10, 10, 10, 10);
                        newButton.setLayoutParams(lp);
                        int temp = newButton.width1;
                        newButton.setWidth(newButton.length1);
                        newButton.setLength1(temp);
                        i=x1;
                        rotated=true;
                        
                    }*/
                  j = minY + 1;
                  first = true;
                  break;

              }
              if (j + newButton.length1 > dpToPx(502, this.getApplicationContext()) + y1)
                  break;
              newButton.setX(i);
              newButton.setY(j);
              for (int k = 0; k < size; k++) {
                  cargoButton1 = autoButtons.get(k);
                  hooffem = cargoButton1.checkhooffeem(newButton);
                  if (hooffem == true) {
                      i = cargoButton1.getX() + cargoButton1.width1;
                      if (first == true) {
                          minY = cargoButton1.getY() + cargoButton1.length1;
                          first = false;
                      } else {
                          if (cargoButton1.getY() + cargoButton1.length1 < minY)
                              minY = (cargoButton1.getY() + cargoButton1.length1);
                      }

                      break;
                  }
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
           /*if (cargoButton1.getLayoutParams().width + cargoButton1.getX() + 1 < 234) {
               x = newButton.getX() + cargoButton1.getX();
               newButton.setX(x);
           } else {
               newButton.setX(0);
               x = 0;
               y= cargoButton1.getY();
           }*/

      if (hooffem == true) {
          //check if we can put it on another cargo
          newButton.setX(x1);
          newButton.setY(y1);
          CargoButton other;
          size = autoButtons.size();
          for (int k1 = 0; k1 < size; k1++) {
              other = autoButtons.get(k1);
              if (newButton.checkhooffeem(other) == true) {
                  //check if we can put it above this cargo
                  if (newButton.canPutOnOther(other) == true) {
                      newButton.putItOnOther(other);
                      inOkPlace = true;
                      break;
                  }
              }
          }

      }
      if (inOkPlace == false) {
          newButton.setX(MainActivity.MainInfo.screenWidth * MainActivity.MainInfo.buttonWidthPercentage);
          newButton.setY(MainActivity.MainInfo.screenHeight * MainActivity.MainInfo.buttonHeightPercentage);
      }

      return new Pair<>(1, 1);
  }
    public static int dpToPx(float dp, Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }
}
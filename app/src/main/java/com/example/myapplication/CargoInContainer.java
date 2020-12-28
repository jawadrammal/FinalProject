package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.text.method.ScrollingMovementMethod;
import android.util.DisplayMetrics;
import android.util.Pair;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class CargoInContainer extends AppCompatActivity {
    public static ConstraintLayout cL;
    public static ImageButton deleteButton;
    public static EditText totalWeightText;
    public static EditText totalCostText;
    public static EditText totalTimeText;
    public static EditText totalWorkers;
    public static Solution trySolution;
    public static boolean ifImportSolution;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        MainActivity.MainInfo.screenHeight = displayMetrics.heightPixels;
        MainActivity.MainInfo.screenWidth = displayMetrics.widthPixels;
        MainActivity.MainInfo.buttonWidthPercentage= (float) (674.0/1080.0);//px
        MainActivity.MainInfo.buttonHeightPercentage= (float) (927.0/2040.0);//px
        MainActivity.MainInfo.CargoPercentagecontainer = (float) (200.0/234.8); //200 dp - 234.8cm
        MainActivity.MainInfo.ContainerStartX = (float) (56/1440.0);//px
        MainActivity.MainInfo.ContainerStartY = (float) (739/2872.0);//px
        MainActivity.MainInfo.alertWidthPerc = (float) (600/1440.0);//px
        MainActivity.MainInfo.alertHeightPerc = (float) (700/3040.0);//px
        MainActivity.MainInfo.alertXPerc = (float) (1100/1440.0);//px
        MainActivity.MainInfo.alertYPerc = (float) (280/2872.0);//px
        MainActivity.MainInfo.containerViewLength = (float) (1559/2040.0);
        MainActivity.MainInfo.containerViewWidth = (float) (331/1080.0);






        setContentView(R.layout.activity_cargo_in_container);
        cL = (ConstraintLayout) findViewById(R.id.constraintLayout);
        deleteButton = (ImageButton) findViewById(R.id.deletebutton);
        deleteButton.setVisibility(View.INVISIBLE);
        ((TextView) findViewById(R.id.DialogView)).setMovementMethod(new ScrollingMovementMethod());
        MainActivity.MainInfo.Dialogbox = ((TextView) findViewById(R.id.DialogView));
        CargoTablePage.containerWidth = dpToPx(200, this.getApplicationContext());
        CargoTablePage.containerLength = dpToPx(502, this.getApplicationContext());
        totalWeightText = ((EditText) findViewById((R.id.TotalWeight)));
        totalCostText = ((EditText) findViewById((R.id.TotalCost)));
        totalTimeText =  ((EditText) findViewById((R.id.TotalTime)));
        totalWorkers =  ((EditText) findViewById((R.id.TotalWorkers)));
        totalWorkers.setText("Total Workers : " + MainActivity.MainInfo.totalWorkers);

        String path= "/storage/emulated/0/MySolution.txt";
        File file = new File ( path );
        if ( file.exists() )
        {
            try {
                ImportSolutionOnStart("MySolution.txt");
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    protected void onResume() {
        super.onResume();

        Intent intent = getIntent();
        boolean addButton = intent.getBooleanExtra("AddToContainer?", false);

        if (addButton == true) {
            deleteButton.setVisibility(View.INVISIBLE);

            cL = (ConstraintLayout) findViewById(R.id.constraintLayout);

            for (int i = 0; i < MainActivity.CargoList.size(); i++) {
                Cargo tempCr = MainActivity.CargoList.get(i);
                if (tempCr.Selected == true) {
                    if (!(tempCr.isInCargoPage())) {
                        CargoButton newButton = new CargoButton(this, tempCr.objectid, -1,-1);
                        CargoTablePage.buttons.add(newButton);
                        cL.addView(newButton);
                        tempCr.setInCargoPage(true);
                     }
                }
            }
        }
       View container = ((View) findViewById(R.id.Container));
        CargoTablePage.containerX = container.getX(); //44 //44
        CargoTablePage.containerY = container.getY();//517
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    public void deleteAll(View view)
    {
        deleteAllButtons();
    }


    public void SaveSol(View view) throws IOException {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        Solution s1=  new Solution();
                        String file="MySolution.txt";

                        FileOutputStream fileOutputStream = null;
                        try {
                            fileOutputStream=new FileOutputStream(Environment.getExternalStorageDirectory() + "/" + file);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }

                        ObjectOutputStream objectOutputStream = null;
                        try {
                            objectOutputStream = new ObjectOutputStream(fileOutputStream);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            objectOutputStream.writeObject(s1);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            objectOutputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(CargoInContainer.this);
        builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }



    public void deleteAllButtons()
    {
        for (int i= 0; i<CargoTablePage.buttons.size();i++)
        {
            CargoButton b = CargoTablePage.buttons.get(i);

            b.cargo.inCargoPage = false;
            cL.removeView(b);
            CargoTablePage.buttons.remove(b);
            i=i-1;
            if (b.insideContainer == true) {
                MainActivity.MainInfo.totalWeight -= b.cargo.weight;
                ((EditText) findViewById((R.id.TotalWeight))).setText("Container weight: " + (int) MainActivity.MainInfo.totalWeight + "(kg)");
                MainActivity.MainInfo.totalCost -= (b.cargo.cost+MainActivity.MainInfo.ProcessingCost+(1.0/MainActivity.MainInfo.AverageAmountOfBoxes)*MainActivity.MainInfo.OneFullContainerTimeInMinutesPerWorker*(MainActivity.MainInfo.WorkersHourlySalary/60.0)*MainActivity.MainInfo.totalWorkers) ;
                ((EditText) findViewById((R.id.TotalCost))).setText("Container cost: " + (int) MainActivity.MainInfo.totalCost +"(NIS)");
                MainActivity.MainInfo.totalTime -= ((1/MainActivity.MainInfo.AverageAmountOfBoxes*MainActivity.MainInfo.OneFullContainerTimeInMinutesPerWorker)/MainActivity.MainInfo.totalWorkers) ;
                CargoInContainer.totalTimeText.setText("Approximate Time: " + String.format("%.2f", MainActivity.MainInfo.totalTime) +"(H)");
            }

        }
    }
    public void OpenCargoPage(View view) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        // intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        if(ifImportSolution==true) {
            intent.putExtra("importSolution", true);
            ifImportSolution=false;
        }
        else
        {
            intent.putExtra("importSolution", false);
        }
        startActivity(intent);
    }
    public void ProduceReport(View view) {
        Workbook workbook = new HSSFWorkbook();
        Sheet sheet = workbook.createSheet("Container Report"); //Creating a sheet
        Solution s1 = new Solution();
        Cargo c = null;
        int cellindex = 0;
        for (int i = 0; i < s1.buttonsInfo.size() + 1; i++) {
            if (i == 0) {
                Row row = sheet.createRow(i);
                row.createCell(0).setCellValue("Object ID");
                row.createCell(1).setCellValue("Width(cm)");
                row.createCell(2).setCellValue("Height(cm)");
                row.createCell(3).setCellValue("Length(cm)");
                row.createCell(4).setCellValue("Xplacement(cm)");
                row.createCell(5).setCellValue("Yplacement(cm)");
                row.createCell(6).setCellValue("Zplacement(cm)");
                row.createCell(7).setCellValue("Weight(kg)");
                row.createCell(8).setCellValue("Fragile");
                row.createCell(9).setCellValue("Cost(NIS)");
                row.createCell(10).setCellValue("ObjectUnder");
            } else {
                int k=i-1;
                if (s1.buttonsInfo.get(k).insideContainer) {
                    Row row = sheet.createRow(i);
                    for (int j = 0; j < s1.CargoList.size(); j++)
                        if (s1.buttonsInfo.get(k).objectId.equals(s1.CargoList.get(j).objectid))
                            c = s1.CargoList.get(j);
                    row.createCell(0).setCellValue(s1.buttonsInfo.get(k).objectId);
                    row.createCell(1).setCellValue(c.width);
                    row.createCell(2).setCellValue(c.height);
                    row.createCell(3).setCellValue(c.length);
                    row.createCell(4).setCellValue(s1.buttonsInfo.get(k).xInContainer);
                    row.createCell(5).setCellValue(s1.buttonsInfo.get(k).yInContainer);
                    row.createCell(6).setCellValue(s1.buttonsInfo.get(k).z);
                    row.createCell(7).setCellValue(c.weight);
                    row.createCell(9).setCellValue(c.cost);
                    row.createCell(8).setCellValue(c.fragile);
                    row.createCell(10).setCellValue(s1.buttonsInfo.get(k).downObjectId);
                }
            }
        }
        Row MyRow;
        if(sheet.getRow(6)==null)
            MyRow = sheet.createRow(6);
        if(sheet.getRow(7)==null)
            MyRow = sheet.createRow(7);
        if(sheet.getRow(8)==null)
            MyRow = sheet.createRow(8);
        if(sheet.getRow(9)==null)
            MyRow = sheet.createRow(9);
        sheet.getRow(6).createCell(14).setCellValue("Workers Amount:");
        sheet.getRow(6).createCell(15).setCellValue(s1.MainInfo.totalWorkers);
        sheet.getRow(7).createCell(14).setCellValue("Total Weight (kg):");
        sheet.getRow(7).createCell(15).setCellValue(s1.totalWeight);
        sheet.getRow(8).createCell(14).setCellValue("Total Cost (NIS)");
        sheet.getRow(8).createCell(15).setCellValue(s1.totalCost);
        sheet.getRow(9).createCell(14).setCellValue("Approximate Time");
        sheet.getRow(9).createCell(15).setCellValue(s1.totalTime);

      //  sheet.autoSizeColumn(15); not working 28/12/2020
                String fileName = "Container Report.xls"; //Name of the file

                String extStorageDirectory = Environment.getExternalStorageDirectory()
                        .toString();
                File folder = new File(extStorageDirectory, "Report");// Name of the folder you want to keep your file in the local storage.
                folder.mkdir(); //creating the folder
                File file = new File(folder, fileName);
                try {
                    file.createNewFile(); // creating the file inside the folder
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

                try {
                    FileOutputStream fileOut = new FileOutputStream(file); //Opening the file
                    workbook.write(fileOut); //Writing all your row column inside the file
                    fileOut.close(); //closing the file and done

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
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
                        ((EditText) findViewById((R.id.TotalWeight))).setText("Container weight: " + (int) MainActivity.MainInfo.totalWeight + "(kg)");
                        MainActivity.MainInfo.totalCost -= (temp.cargo.cost+MainActivity.MainInfo.ProcessingCost+(1.0/MainActivity.MainInfo.AverageAmountOfBoxes)*MainActivity.MainInfo.OneFullContainerTimeInMinutesPerWorker*(MainActivity.MainInfo.WorkersHourlySalary/60.0)*MainActivity.MainInfo.totalWorkers) ;
                        ((EditText) findViewById((R.id.TotalCost))).setText("Container cost: " + (int) MainActivity.MainInfo.totalCost +"(NIS)");
                        MainActivity.MainInfo.totalTime -= ((1/MainActivity.MainInfo.AverageAmountOfBoxes*MainActivity.MainInfo.OneFullContainerTimeInMinutesPerWorker)/MainActivity.MainInfo.totalWorkers) ;
                        CargoInContainer.totalTimeText.setText("Approximate Time: " + String.format("%.2f", MainActivity.MainInfo.totalTime) +"(H)");
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
                } else
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
                if (cargoButton.up.isEmpty())
                    cargoButton.rotate();
                else
                    MainActivity.MainInfo.Dialogbox.setText("Alert!:You are trying to rotate an  object which has objects on top of it!");

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

        for (int i = 0; i < MainActivity.CargoList.size(); i++) {
            Cargo tempCr = MainActivity.CargoList.get(i);
            if (!(tempCr.isInCargoPage())) {
                CargoButton newButton = new CargoButton(this, tempCr.objectid, 0, 0);

                autoMoveButton(newButton);

                MainActivity.MainInfo.totalWeight += newButton.cargo.weight;
                ((EditText) findViewById((R.id.TotalWeight))).setText("Container weight: " + (int) MainActivity.MainInfo.totalWeight +"(kg)");
                MainActivity.MainInfo.totalCost += newButton.cargo.cost + MainActivity.MainInfo.ProcessingCost + ((1.0/MainActivity.MainInfo.AverageAmountOfBoxes)*MainActivity.MainInfo.OneFullContainerTimeInMinutesPerWorker*(MainActivity.MainInfo.WorkersHourlySalary/60.0)*MainActivity.MainInfo.totalWorkers) ;
                ((EditText) findViewById((R.id.TotalCost))).setText("Container cost: " + (int) MainActivity.MainInfo.totalCost +"(NIS)");
                MainActivity.MainInfo.totalTime += ((1/MainActivity.MainInfo.AverageAmountOfBoxes*MainActivity.MainInfo.OneFullContainerTimeInMinutesPerWorker)/MainActivity.MainInfo.totalWorkers) ;
                CargoInContainer.totalTimeText.setText("Approximate Time: " + String.format("%.2f", MainActivity.MainInfo.totalTime) +"(H)");
                CargoTablePage.buttons.add(newButton);
                cL.addView(newButton);
                tempCr.setInCargoPage(true);
            }
        }
    }


    void autoMoveButton(CargoButton newButton) {
        float minY = 0, minZ = 0;
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
        float i, j;
        //for on Z
        //  for (int k=0 ; k<259 ; k++) {
        //for on y
        for (j = 0; j < 586; j++) {
            rotated = false;

            //for on x
            for (i = 0; i < 234.8; i++) {
                if (i + newButton.widthInCm > 234.8) {
                    if (rotated == false) {
                        newButton.rotate();
                        i = -1;
                        rotated = true;
                        continue;
                    } else {
                        newButton.rotate();
                        j = minY + 1;
                    }
                    first = true;
                    break;
                }
                if (j + newButton.lengthInCm > 586) {
                        if (rotated == false) {
                            newButton.rotate();
                            i = 0;
                            rotated = true;
                            first = true;
                            continue;
                        }
                    break;
                }

                //check if can put here(float x , float y)
                newButton.setxInContainer(i);
                newButton.setyInContainer(j);
                newButton.setX(x1 + (int) Math.ceil(dpToPx(newButton.xInContainer, getApplicationContext()) * MainActivity.MainInfo.CargoPercentagecontainer));
                newButton.setY(y1 + (int) Math.ceil(dpToPx(newButton.yInContainer, getApplicationContext()) * MainActivity.MainInfo.CargoPercentagecontainer));


              //  if(newButton.tryToPutHere(null);


                //check if not conflict with other if yes move i width and minY length
                for (int i1 = 0; i1 < size; i1++) {
                    cargoButton1 = CargoTablePage.buttons.get(i1);
                    if (cargoButton1 != newButton) {
                        if (cargoButton1.insideContainer==true) {
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
            newButton.insideContainer = false;
            newButton.setxInContainer(-1);
            newButton.setyInContainer(-1);
            newButton.setX(MainActivity.MainInfo.screenWidth * MainActivity.MainInfo.buttonWidthPercentage);
            newButton.setY(MainActivity.MainInfo.screenHeight * MainActivity.MainInfo.buttonHeightPercentage);
        } else
            newButton.insideContainer = true;
    }

    public static int dpToPx(float dp, Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }


    public void ImportSolution(View view) throws IOException, ClassNotFoundException {
        cL = (ConstraintLayout) findViewById(R.id.constraintLayout);
            cL.setVisibility(View.VISIBLE);
            View container = ((View) findViewById(R.id.Container));
            CargoTablePage.containerX = container.getX(); //44 //44
            CargoTablePage.containerY = container.getY();//517
            ifImportSolution=true; //56/1040 739/2872
            //  Solution LoadSolution = this.trySolution;

            String file = "solution.txt";
            FileInputStream fileInputStream = new FileInputStream(Environment.getExternalStorageDirectory() + "/" + file);


            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            //Cargo c1=(Cargo)(
            Object o = objectInputStream.readObject();
            objectInputStream.close();
            Solution LoadSolution = ((Solution)o);

        /*Solution s =(Solution)(objectInputStream.readObject());
        objectInputStream.close();

        LoadSolution = s;*/

            // = new Solution();
        /*for (int i = 0; i < LoadSolution.buttons.size(); i++)
            CargoInContainer.cL.addView(LoadSolution.buttons.get(i));
        CargoTablePage.buttons=LoadSolution.buttons;

       /* totalCost.setText("" + LoadSolution.MainInfo.totalCost);
        MainActivity.MainInfo.totalCost = LoadSolution.MainInfo.totalCost;
        totalTime.setText("" + LoadSolution.MainInfo.totalTime);
        MainActivity.MainInfo.totalTime = LoadSolution.MainInfo.totalTime;
        totalWorkers.setText("" + LoadSolution.MainInfo.totalWorkers);
        MainActivity.MainInfo.totalWorkers = LoadSolution.MainInfo.totalWorkers;*/
            //      MainActivity.MainInfo.totalWeight = LoadSolution.MainInfo.totalWeight;
//        totalWeightText.setText("container weight: " + LoadSolution.MainInfo.totalWeight);

            deleteAllButtons();
            MainActivity.MainInfo.totalWeight = LoadSolution.totalWeight;
            totalWeightText.setText("Container weight: " + LoadSolution.totalWeight +"(kg))");
            MainActivity.MainInfo.totalCost += LoadSolution.totalCost;
            ((EditText) findViewById((R.id.TotalCost))).setText("Container cost: " + (int) MainActivity.MainInfo.totalCost +"(NIS)");
            MainActivity.MainInfo.totalTime += LoadSolution.totalTime;
            CargoInContainer.totalTimeText.setText("Approximate Time: " + String.format("%.2f", MainActivity.MainInfo.totalTime)+"(H)");
            MainActivity.CargoList = new ArrayList<>();
            for (Cargo c : LoadSolution.CargoList) {
                MainActivity.CargoList.add(new Cargo(c));
            }

            CargoTablePage.buttons=new ArrayList<>();
            for (CargoButtonInfoForSolution i:LoadSolution.buttonsInfo) {
                CargoButton b =new CargoButton(this,i);
                CargoTablePage.buttons.add(b);

            }
            for (CargoButton b:CargoTablePage.buttons)
            {
                for (Cargo car:MainActivity.CargoList) {
                    if (b.objectId.equals(car.objectid))
                    {
                        b.cargo=car;
                    }
                }

                for (CargoButtonInfoForSolution i:LoadSolution.buttonsInfo)
                {
                    if (b.objectId.equals(i.objectId)) {
                        for (String sId : i.upIds) {
                            for (CargoButton c : CargoTablePage.buttons) {
                                if (sId.equals(c.objectId)) {
                                    b.up.add(c);
                                }
                            }
                        }
                        for (CargoButton c : CargoTablePage.buttons) {
                            if (i.downObjectId!=null) {
                                if (i.downObjectId.equals(c.objectId)) {
                                    b.down = c;
                                }
                            }
                        }
                    }
                }
                CargoInContainer.cL.addView(b);
        }
        for (CargoButton b : CargoTablePage.buttons) {
            if (b.z==0)
            {
               recursiveBringToFront(b);
            }
        }
    }

    public void ImportSolutionOnStart(String FileName) throws IOException, ClassNotFoundException {
        cL = (ConstraintLayout) findViewById(R.id.constraintLayout);
        cL.setVisibility(View.VISIBLE);
        View container = ((View) findViewById(R.id.Container));
        CargoTablePage.containerX = MainActivity.MainInfo.ContainerStartX*MainActivity.MainInfo.screenWidth;
        CargoTablePage.containerY = MainActivity.MainInfo.ContainerStartY*MainActivity.MainInfo.screenHeight;
        ifImportSolution=true;
        //  Solution LoadSolution = this.trySolution;

        String file = FileName;
        FileInputStream fileInputStream = new FileInputStream(Environment.getExternalStorageDirectory() + "/" + file);


        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
        //Cargo c1=(Cargo)(
        Object o = objectInputStream.readObject();
        objectInputStream.close();
        Solution LoadSolution = ((Solution)o);

        /*Solution s =(Solution)(objectInputStream.readObject());
        objectInputStream.close();

        LoadSolution = s;*/

        // = new Solution();
        /*for (int i = 0; i < LoadSolution.buttons.size(); i++)
            CargoInContainer.cL.addView(LoadSolution.buttons.get(i));
        CargoTablePage.buttons=LoadSolution.buttons;

       /* totalCost.setText("" + LoadSolution.MainInfo.totalCost);
        MainActivity.MainInfo.totalCost = LoadSolution.MainInfo.totalCost;
        totalTime.setText("" + LoadSolution.MainInfo.totalTime);
        MainActivity.MainInfo.totalTime = LoadSolution.MainInfo.totalTime;
        totalWorkers.setText("" + LoadSolution.MainInfo.totalWorkers);
        MainActivity.MainInfo.totalWorkers = LoadSolution.MainInfo.totalWorkers;*/
        //      MainActivity.MainInfo.totalWeight = LoadSolution.MainInfo.totalWeight;
//        totalWeightText.setText("container weight: " + LoadSolution.MainInfo.totalWeight);

        deleteAllButtons();
        MainActivity.MainInfo.totalWeight = LoadSolution.totalWeight;
        totalWeightText.setText("Container weight: " + LoadSolution.totalWeight +"(kg))");
        MainActivity.MainInfo.totalCost += LoadSolution.totalCost;
        ((EditText) findViewById((R.id.TotalCost))).setText("Container cost: " + (int) MainActivity.MainInfo.totalCost +"(NIS)");
        MainActivity.MainInfo.totalTime += LoadSolution.totalTime;
        CargoInContainer.totalTimeText.setText("Approximate Time: " + String.format("%.2f", MainActivity.MainInfo.totalTime)+"(H)");
        MainActivity.CargoList = new ArrayList<>();
        for (Cargo c : LoadSolution.CargoList) {
            MainActivity.CargoList.add(new Cargo(c));
        }

        CargoTablePage.buttons=new ArrayList<>();
        for (CargoButtonInfoForSolution i:LoadSolution.buttonsInfo) {
            CargoButton b =new CargoButton(this,i);
            CargoTablePage.buttons.add(b);

        }
        for (CargoButton b:CargoTablePage.buttons)
        {
            for (Cargo car:MainActivity.CargoList) {
                if (b.objectId.equals(car.objectid))
                {
                    b.cargo=car;
                }
            }

            for (CargoButtonInfoForSolution i:LoadSolution.buttonsInfo)
            {
                if (b.objectId.equals(i.objectId)) {
                    for (String sId : i.upIds) {
                        for (CargoButton c : CargoTablePage.buttons) {
                            if (sId.equals(c.objectId)) {
                                b.up.add(c);
                            }
                        }
                    }
                    for (CargoButton c : CargoTablePage.buttons) {
                        if (i.downObjectId!=null) {
                            if (i.downObjectId.equals(c.objectId)) {
                                b.down = c;
                            }
                        }
                    }
                }
            }
            CargoInContainer.cL.addView(b);
        }
        for (CargoButton b : CargoTablePage.buttons) {
            if (b.z==0)
            {
                recursiveBringToFront(b);
            }
        }
    }





    void recursiveBringToFront(CargoButton b)
    {
        b.bringToFront();
        CargoButton c=b;
        if (b.up.isEmpty()!=true)
        {
            for (CargoButton u :b.up) {
                recursiveBringToFront(u);
            }
        }
    }

    public void ExportSolution(View view) throws IOException {

     /*   ArrayList<CargoButton> SaveButtons=new ArrayList<CargoButton>(CargoTablePage.buttons);
        ArrayList<Cargo> SaveCargoList=new ArrayList<>(MainActivity.CargoList);
        Solution ExportSolution = new Solution(SaveCargoList, MainActivity.MainInfo, SaveButtons);
        this.trySolution=ExportSolution;*/
        this.trySolution = new Solution();
        Solution s1= trySolution;
        String file="solution.txt";

        FileOutputStream fileOutputStream ;
        fileOutputStream=new FileOutputStream(Environment.getExternalStorageDirectory() + "/" + file);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(s1);
        objectOutputStream.close();

        }
    //String filename = "MySolution.txt";

   /*     Gson gson = new Gson();
        String s = gson.toJson(v);

        FileOutputStream outputStream;
        try {
            outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(s.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}*/
    public void moveToNearest(View view)
    {
        for (CargoButton c:CargoTablePage.buttons) {
            if(c.objectId.equals(CargoButton.selected))
                moveToNearest(c);
        }
    }

    public void moveToNearest(CargoButton c) {
        boolean flag = false;
        double minXLeft = c.xInContainer, minXRight = 234 - (c.xInContainer + c.widthInCm), minYUp = c.yInContainer, minYDown = 586 - (c.yInContainer + c.lengthInCm);
        double xLeft, xRight, yUp, yDown;
        double temp=(c.xInContainer + c.widthInCm);
        minXRight = (234 - temp);
        boolean noHoffemXCargoToYUp = true, noHoffemXCargoToYDown = true, noHoffemYCargoToXRight = true, noHoffemYCargoToXLeft = true;
        if (c.insideContainer == true && c.up.isEmpty()==true) {

            if (c.down != null) {
                minXLeft = c.xInContainer - c.down.xInContainer;
                minXRight = c.down.xInContainer + c.down.widthInCm - (c.xInContainer + c.widthInCm);
                minYUp = c.yInContainer - c.down.yInContainer;
                minYDown = c.down.yInContainer + c.down.lengthInCm - (c.yInContainer + c.lengthInCm);

            }
            for (CargoButton cb : CargoTablePage.buttons) {
                if (cb != c && cb.insideContainer == true) {
                    if (cb.z == c.z && c.down == cb.down) {
                        if (c.hoffeemX(cb) == true) {
                            if (cb.yInContainer < c.yInContainer) {
                                yUp = c.yInContainer - (cb.yInContainer + cb.lengthInCm);

                                if (yUp < minYUp) {
                                    minYUp = yUp;
                                }

                                noHoffemXCargoToYUp = false;
                            } else {
                                if (cb.yInContainer > c.yInContainer) {
                                    yDown = cb.yInContainer - (c.yInContainer + c.lengthInCm);

                                    if (yDown < minYDown) {
                                        minYDown = yDown;
                                    }

                                    noHoffemXCargoToYDown = false;
                                }
                            }
                        } else {
                            if (c.hoffeemY(cb) == true) {
                                if (cb.xInContainer < c.xInContainer) {
                                    xLeft = c.xInContainer - (cb.xInContainer + cb.widthInCm);

                                    if (xLeft < minXLeft) {
                                        minXLeft = xLeft;
                                    }

                                    noHoffemYCargoToXLeft = false;
                                } else {
                                    if (cb.xInContainer > c.xInContainer) {
                                        xRight = cb.xInContainer - (c.xInContainer + c.widthInCm);
                                        if (xRight < minXRight) {
                                            minXRight = xRight;
                                        }
                                        noHoffemYCargoToXRight = false;
                                    }
                                }
                            }
                        }
                    }


                }
            }


            if (minYUp <= minYDown && (((minYUp <= minXLeft) && (minYUp <= minXRight))||(minXLeft==0||minXRight==0))) {
                c.setyInContainer((float) (c.yInContainer - minYUp));
                c.setY(CargoTablePage.containerY + (int) Math.ceil(dpToPx(c.yInContainer, getApplicationContext()) * MainActivity.MainInfo.CargoPercentagecontainer));

            }

            if (minYDown < minYUp && (((minYDown < minXLeft) && (minYDown < minXRight)) || (minXLeft==0||minXRight==0))) {
                c.setyInContainer((float) (c.yInContainer + minYDown));
                c.setY(CargoTablePage.containerY + (int) Math.ceil(dpToPx(c.yInContainer, getApplicationContext()) * MainActivity.MainInfo.CargoPercentagecontainer));

            }

            if (minXLeft < minXRight && (((minXLeft < minYUp) && (minXLeft < minYDown)) || (minYUp==0||minYDown==0))) {
                c.setxInContainer((float) (c.xInContainer - minXLeft));
                c.setX(CargoTablePage.containerX + (int) Math.ceil(dpToPx(c.xInContainer, getApplicationContext()) * MainActivity.MainInfo.CargoPercentagecontainer));

            }

            if (minXRight < minXLeft && (((minXRight < minYUp) && (minXRight < minYDown)) || (minYUp==0 || minYDown==0))) {
                c.setxInContainer((float) (c.xInContainer + minXRight));
                c.setX(CargoTablePage.containerX + (int) Math.ceil(dpToPx(c.xInContainer, getApplicationContext()) * MainActivity.MainInfo.CargoPercentagecontainer));

            }
        }

    }

}
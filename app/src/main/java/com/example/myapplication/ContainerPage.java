package com.example.myapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.text.InputType;
import android.text.method.ScrollingMovementMethod;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
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
import java.util.Collections;

public class ContainerPage extends AppCompatActivity {
    public static ConstraintLayout cL;
    public static ImageButton deleteButton;
    public static Button RotateButton;
    public static Button MoveToNearestBtn;
    public static TextView totalWeightText;
    public static TextView totalCostText;
    public static TextView totalTimeText;
    public static TextView totalWorkers;
    public static Solution trySolution;
    public String ExportString;
    public static boolean ifImportSolution;
    public static View containerView;
    Intent myFileIntent;
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
        CargoList.MainInfo.screenHeight = displayMetrics.heightPixels;
        CargoList.MainInfo.screenWidth = displayMetrics.widthPixels;
        CargoList.MainInfo.buttonWidthPercentage= (float) (674.0/1080.0);//px
        CargoList.MainInfo.buttonHeightPercentage= (float) (927.0/2040.0);//px
        CargoList.MainInfo.CargoPercentagecontainer = (float) (200.0/234.8); //200 dp - 234.8cm
        CargoList.MainInfo.ContainerStartX = (float) (56/1440.0);//px
        CargoList.MainInfo.ContainerStartY = (float) (739/2872.0);//px
        CargoList.MainInfo.alertWidthPerc = (float) (600/1440.0);//px
        CargoList.MainInfo.alertHeightPerc = (float) (700/3040.0);//px
        CargoList.MainInfo.alertXPerc = (float) (1100/1440.0);//px
        CargoList.MainInfo.alertYPerc = (float) (280/2872.0);//px
        CargoList.MainInfo.containerViewLength = (float) (1559/2040.0);
        CargoList.MainInfo.containerViewWidth = (float) (331/1080.0);






        setContentView(R.layout.activity_cargo_in_container);
        cL = (ConstraintLayout) findViewById(R.id.constraintLayout);
        deleteButton = (ImageButton) findViewById(R.id.deletebutton);
        deleteButton.setVisibility(View.INVISIBLE);
        RotateButton = (Button) findViewById(R.id.button5);
        RotateButton.setVisibility(View.INVISIBLE);
        MoveToNearestBtn = (Button) findViewById(R.id.button7);
        MoveToNearestBtn.setVisibility(View.INVISIBLE);
        ((TextView) findViewById(R.id.DialogView)).setMovementMethod(new ScrollingMovementMethod());
        CargoList.MainInfo.Dialogbox = ((TextView) findViewById(R.id.DialogView));
        ContainerInfo.containerWidth = dpToPx(200, this.getApplicationContext());
        ContainerInfo.containerLength = dpToPx(502, this.getApplicationContext());
        totalWeightText = ((TextView) findViewById((R.id.TotalWeight)));
        totalCostText = ((TextView) findViewById((R.id.TotalCost)));
        totalTimeText =  ((TextView) findViewById((R.id.TotalTime)));
        totalWorkers =  ((TextView) findViewById((R.id.TotalWorkers)));
        totalWorkers.setText("Total Workers : " + CargoList.MainInfo.totalWorkers);


        containerView = (View)findViewById((R.id.Container));
        containerView.post(new Runnable() {
            @Override
            public void run() {
                ContainerInfo.containerX=containerView.getX();
                ContainerInfo.containerY=containerView.getY();
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
        });
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

            for (int i = 0; i < CargoList.CargoList.size(); i++) {
                Cargo tempCr = CargoList.CargoList.get(i);
                if (tempCr.Selected == true) {
                    if (!(tempCr.isInCargoPage())) {
                        CargoButton newButton = new CargoButton(this, tempCr.objectid, -1,-1);
                        ContainerInfo.buttons.add(newButton);
                        TableLayout cargoTable = CargoList.MainInfo.mytable;
                        for (int j =0 ;j<cargoTable.getChildCount();j++)
                        {
                             TextView t=(TextView) (((TableRow)(cargoTable.getChildAt(j))).getChildAt(0));
                             String s =t.getText().toString();
                            if(s.equals(newButton.objectId))
                            {
                                ((TableRow)(cargoTable.getChildAt(j))).setBackgroundColor(Color.BLUE);
                            }

                        }
                        cL.addView(newButton);
                        tempCr.setInCargoPage(true);
                     }
                }
            }
            saveSolution();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }
    public void ChangeWorkersAmount(View view)
    {
        final String[] m_Text = {""};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter Amount of workers");

// Set up the input
        final EditText input = new EditText(this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

// Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                m_Text[0] = input.getText().toString();
                int OldWorkersAmount= CargoList.MainInfo.totalWorkers;
                Double OldTotalTime = CargoList.MainInfo.totalTime;
                CargoList.MainInfo.totalWorkers=Integer.parseInt(input.getText().toString());
                totalWorkers.setText("Total Workers : " + CargoList.MainInfo.totalWorkers);
                CargoList.MainInfo.totalTime= CargoList.MainInfo.totalTime * OldWorkersAmount / CargoList.MainInfo.totalWorkers ;
                CargoList.MainInfo.totalCost = CargoList.MainInfo.totalCost - OldTotalTime * (CargoList.MainInfo.WorkersHourlySalary/60.0) * OldWorkersAmount;
                CargoList.MainInfo.totalCost =  CargoList.MainInfo.totalCost + CargoList.MainInfo.totalWorkers * (CargoList.MainInfo.WorkersHourlySalary/60.0) * CargoList.MainInfo.totalTime;
                ((TextView) findViewById((R.id.TotalCost))).setText("Container cost: " + (int) CargoList.MainInfo.totalCost +"(NIS)");
                ContainerPage.totalTimeText.setText("Approximate Time: " + String.format("%.2f", CargoList.MainInfo.totalTime) +"(H)");
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
    public void deleteAll(View view)
    {

        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        deleteAllButtons();
                        saveSolution();
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(ContainerPage.this);
        builder.setMessage("Are you sure you want to delete all?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();

    }


    public void SaveSol(View view) throws IOException {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                       saveSolution();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(ContainerPage.this);
        builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

    public static void saveSolution()
    {
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
    }

    public void deleteAllButtons()
    {
        TableLayout cargoTable = CargoList.MainInfo.mytable;
        for (int i = 0; i< ContainerInfo.buttons.size(); i++)
        {
            CargoButton b = ContainerInfo.buttons.get(i);

            b.cargo.inCargoPage = false;
            cL.removeView(b);
            ContainerInfo.buttons.remove(b);
            i=i-1;
            if (b.insideContainer == true) {
                CargoList.MainInfo.totalWeight -= b.cargo.weight;
                ((TextView) findViewById((R.id.TotalWeight))).setText("Container weight: " + (int) CargoList.MainInfo.totalWeight + "(kg)");
                CargoList.MainInfo.totalCost -= (b.cargo.cost+ CargoList.MainInfo.ProcessingCost+(1.0/ CargoList.MainInfo.AverageAmountOfBoxes)* CargoList.MainInfo.OneFullContainerTimeInMinutesPerWorker*(CargoList.MainInfo.WorkersHourlySalary/60.0)* CargoList.MainInfo.totalWorkers) ;
                ((TextView) findViewById((R.id.TotalCost))).setText("Container cost: " + (int) CargoList.MainInfo.totalCost +"(NIS)");
                CargoList.MainInfo.totalTime -= ((1/ CargoList.MainInfo.AverageAmountOfBoxes* CargoList.MainInfo.OneFullContainerTimeInMinutesPerWorker)/ CargoList.MainInfo.totalWorkers) ;
                ContainerPage.totalTimeText.setText("Approximate Time: " + String.format("%.2f", CargoList.MainInfo.totalTime) +"(H)");
            }

            for (int j =0 ;j<cargoTable.getChildCount();j++)
            {
                TextView t=(TextView) (((TableRow)(cargoTable.getChildAt(j))).getChildAt(0));

                if(t.getText().toString().equals(b.objectId)) {
                    if (b.cargo.isSelected() == true) {
                        ((TableRow) (cargoTable.getChildAt(j))).setBackgroundColor(Color.LTGRAY);
                    }
                    else {
                        ((TableRow) (cargoTable.getChildAt(j))).setBackgroundColor(Color.TRANSPARENT);
                    }
                }

            }
        }
        CargoList.MainInfo.Dialogbox.setText("");
        deleteButton.setVisibility(View.INVISIBLE);
        RotateButton.setVisibility(View.INVISIBLE);
        MoveToNearestBtn.setVisibility(View.INVISIBLE);
    }
    public void OpenCargoPage(View view) {
        Intent intent = new Intent(getApplicationContext(), CargoList.class);
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
                row.createCell(4).setCellValue("From Xplacement(cm)");
                row.createCell(5).setCellValue("From Yplacement(cm)");
                row.createCell(6).setCellValue("From Zplacement(cm)");
                row.createCell(7).setCellValue("To Xplacement(cm)");
                row.createCell(8).setCellValue("To Yplacement(cm)");
                row.createCell(9).setCellValue("To Zplacement(cm)");
                row.createCell(10).setCellValue("Weight(kg)");
                row.createCell(11).setCellValue("Fragile");
                row.createCell(12).setCellValue("Cost(NIS)");
                row.createCell(13).setCellValue("ObjectUnder");
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
                    row.createCell(7).setCellValue(s1.buttonsInfo.get(k).xInContainer+c.width);
                    row.createCell(8).setCellValue(s1.buttonsInfo.get(k).yInContainer+c.length);
                    row.createCell(9).setCellValue(s1.buttonsInfo.get(k).z+c.height);
                    row.createCell(10).setCellValue(c.weight);
                    row.createCell(11).setCellValue(c.cost);
                    row.createCell(12).setCellValue(c.fragile);
                    row.createCell(13).setCellValue(s1.buttonsInfo.get(k).downObjectId);
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

        AlertDialog alertDialog = new AlertDialog.Builder(ContainerPage.this).create();
        alertDialog.setTitle("Report");
        alertDialog.setMessage("Produced Report successfully!");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }
    public void deleteCargoButton(View view) {
        TableLayout cargoTable = CargoList.MainInfo.mytable;
        int flaggy=0;
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int i;
                CargoButton temp;
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        for (i = 0; i < ContainerInfo.buttons.size(); i++) {
                            temp = ContainerInfo.buttons.get(i);
                            if (temp.objectId.equals(CargoButton.selected)) {
                                if (temp.up.isEmpty()) {
                                    if (temp.insideContainer == true) {
                                        CargoList.MainInfo.totalWeight -= temp.cargo.weight;
                                        ((TextView) findViewById((R.id.TotalWeight))).setText("Container weight: " + (int) CargoList.MainInfo.totalWeight + "(kg)");
                                        CargoList.MainInfo.totalCost -= (temp.cargo.cost+ CargoList.MainInfo.ProcessingCost+(1.0/ CargoList.MainInfo.AverageAmountOfBoxes)* CargoList.MainInfo.OneFullContainerTimeInMinutesPerWorker*(CargoList.MainInfo.WorkersHourlySalary/60.0)* CargoList.MainInfo.totalWorkers) ;
                                        ((TextView) findViewById((R.id.TotalCost))).setText("Container cost: " + (int) CargoList.MainInfo.totalCost +"(NIS)");
                                        CargoList.MainInfo.totalTime -= ((1/ CargoList.MainInfo.AverageAmountOfBoxes* CargoList.MainInfo.OneFullContainerTimeInMinutesPerWorker)/ CargoList.MainInfo.totalWorkers) ;
                                        ContainerPage.totalTimeText.setText("Approximate Time: " + String.format("%.2f", CargoList.MainInfo.totalTime) +"(H)");
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
                                    temp.cargo.inCargoPage=false;
                                    cL.removeView(temp);
                                    ContainerInfo.buttons.remove(i);

                                    for (int j =0 ;j<cargoTable.getChildCount();j++)
                                    {
                                        TextView t=(TextView) (((TableRow)(cargoTable.getChildAt(j))).getChildAt(0));

                                        if(t.getText().toString().equals(temp.objectId)) {
                                            if (temp.cargo.isSelected() == true) {
                                                ((TableRow) (cargoTable.getChildAt(j))).setBackgroundColor(Color.LTGRAY);
                                            }
                                            else {
                                                ((TableRow) (cargoTable.getChildAt(j))).setBackgroundColor(Color.TRANSPARENT);
                                            }
                                        }

                                    }
                                    saveSolution();
                                    deleteButton.setVisibility(View.INVISIBLE);
                                    CargoList.MainInfo.Dialogbox.setText("");
                                    RotateButton.setVisibility(View.INVISIBLE);
                                    MoveToNearestBtn.setVisibility(View.INVISIBLE);
                                    i = i - 1;
                                } else
                                    CargoList.MainInfo.Dialogbox.setText("Alert!: You are trying to delete an object which has objects on top of it!");
                            }
                        }
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(ContainerPage.this);
        builder.setMessage("Are you sure you want to delete this object?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

    public void rotate(View view) {
        CargoButton cargoButton = null;
        boolean canRotate=true;

        for (int i = 0; i < ContainerInfo.buttons.size(); i++) {
            cargoButton = ContainerInfo.buttons.get(i);
            if (cargoButton.objectId.equals(CargoButton.selected)) {
                if (cargoButton.up.isEmpty()) {
                    if(cargoButton.lengthInCm + cargoButton.xInContainer <= 234.8 && cargoButton.widthInCm +cargoButton.yInContainer <= 586) {
                        if(cargoButton.down!=null) {
                            if (cargoButton.lengthInCm + cargoButton.xInContainer <= cargoButton.down.xInContainer + cargoButton.down.widthInCm && cargoButton.widthInCm + cargoButton.yInContainer <= cargoButton.down.yInContainer + cargoButton.down.lengthInCm) {

                            }
                            else
                            {
                                canRotate = false;
                            }
                        }
                        if (canRotate == true) {
                            cargoButton.rotate();
                            for (CargoButton c : ContainerInfo.buttons) {
                                if (cargoButton != c && cargoButton.z == c.z) {

                                    if (cargoButton.checkhooffeem(c)) {
                                        cargoButton.rotate();
                                        canRotate = false;
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    if(canRotate==false)
                    {
                        CargoList.MainInfo.Dialogbox.setText("You can't rotate this object!");
                    }

                }
                else
                    CargoList.MainInfo.Dialogbox.setText("Alert!:You are trying to rotate an  object which has objects on top of it!");
            }
        }
    }

    //numeric
    public void randomSolution(View view)
    {
        deleteAllButtons();
        float x1, y1;

        x1 = ((View) findViewById(R.id.Container)).getX();
        y1 = ((View) findViewById(R.id.Container)).getY();
        ArrayList<Cargo> randomArray = new ArrayList<Cargo>(CargoList.CargoList);

        Collections.shuffle(randomArray);
        for (int i = 0; i < randomArray.size(); i++) {
            Cargo tempCr = randomArray.get(i);
            if (!(tempCr.isInCargoPage())) {
                CargoButton newButton = new CargoButton(this, tempCr.objectid, 0, 0);

                autoMoveButton(newButton);

                CargoList.MainInfo.totalWeight += newButton.cargo.weight;
                ((TextView) findViewById((R.id.TotalWeight))).setText("Container weight: " + (int) CargoList.MainInfo.totalWeight +"(kg)");
                CargoList.MainInfo.totalCost += newButton.cargo.cost + CargoList.MainInfo.ProcessingCost + ((1.0/ CargoList.MainInfo.AverageAmountOfBoxes)* CargoList.MainInfo.OneFullContainerTimeInMinutesPerWorker*(CargoList.MainInfo.WorkersHourlySalary/60.0)* CargoList.MainInfo.totalWorkers) ;
                ((TextView) findViewById((R.id.TotalCost))).setText("Container cost: " + (int) CargoList.MainInfo.totalCost +"(NIS)");
                CargoList.MainInfo.totalTime += ((1/ CargoList.MainInfo.AverageAmountOfBoxes* CargoList.MainInfo.OneFullContainerTimeInMinutesPerWorker)/ CargoList.MainInfo.totalWorkers) ;
                ContainerPage.totalTimeText.setText("Approximate Time: " + String.format("%.2f", CargoList.MainInfo.totalTime) +"(H)");
                ContainerInfo.buttons.add(newButton);
                cL.addView(newButton);
                tempCr.setInCargoPage(true);
                TableLayout cargoTable = CargoList.MainInfo.mytable;
                for (int j =0 ;j<cargoTable.getChildCount();j++)
                {
                    TextView t=(TextView) (((TableRow)(cargoTable.getChildAt(j))).getChildAt(0));
                    String s =t.getText().toString();
                    if(s.equals(newButton.objectId))
                    {
                        ((TableRow)(cargoTable.getChildAt(j))).setBackgroundColor(Color.BLUE);
                    }

                }
            }
        }
        saveSolution();
    }
    public void automaticSolution(View view) {
        float x1, y1;

        x1 = ((View) findViewById(R.id.Container)).getX();
        y1 = ((View) findViewById(R.id.Container)).getY();

        for (int i = 0; i < CargoList.CargoList.size(); i++) {
            Cargo tempCr = CargoList.CargoList.get(i);
            if (!(tempCr.isInCargoPage())) {
                CargoButton newButton = new CargoButton(this, tempCr.objectid, 0, 0);

                autoMoveButton(newButton);

                CargoList.MainInfo.totalWeight += newButton.cargo.weight;
                ((TextView) findViewById((R.id.TotalWeight))).setText("Container weight: " + (int) CargoList.MainInfo.totalWeight +"(kg)");
                CargoList.MainInfo.totalCost += newButton.cargo.cost + CargoList.MainInfo.ProcessingCost + ((1.0/ CargoList.MainInfo.AverageAmountOfBoxes)* CargoList.MainInfo.OneFullContainerTimeInMinutesPerWorker*(CargoList.MainInfo.WorkersHourlySalary/60.0)* CargoList.MainInfo.totalWorkers) ;
                ((TextView) findViewById((R.id.TotalCost))).setText("Container cost: " + (int) CargoList.MainInfo.totalCost +"(NIS)");
                CargoList.MainInfo.totalTime += ((1/ CargoList.MainInfo.AverageAmountOfBoxes* CargoList.MainInfo.OneFullContainerTimeInMinutesPerWorker)/ CargoList.MainInfo.totalWorkers) ;
                ContainerPage.totalTimeText.setText("Approximate Time: " + String.format("%.2f", CargoList.MainInfo.totalTime) +"(H)");
                ContainerInfo.buttons.add(newButton);
                cL.addView(newButton);
                tempCr.setInCargoPage(true);

                TableLayout cargoTable = CargoList.MainInfo.mytable;
                for (int j =0 ;j<cargoTable.getChildCount();j++)
                {
                    TextView t=(TextView) (((TableRow)(cargoTable.getChildAt(j))).getChildAt(0));
                    String s =t.getText().toString();
                    if(s.equals(newButton.objectId))
                    {
                        ((TableRow)(cargoTable.getChildAt(j))).setBackgroundColor(Color.BLUE);
                    }

                }
            }
        }
        saveSolution();
    }


    void autoMoveButton(CargoButton newButton) {
        float minY = 0, minZ = 0;
        float x = 0, y = 0, x1, y1;
        boolean hooffem = false, first = true, rotated = false, onFloor = false, inOkPlace = false;

        CargoButton cargoButton1 = null;

        x1 = ((View) findViewById(R.id.Container)).getX();
        y1 = ((View) findViewById(R.id.Container)).getY();

        ContainerInfo.containerX = ((View) findViewById(R.id.Container)).getX();
        ContainerInfo.containerY = ((View) findViewById(R.id.Container)).getY();

        x = newButton.getX();
        y = newButton.getY();

        minY = 0;

        //check space algorithm
        int size = ContainerInfo.buttons.size();
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
                newButton.setX(x1 + (int) Math.ceil(dpToPx(newButton.xInContainer, getApplicationContext()) * CargoList.MainInfo.CargoPercentagecontainer));
                newButton.setY(y1 + (int) Math.ceil(dpToPx(newButton.yInContainer, getApplicationContext()) * CargoList.MainInfo.CargoPercentagecontainer));


              //  if(newButton.tryToPutHere(null);


                //check if not conflict with other if yes move i width and minY length
                for (int i1 = 0; i1 < size; i1++) {
                    cargoButton1 = ContainerInfo.buttons.get(i1);
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
            size = ContainerInfo.buttons.size();

            for (int k1 = 0; k1 < size; k1++) {
                other = ContainerInfo.buttons.get(k1);
                if (newButton.objectId.equals(other.objectId) == false) {
                    if (newButton.canPutOnOther(other) == true) {
                        if (newButton.AutoPutOnOther(other) == true) {
                            inOkPlace = true;
                            break;
                        } else {
                            newButton.insideContainer = false;
                            newButton.setyInContainer(-1);
                            newButton.setxInContainer(-1);
                            newButton.setX(CargoList.MainInfo.screenWidth * CargoList.MainInfo.buttonWidthPercentage);
                            newButton.setY(CargoList.MainInfo.screenHeight * CargoList.MainInfo.buttonHeightPercentage);
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
            newButton.setX(CargoList.MainInfo.screenWidth * CargoList.MainInfo.buttonWidthPercentage);
            newButton.setY(CargoList.MainInfo.screenHeight * CargoList.MainInfo.buttonHeightPercentage);
        } else
            newButton.insideContainer = true;
    }

    public static int dpToPx(float dp, Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }


    public void ImportSolution(View view) throws IOException, ClassNotFoundException {
            myFileIntent = new Intent(Intent.ACTION_GET_CONTENT);
            myFileIntent.setType("*/*");
             startActivityForResult(myFileIntent,10);
    }
    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        cL = (ConstraintLayout) findViewById(R.id.constraintLayout);
        cL.setVisibility(View.VISIBLE);
        View container = ((View) findViewById(R.id.Container));
        ifImportSolution=true;
        switch (requestCode) {
            case 10:
                if (resultCode == RESULT_OK) {
                    String path = data.getData().getPath();
                    //ExcelFilePath = path;

                    try {
                        File expath = Environment.getExternalStorageDirectory();
                        String newpath = path.replace("/document/primary:", "");
                        FileInputStream MyFile = new FileInputStream(new File((expath + "/" + newpath)));//"/storage/emulated/0/"+newpath));


                        ObjectInputStream objectInputStream = new ObjectInputStream(MyFile);
                        Object o = objectInputStream.readObject();
                        objectInputStream.close();
                        Solution LoadSolution = ((Solution)o);

                        deleteAllButtons();
                        CargoList.MainInfo.totalWeight = LoadSolution.totalWeight;
                        totalWeightText.setText("Container weight: " + LoadSolution.totalWeight +"(kg)");
                        CargoList.MainInfo.totalCost += LoadSolution.totalCost;
                        ((TextView) findViewById((R.id.TotalCost))).setText("Container cost: " + (int) CargoList.MainInfo.totalCost +"(NIS)");
                        CargoList.MainInfo.totalTime += LoadSolution.totalTime;
                        ContainerPage.totalTimeText.setText("Approximate Time: " + String.format("%.2f", CargoList.MainInfo.totalTime)+"(H)");
                        CargoList.CargoList = new ArrayList<>();
                        for (Cargo c : LoadSolution.CargoList) {
                            CargoList.CargoList.add(new Cargo(c));
                        }

                        ContainerInfo.buttons=new ArrayList<>();
                        for (CargoButtonInfo i:LoadSolution.buttonsInfo) {
                            CargoButton b =new CargoButton(this,i);
                            ContainerInfo.buttons.add(b);

                        }
                        for (CargoButton b: ContainerInfo.buttons)
                        {
                            for (Cargo car: CargoList.CargoList) {
                                if (b.objectId.equals(car.objectid))
                                {
                                    b.cargo=car;
                                }
                            }

                            for (CargoButtonInfo i:LoadSolution.buttonsInfo)
                            {
                                if (b.objectId.equals(i.objectId)) {
                                    for (String sId : i.upIds) {
                                        for (CargoButton c : ContainerInfo.buttons) {
                                            if (sId.equals(c.objectId)) {
                                                b.up.add(c);
                                            }
                                        }
                                    }
                                    for (CargoButton c : ContainerInfo.buttons) {
                                        if (i.downObjectId!=null) {
                                            if (i.downObjectId.equals(c.objectId)) {
                                                b.down = c;
                                            }
                                        }
                                    }
                                }
                            }
                            ContainerPage.cL.addView(b);
                        }
                        for (CargoButton b : ContainerInfo.buttons) {
                            if (b.z==0)
                            {
                                recursiveBringToFront(b);
                            }
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    break;
                }
        }
    }



    public void ImportSolutionOnStart(String FileName) throws IOException, ClassNotFoundException {
        cL = (ConstraintLayout) findViewById(R.id.constraintLayout);
        cL.setVisibility(View.VISIBLE);
        View container = ((View) findViewById(R.id.Container));
      //  CargoTablePage.containerX = MainActivity.MainInfo.ContainerStartX*MainActivity.MainInfo.screenWidth;
      //  CargoTablePage.containerY = MainActivity.MainInfo.ContainerStartY*MainActivity.MainInfo.screenHeight;
        ifImportSolution=true;


        String file = FileName;
        FileInputStream fileInputStream = new FileInputStream(Environment.getExternalStorageDirectory() + "/" + file);


        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
        Object o = objectInputStream.readObject();
        objectInputStream.close();
        Solution LoadSolution = ((Solution)o);


        deleteAllButtons();
        CargoList.MainInfo.totalWeight = LoadSolution.totalWeight;
        totalWeightText.setText("Container weight: " + LoadSolution.totalWeight +"(kg)");
        CargoList.MainInfo.totalCost += LoadSolution.totalCost;
        ((TextView) findViewById((R.id.TotalCost))).setText("Container cost: " + (int) CargoList.MainInfo.totalCost +"(NIS)");
        CargoList.MainInfo.totalTime += LoadSolution.totalTime;
        ContainerPage.totalTimeText.setText("Approximate Time: " + String.format("%.2f", CargoList.MainInfo.totalTime)+"(H)");
        CargoList.CargoList = new ArrayList<>();
        for (Cargo c : LoadSolution.CargoList) {
            CargoList.CargoList.add(new Cargo(c));
        }

        ContainerInfo.buttons=new ArrayList<>();
        for (CargoButtonInfo i:LoadSolution.buttonsInfo) {
            CargoButton b =new CargoButton(this,i);
            ContainerInfo.buttons.add(b);

        }
        for (CargoButton b: ContainerInfo.buttons)
        {
            for (Cargo car: CargoList.CargoList) {
                if (b.objectId.equals(car.objectid))
                {
                    b.cargo=car;
                }
            }

            for (CargoButtonInfo i:LoadSolution.buttonsInfo)
            {
                if (b.objectId.equals(i.objectId)) {
                    for (String sId : i.upIds) {
                        for (CargoButton c : ContainerInfo.buttons) {
                            if (sId.equals(c.objectId)) {
                                b.up.add(c);
                            }
                        }
                    }
                    for (CargoButton c : ContainerInfo.buttons) {
                        if (i.downObjectId!=null) {
                            if (i.downObjectId.equals(c.objectId)) {
                                b.down = c;
                            }
                        }
                    }
                }
            }
            ContainerPage.cL.addView(b);
        }
        for (CargoButton b : ContainerInfo.buttons) {
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

        this.trySolution = new Solution();
        Solution s1= trySolution;
        final String[] m_Text = {""};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter File Name:");

// Set up the input
        final EditText input = new EditText(this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

// Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                m_Text[0] = input.getText().toString();
                ExportString=input.getText().toString();
                String file=ExportString+".txt";

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
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();

        }

    public void moveToNearest(View view)
    {
        for (CargoButton c: ContainerInfo.buttons) {
            if(c.objectId.equals(CargoButton.selected))
                moveToNearest(c);
        }
        saveSolution();
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
            for (CargoButton cb : ContainerInfo.buttons) {
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
                c.setY(ContainerInfo.containerY + (int) Math.ceil(dpToPx(c.yInContainer, getApplicationContext()) * CargoList.MainInfo.CargoPercentagecontainer));

            }

            if (minYDown < minYUp && (((minYDown < minXLeft) && (minYDown < minXRight)) || (minXLeft==0||minXRight==0))) {
                c.setyInContainer((float) (c.yInContainer + minYDown));
                c.setY(ContainerInfo.containerY + (int) Math.ceil(dpToPx(c.yInContainer, getApplicationContext()) * CargoList.MainInfo.CargoPercentagecontainer));

            }

            if (minXLeft < minXRight && (((minXLeft < minYUp) && (minXLeft < minYDown)) || (minYUp==0||minYDown==0))) {
                c.setxInContainer((float) (c.xInContainer - minXLeft));
                c.setX(ContainerInfo.containerX + (int) Math.ceil(dpToPx(c.xInContainer, getApplicationContext()) * CargoList.MainInfo.CargoPercentagecontainer));

            }

            if (minXRight < minXLeft && (((minXRight < minYUp) && (minXRight < minYDown)) || (minYUp==0 || minYDown==0))) {
                c.setxInContainer((float) (c.xInContainer + minXRight));
                c.setX(ContainerInfo.containerX + (int) Math.ceil(dpToPx(c.xInContainer, getApplicationContext()) * CargoList.MainInfo.CargoPercentagecontainer));

            }
        }

    }

}
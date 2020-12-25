package com.example.myapplication;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.text.method.ScrollingMovementMethod;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity {

    ConstraintLayout rl;
    public static ArrayList<Cargo> CargoList = new ArrayList<Cargo>();
    public static InfoHolder MainInfo = new InfoHolder();
    int CargoListIndex=0;
    Intent myFileIntent;
    String ExcelFilePath;
    String newpath;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    HSSFWorkbook wb ;
    View addcargototableview;

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), CargoInContainer.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        //intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        intent.putExtra("AddToContainer?",false);
        startActivity(intent);
    }
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
        MainInfo.screenHeight = displayMetrics.heightPixels;
        MainInfo.screenWidth = displayMetrics.widthPixels;
        MainInfo.buttonWidthPercentage= (float) (674.0/1080.0);//px
        MainInfo.buttonHeightPercentage= (float) (927.0/2040.0);//px
        MainInfo.CargoPercentagecontainer = (float) (200.0/234.8); //200 dp - 234.8cm
        MainInfo.ContainerStartX = (float) (44/1080.0);//px
        MainInfo.ContainerStartY = (float) (517/2040.0);//px
        MainInfo.alertWidthPerc = (float) (600/1440.0);//px
        MainInfo.alertHeightPerc = (float) (700/3040.0);//px
        MainInfo.alertXPerc = (float) (1100/1440.0);//px
        MainInfo.alertYPerc = (float) (280/2872.0);//px
        MainInfo.containerViewLength = (float) (1559/2040.0);
        MainInfo.containerViewWidth = (float) (331/1080.0);

        setContentView(R.layout.cargotable);

    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        boolean importSolution = intent.getBooleanExtra("importSolution", false);
        if (importSolution==true) {
            RebuildTable(this.findViewById(android.R.id.content).getRootView());

            Cargo.selectedCnt = 0;
            CargoListIndex=0;
            for (int i = 0; i < CargoList.size(); i++) {
                if (CargoList.get(i).isSelected() == true) ;
                Cargo.selectedCnt++;

                CargoListIndex++;
            }

        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    int xDelta;
    int yDelta;
    @SuppressLint("ClickableViewAccessibility")
    public void DrawObject(View view) {
        Intent intent = new Intent(getApplicationContext(), CargoInContainer.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        //intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        intent.putExtra("AddToContainer?",true);
        startActivity(intent);

    }

    /** Called when the user touches the button */
    public void OpenCargoPage(View view) {

       rl = (ConstraintLayout) getLayoutInflater().inflate(R.layout.layout, null);


        setContentView(R.layout.cargotable);
        RebuildTable(view);
    }
    public void OpenAddWorkerPage(View view) {
        setContentView(R.layout.addworker);
    }
    public void OpenAddcargoPage(View view) {
        setContentView(R.layout.addcargopage);
    }
    public void OpenFirstlayoutPage(View view) {
       // setContentView(R.layout.layout);
        Intent intent = new Intent(getApplicationContext(), CargoInContainer.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        //intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        intent.putExtra("AddToContainer?",false);
        startActivity(intent);

    }
    public void OpenEditcargoPage(View view)
    {
        setContentView(R.layout.editcargopage);
        for(int i=0;i<CargoList.size();i++)
            if(CargoList.get(i).Selected==true)
            {
                ((EditText)findViewById((R.id.ObjectIDTxt1))).setText(CargoList.get(i).objectid);
                ((EditText)findViewById((R.id.HeightTxt1))).setText(CargoList.get(i).height.toString());
                ((EditText)findViewById((R.id.WeightTxt))).setText(CargoList.get(i).weight.toString());
                ((EditText)findViewById((R.id.WidthTxt))).setText(CargoList.get(i).width.toString());
                ((EditText)findViewById((R.id.LengthTxt1))).setText(CargoList.get(i).length.toString());
                ((EditText)findViewById((R.id.ThreshHoldTxt))).setText(CargoList.get(i).WeightThreshold.toString());
                if(CargoList.get(i).fragile==true)
                    ((CheckBox)findViewById(R.id.FragileBox1)).setChecked(true);
                break;
            }
    }

    public void EditData(View view)
    {
        if(isTextEmpty((EditText)findViewById(R.id.ObjectIDTxt1)) || isTextEmpty((EditText)findViewById(R.id.HeightTxt1)) || isTextEmpty((EditText)findViewById(R.id.WidthTxt))|| isTextEmpty((EditText)findViewById(R.id.WeightTxt))|| isTextEmpty((EditText)findViewById(R.id.LengthTxt1))|| isTextEmpty((EditText)findViewById(R.id.ThreshHoldTxt)))
        {
        }
        else {
            for (int i = 0; i < CargoList.size(); i++)
                if (CargoList.get(i).Selected == true) {
                    CargoList.get(i).objectid = ((EditText) findViewById(R.id.ObjectIDTxt1)).getText().toString();
                    CargoList.get(i).height = Double.parseDouble(((EditText) findViewById(R.id.HeightTxt1)).getText().toString());
                    CargoList.get(i).width = Double.parseDouble(((EditText) findViewById(R.id.WidthTxt)).getText().toString());
                    CargoList.get(i).weight = Double.parseDouble(((EditText) findViewById(R.id.WeightTxt)).getText().toString());
                    CargoList.get(i).length = Double.parseDouble(((EditText) findViewById(R.id.LengthTxt1)).getText().toString());
                    CargoList.get(i).WeightThreshold = Double.parseDouble(((EditText) findViewById(R.id.ThreshHoldTxt)).getText().toString());

                    if (((CheckBox) findViewById(R.id.FragileBox1)).isChecked())
                        CargoList.get(i).fragile = true;
                    else
                        CargoList.get(i).fragile = false;

                    CargoList.get(i).Selected = false;
                    Cargo.selectedCnt--;
                }
            RebuildTable(view);
            //updateClickableButtons();
        }
    }
  //  int i=0;
    public void AddCargo(View view) {
        Cargo newCargo = new Cargo();
         if(isTextEmpty((EditText)findViewById(R.id.ObjectIDTxt)) || isTextEmpty((EditText)findViewById(R.id.HeightTxt)) || isTextEmpty((EditText)findViewById(R.id.WidthTxt))|| isTextEmpty((EditText)findViewById(R.id.WeightTxt))|| isTextEmpty((EditText)findViewById(R.id.LengthTxt))|| isTextEmpty((EditText)findViewById(R.id.ThreshHoldTxt)))
         {

         }
         else {
             newCargo.objectid = ((EditText) findViewById(R.id.ObjectIDTxt)).getText().toString();
             newCargo.height = Double.parseDouble(((EditText) findViewById(R.id.HeightTxt)).getText().toString());
             newCargo.width = Double.parseDouble(((EditText) findViewById(R.id.WidthTxt)).getText().toString());
             newCargo.weight = Double.parseDouble(((EditText) findViewById(R.id.WeightTxt)).getText().toString());
             newCargo.length = Double.parseDouble(((EditText) findViewById(R.id.LengthTxt)).getText().toString());
             newCargo.WeightThreshold = Double.parseDouble(((EditText) findViewById(R.id.ThreshHoldTxt)).getText().toString());

             if (((CheckBox) findViewById(R.id.FragileBox)).isChecked())
                 newCargo.fragile = true;
             else
                 newCargo.fragile = false;

             CargoList.add(newCargo);
             AddCargoToTable(view, newCargo);
         }
    }

    public void AddCargoToTable(View view, Cargo newCargo){
        int i=0;
        CargoListIndex++;
        setContentView(R.layout.cargotable);
        TableLayout cargoTable = findViewById(R.id.maintable);
        for(i=0;i<CargoListIndex;i++)
            cargoTable.addView(Createrow(CargoList.get(i)),i+1);
        updateClickableButtons();
    }

    public void uploadDataFile(View view){
        myFileIntent = new Intent(Intent.ACTION_GET_CONTENT);
        myFileIntent.setType("*/*");
        startActivityForResult(myFileIntent,10);
    }
    public void AddToContainer(View view){
        Cargo SelectedCargo = new Cargo();
        for(int i=0;i<CargoList.size();i++)
            if(CargoList.get(i).Selected==true)
            {
                SelectedCargo.objectid = CargoList.get(i).objectid;
                SelectedCargo.height = CargoList.get(i).height;
                SelectedCargo.weight = CargoList.get(i).weight;
                SelectedCargo.width = CargoList.get(i).width;
                SelectedCargo.length = CargoList.get(i).length;
                SelectedCargo.WeightThreshold = CargoList.get(i).WeightThreshold;
                if(CargoList.get(i).fragile==true)
                    SelectedCargo.fragile = true;
            }

    }
    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case 10:
                if (resultCode == RESULT_OK) {
                    String path = data.getData().getPath();
                    ExcelFilePath = path;

                    try {
                        File expath = Environment.getExternalStorageDirectory();
                            newpath = path.replace("/document/primary:", "");
                        FileInputStream excelFile = new FileInputStream(new File((expath + "/" + newpath)));//"/storage/emulated/0/"+newpath));

                        Workbook workbook = new HSSFWorkbook(excelFile);
                        Sheet datatypeSheet = workbook.getSheetAt(0);
                        Iterator<Row> iterator = datatypeSheet.iterator();

                        int rowindex = 0;
                        while (iterator.hasNext()) {
                            Cargo newcargo = new Cargo();
                            Row currentRow = iterator.next();
                            Iterator<Cell> cellIterator = currentRow.iterator();
                            int colindex = 0;
                            while (cellIterator.hasNext()) {
                                Cell currentCell = cellIterator.next();
                                //getCellTypeEnum shown as deprecated for version 3.15
                                //getCellTypeEnum ill be renamed to getCellType starting from version 4.0
                                if (currentCell.getCellType() == Cell.CELL_TYPE_STRING) {
                                    //System.out.print(currentCell.getStringCellValue() + "--");
                                    if (rowindex != 0) {
                                        if (colindex == 0)
                                          newcargo.objectid = currentCell.getStringCellValue();

                                        if (colindex == 6) {
                                            if (currentCell.getStringCellValue().equals("yes")) {
                                                newcargo.fragile = true;
                                            } else {
                                                newcargo.fragile = false;
                                            }
                                        }
                                    }


                                } else {
                                    if (currentCell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                                        //System.out.print(currentCell.getNumericCellValue() + "--");
                                        if (rowindex != 0) {
                                            switch (colindex) {
                                                case 0:
                                                    newcargo.objectid = String.valueOf(currentCell.getNumericCellValue());
                                                    break;
                                                case 1:
                                                    newcargo.height = currentCell.getNumericCellValue();
                                                    break;
                                                case 2:
                                                    newcargo.width = currentCell.getNumericCellValue();
                                                    break;
                                                case 3:
                                                    newcargo.weight = currentCell.getNumericCellValue();
                                                    break;
                                                case 4:
                                                    newcargo.length = currentCell.getNumericCellValue();
                                                    break;
                                                case 5:
                                                    newcargo.WeightThreshold = currentCell.getNumericCellValue();
                                                    break;

                                            }
                                        }
                                    }
                                }
                                colindex++;
                            }

                            System.out.println();
                            if (rowindex != 0) {
                                CargoList.add(newcargo);
                                CargoListIndex++;
                            }
                            rowindex++;
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    RebuildTable(this.findViewById(android.R.id.content).getRootView());
                }
                break;
        }
    }

    public void DeleteRows(View view){
        TableLayout tb = findViewById(R.id.maintable);
        TableRow tr;
        int colorCode = Color.TRANSPARENT;
        TextView ObjectIdView;
        String ObjectId;


        for(int i=1;i<tb.getChildCount();i++) {
            tr =(TableRow)tb.getChildAt(i);
            ObjectIdView = (TextView) tr.getChildAt(0);
            ObjectId=ObjectIdView.getText().toString();

                for(int j=0;j<CargoList.size();j++)
                {
                    if (CargoList.get(j).objectid == ObjectId)
                        if (CargoList.get(j).Selected == true)
                        {
                            Cargo.selectedCnt--;
                            CargoListIndex--;
                            CargoList.remove(j);
                        }
                }

        }

        RebuildTable(view);
      //  updateClickableButtons();
    }
    public TableRow Createrow(Cargo newCargo)
    {
        int i;
        TableRow row = new TableRow(this);
        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
        row.setLayoutParams(lp);

        ArrayList<TextView> texts = new ArrayList<TextView>();

        for(i=0;i<6;i++)
        {
            texts.add(new TextView(this));
            texts.get(i).setTextSize(18);
        }
        texts.get(0).setText(newCargo.objectid);
        row.addView(texts.get(0));
        texts.get(1).setText(fmt(newCargo.height));
        row.addView(texts.get(1));
        texts.get(2).setText(fmt(newCargo.width));
        row.addView(texts.get(2));
        texts.get(3).setText(fmt(newCargo.weight));
        row.addView(texts.get(3));
        texts.get(4).setText(fmt(newCargo.length));
        row.addView(texts.get(4));
        if (newCargo.fragile)
            texts.get(5).setText("yes");
        else
            texts.get(5).setText("no");

        row.addView(texts.get(5));
        if (newCargo.Selected==true)
            row.setBackgroundColor(Color.LTGRAY);
        else
            row.setBackgroundColor(Color.TRANSPARENT);
        row.setClickable(true);
        row.setOnClickListener(tablerowOnClickListener);
        return row;
    }
    public static String fmt(double d)
    {
        if(d == (long) d)
            return String.format("%d",(long)d);
        else
            return String.format("%s",d);
    }

    private View.OnClickListener tablerowOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {

            TextView ObjectIdView;
            String ObjectId;
            TableRow tr1=(TableRow)v;
            ObjectIdView = (TextView) tr1.getChildAt(0);
            ObjectId=ObjectIdView.getText().toString();
            for(int j=0;j<CargoList.size();j++) {
                if (CargoList.get(j).objectid == ObjectId)
                    if (CargoList.get(j).Selected == false)
                    {
                        tr1.setBackgroundColor(Color.LTGRAY);
                        CargoList.get(j).Selected = true;
                        Cargo.selectedCnt++;
                    }
                    else
                        {
                        CargoList.get(j).Selected = false;
                            tr1.setBackgroundColor(Color.TRANSPARENT);
                           Cargo.selectedCnt--;
                    }

            }
            updateClickableButtons();
        }
    };
    public void RebuildTable(View view){
        int i=0;
        setContentView(R.layout.cargotable);
        TableLayout cargoTable = findViewById(R.id.maintable);
        for(i=0;i<CargoList.size();i++)
            cargoTable.addView(Createrow(CargoList.get(i)),i+1);
        updateClickableButtons();
    }
    private boolean isTextEmpty(EditText myeditText) {
        return myeditText.getText().toString().trim().length() == 0;
    }
    public void updateClickableButtons()
    {
        if (Cargo.selectedCnt!=1){
            findViewById(R.id.imageButton6).setClickable(false);
        }
        else{
            findViewById(R.id.imageButton6).setClickable(true);
        }

        if (Cargo.selectedCnt==0){
            findViewById(R.id.imageButton5).setClickable(false);
            findViewById(R.id.button3).setClickable(false);
        }
        else{
            findViewById(R.id.imageButton5).setClickable(true);
            findViewById(R.id.button3).setClickable(true);
        }
    }


}

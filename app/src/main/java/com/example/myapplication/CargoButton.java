package com.example.myapplication;

import android.content.Context;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.ArrayList;
import java.util.Random;

public class CargoButton extends androidx.appcompat.widget.AppCompatButton {
    int r=0,g=0,b=0;
    float x,y;
    String objectId;
    Cargo cargo;
    Random rand;
    ArrayList<CargoButton> up;
    ArrayList<CargoButton> down;
    static String selected = null;
    public CargoButton(Context context) {
        super(context);
      setTouchListener();
    }
    public CargoButton(Context context,String objectId,float x,float y) {
        super(context);
        for (int i=0;i<MainActivity.CargoList.size();i++)
            if (MainActivity.CargoList.get(i).objectid==objectId)
               cargo = MainActivity.CargoList.get(i);

        ConstraintLayout.LayoutParams lp = new ConstraintLayout.LayoutParams(cargo.width.intValue(),cargo.length.intValue());
        //this.setpa
        lp.setMargins(10, 10, 10, 10);
        this.setLayoutParams(lp);
        setTouchListener();
        this.objectId = objectId;
        this.x=x;
        this.y=y;
        this.setX(x);
        this.setY(y);

        this.setText(objectId);

        r = getrandomNum(255);
        b = getrandomNum(255);
        g = getrandomNum(255);
        this.setBackgroundColor(Color.rgb(r,g,b));
        up = new ArrayList<>();
        down = new ArrayList<>();
       // this.setTextColor(Color.rgb((255 - r),(255-g),(255-b)));
        //this.setWidth(20);
        //this.setHeight(20);
    }
    int getrandomNum(int num)
    {
      rand=new Random();
        return rand.nextInt(num);
    }

    public void setXvalue(float x)
    {
        this.x = x;
    }
    public void setYvalue(float y)
    {
        this.y=y;
    }
    public void setTouchListener() {
        this.setOnTouchListener(new View.OnTouchListener() {
            int xDelta;
            int yDelta;
            boolean moved=false;
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                view.bringToFront();
                final int x = (int) event.getRawX();
                final int y = (int) event.getRawY();

                switch (event.getAction() & MotionEvent.ACTION_MASK) {

                    case MotionEvent.ACTION_DOWN:
                        moved=false;

                        xDelta = (int) (x - view.getX());
                        yDelta = (int) (y - view.getY());
                        break;

                    case MotionEvent.ACTION_UP:

                        if (moved==true) {
                            Toast.makeText(getContext(),
                                    "thanks for new location!", Toast.LENGTH_SHORT)
                                    .show();
                            for (int i = 0; i < CargoTablePage.buttons.size(); i++) {
                                float xright, xleft, viewXright, viewXLeft;
                                xleft = CargoTablePage.buttons.get(i).getX();
                                viewXLeft = view.getX();
                                xright =xleft  + CargoTablePage.buttons.get(i).cargo.width.intValue();
                                viewXright =viewXLeft  + cargo.width.intValue();

                                if (CargoTablePage.buttons.get(i).objectId.equals(selected))
                                {
                                   // if ((view.getX() - CargoTablePage.buttons.get(i).getX()))) < (Math.max(CargoTablePage.buttons.get(i).cargo.width.intValue(), cargo.width.intValue())))
                                //||(Math.abs((CargoTablePage.buttons.get(i).getX()+CargoTablePage.buttons.get(i).cargo.width.intValue()) - (view.getX() + cargo.width.intValue()))< CargoTablePage.buttons.get(i).cargo.width.intValue())) {
                                    if((viewXLeft-xleft)<0) {
                                        if (viewXright>xleft) {
                                            Toast.makeText(getContext(),
                                                    "hofifeem", Toast.LENGTH_SHORT)
                                                    .show();
                                        }
                                        else {
                                            Toast.makeText(getContext(),
                                                    "not hofifeem", Toast.LENGTH_SHORT)
                                                    .show();
                                        }
                                    }
                                    else {
                                        if (viewXLeft<xright) {
                                            Toast.makeText(getContext(),
                                                    "hofifeem", Toast.LENGTH_SHORT)
                                                    .show();
                                        }
                                        else {
                                            Toast.makeText(getContext(),
                                                    "not hofifeem", Toast.LENGTH_SHORT)
                                                    .show();
                                        }

                                    }
                                    Toast.makeText(getContext(),
                                            "test1", Toast.LENGTH_SHORT)
                                            .show();
                                }
                            }
                        }
                        else{
                            Toast.makeText(getContext(),
                                    "didn't move!", Toast.LENGTH_SHORT)
                                    .show();
                            selected=objectId;
                           CargoInContainer.deleteButton.setVisibility(VISIBLE);

                        }
                        break;

                    case MotionEvent.ACTION_MOVE:
                        moved=true;
                        view.setX(x - xDelta);
                        view.setY(y - yDelta);
                        setXvalue(x);
                        setYvalue(y);
                        break;
                }

                return true;
            }
        });
    }
}

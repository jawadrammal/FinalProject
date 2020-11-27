package com.example.myapplication;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
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
       /* GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setStroke(0,Color.GREEN);
        drawable.setColor(Color.rgb(r,g,b));
        this.setBackgroundDrawable(drawable);*/
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

    private boolean checkHoffemAxis(float a,float b, float c , float d)
    {
        boolean flag= false;

        if((a-c)<0) {
            if (b > c) {
                flag = true;
            } else {
                flag = false;
            }
        }
            else {
                if (a<d)
                {
                    flag=true;
                }
                else {
                    flag= false;
                }
            }

        return flag;
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
                        boolean hoffemX=false;
                        boolean hoffemY = false;
                        if (moved==true) {
                            Toast.makeText(getContext(),
                                    "thanks for new location!", Toast.LENGTH_SHORT)
                                    .show();
                            for (int i = 0; i < CargoTablePage.buttons.size(); i++) {
                                float xright, xleft, viewXright, viewXLeft;
                                xleft = CargoTablePage.buttons.get(i).getX();
                                viewXLeft = view.getX();
                                xright =xleft  + CargoTablePage.buttons.get(i).getLayoutParams().width;
                                viewXright =viewXLeft  + view.getLayoutParams().width;
                                float yUp, yDown, viewYup, viewYDown;
                                yUp = CargoTablePage.buttons.get(i).getY();
                                viewYup = view.getY();
                                yDown =yUp  + CargoTablePage.buttons.get(i).getLayoutParams().height;
                                viewYDown =viewYup  + view.getLayoutParams().height;

                                if (CargoTablePage.buttons.get(i).objectId.equals(selected))
                                {
                                   // if ((view.getX() - CargoTablePage.buttons.get(i).getX()))) < (Math.max(CargoTablePage.buttons.get(i).cargo.width.intValue(), cargo.width.intValue())))
                                //||(Math.abs((CargoTablePage.buttons.get(i).getX()+CargoTablePage.buttons.get(i).cargo.width.intValue()) - (view.getX() + cargo.width.intValue()))< CargoTablePage.buttons.get(i).cargo.width.intValue())) {
                                    if((viewXLeft-xleft)<0) {
                                        if (viewXright>xleft) {
                                            Toast.makeText(getContext(),
                                                    "hofifeemX", Toast.LENGTH_SHORT)
                                                    .show();
                                            hoffemX=true;
                                        }
                                        else {
                                            Toast.makeText(getContext(),
                                                    "not hofifeemX", Toast.LENGTH_SHORT)
                                                    .show();
                                                    hoffemX=false;
                                        }
                                    }
                                    else {
                                        if (viewXLeft<xright) {
                                            Toast.makeText(getContext(),
                                                    "hofifeemX", Toast.LENGTH_SHORT)
                                                    .show();
                                            hoffemX=true;
                                        }
                                        else {
                                            Toast.makeText(getContext(),
                                                    "not hofifeemX", Toast.LENGTH_SHORT)
                                                    .show();
                                            hoffemX=false;
                                        }

                                    }
                                    hoffemX= checkHoffemAxis(viewXLeft,viewXright,xleft,xright);
                                    hoffemY = checkHoffemAxis(viewYup,viewYDown,yUp,yDown);
                                    if(hoffemX==true&&hoffemY==true)
                                    {
                                        Toast.makeText(getContext(),
                                                "hoffeem X and Y", Toast.LENGTH_SHORT)
                                                .show();
                                    }
                                    Toast.makeText(getContext(),
                                            "test1", Toast.LENGTH_SHORT)
                                            .show();
                                }
                            }


                           /* GradientDrawable drawable = new GradientDrawable();
                            drawable.setShape(GradientDrawable.RECTANGLE);
                            drawable.setStroke(0,Color.rgb(r,g,b));
                            drawable.setColor(Color.rgb(r,g,b));
                            view.setBackgroundDrawable(drawable);*/



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
                        /*GradientDrawable drawable = new GradientDrawable();
                        drawable.setShape(GradientDrawable.RECTANGLE);
                        drawable.setStroke(5,Color.GREEN);
                        drawable.setColor(Color.rgb(r,g,b));
                        view.setBackgroundDrawable(drawable);*/
                        break;
                }

                return true;
            }
        });
    }
}

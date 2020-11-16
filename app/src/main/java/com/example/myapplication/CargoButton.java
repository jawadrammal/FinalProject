package com.example.myapplication;

import android.content.Context;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

public class CargoButton extends androidx.appcompat.widget.AppCompatButton {
    static int r=0,g=0,b=0;
    float x,y;
    String objectId;

    public CargoButton(Context context) {
        super(context);
      setTouchListener();
    }
    public CargoButton(Context context,String objectId,float x,float y) {
        super(context);
        ConstraintLayout.LayoutParams lp = new ConstraintLayout.LayoutParams(100,100);
        this.setLayoutParams(lp);
        setTouchListener();
        this.objectId = objectId;
        this.x=x;
        this.y=y;
        this.setX(x);
        this.setY(y);
        this.setBackgroundColor(Color.rgb(r,g,b));
        r+=10;
        b+=10;
        g+=10;
        //this.setWidth(20);
        //this.setHeight(20);
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

            @Override
            public boolean onTouch(View view, MotionEvent event) {
                view.bringToFront();
                final int x = (int) event.getRawX();
                final int y = (int) event.getRawY();

                switch (event.getAction() & MotionEvent.ACTION_MASK) {

                    case MotionEvent.ACTION_DOWN:


                        xDelta = (int) (x - view.getX());
                        yDelta = (int) (y - view.getY());
                        break;

                    case MotionEvent.ACTION_UP:
                        Toast.makeText(getContext(),
                                "thanks for new location!", Toast.LENGTH_SHORT)
                                .show();
                        break;

                    case MotionEvent.ACTION_MOVE:

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

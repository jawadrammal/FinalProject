package com.example.myapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.ArrayList;
import java.util.Random;

public class CargoButton extends androidx.appcompat.widget.AppCompatButton {

    static String selected = null;

    String objectId;
    Cargo cargo;
    int width1, length1;
    float xInContainer, yInContainer;
    double z=0;
    ArrayList<CargoButton> up;
    CargoButton down;
    boolean insideContainer = false;

    int r = 0, g = 0, b = 0;
    Random rand;
    PopupWindow popUp;
    boolean click=true;

    public CargoButton(Context context) {
        super(context);
        setTouchListener();
    }

    public CargoButton(Context context, String objectId, float xInContainer, float yInContainer)
    {
        super(context);

        this.objectId = objectId;

        this.xInContainer = xInContainer;
        this.yInContainer = yInContainer;

        this.setX(xInContainer);
        this.setY(yInContainer);

        popUp = new PopupWindow();
        this.setText(objectId);
        up = new ArrayList<>();

        //get the cargo with objectId
        for (int i = 0; i < MainActivity.CargoList.size(); i++)
            if (MainActivity.CargoList.get(i).objectid == objectId)
                cargo = MainActivity.CargoList.get(i);

        //set layoutparams for the CargoButton in the Layout
        width1 = (int) Math.ceil(dpToPx(cargo.width.floatValue(), this.getContext()) * MainActivity.MainInfo.CargoPercentagecontainer);
        length1 = (int) Math.ceil(dpToPx(cargo.length.floatValue(), this.getContext()) * MainActivity.MainInfo.CargoPercentagecontainer);
        ConstraintLayout.LayoutParams lp = new ConstraintLayout.LayoutParams(width1, length1);
        this.setLayoutParams(lp);

        //set onTouch listener
        setTouchListener();


        //generate randomized color
        r = getRandomNum(255);
        b = getRandomNum(255);
        g = getRandomNum(255);
        this.setBackgroundColor(Color.rgb(r, g, b));

       /* GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setStroke(0,Color.GREEN);
        drawable.setColor(Color.rgb(r,g,b));
        this.setBackgroundDrawable(drawable);*/
    }

    public boolean canPutOnOther(CargoButton other) {
        if (((this.width1 * this.length1) <= (other.length1 * other.width1))&&(other.z+other.cargo.height+this.cargo.height)<InfoHolder.containerHeight)
            return true;
        return false;
    }

    public boolean putItOnOther(CargoButton other) {
        boolean hoffeem=false;
        if (other.up.isEmpty()) {
            this.setX(other.getX());
            this.setY(other.getY());
            if (this.width1 > other.width1) {
                this.rotate();
            }
            //other.up.add(this);
            if (down!=null)
            {
                if (down.up.isEmpty()!= true) {
                    for(int i=0;i<down.up.size();i++)
                        if (down.up.get(i).objectId.equals(this.objectId))
                        {

                            down.up.remove(i);
                            break;
                        }
                    z-=down.cargo.height;
                }
            }
            down = other;
            down.up.add(this);
            z=(down.z+down.cargo.height);
            return true;
        }
        else
        {
            for (int i=0;i<other.up.size();i++)
            {
                if (checkhooffeem(other.up.get(i))) {
                    if(canPutOnOther(other.up.get(i))) {
                       if (putItOnOther(other.up.get(i))==true) {
                            return true;
                       }
                    }
                }
            }
        }

      return false;

    }

    public boolean checkhooffeem(CargoButton other)
    {
        boolean hoffemY, hoffemX;
        float xright, xleft, viewXright, viewXLeft;
        float yUp, yDown, viewYup, viewYDown;

        xleft = other.getX();
        viewXLeft = this.getX();
        xright = xleft + other.getLayoutParams().width;
        viewXright = viewXLeft + this.getLayoutParams().width;

        yUp = other.getY();
        viewYup = this.getY();
        yDown = yUp + other.getLayoutParams().height;
        viewYDown = viewYup + this.getLayoutParams().height;

        hoffemX = checkHoffemAxis(viewXLeft, viewXright, xleft, xright);
        hoffemY = checkHoffemAxis(viewYup, viewYDown, yUp, yDown);

        if (hoffemX == true && hoffemY == true) {
            return true;
        }
        return false;
    }

    boolean ifInContainer() {
        return checkIfInContainer(this.getX(),this.getY(),this.width1,this.length1);
    }
    static boolean checkIfInContainer(float x , float y , float width , float length) {
        if ((x >= CargoTablePage.containerX) && ((x + width) <= (CargoTablePage.containerX + CargoTablePage.containerWidth)) && (y >= CargoTablePage.containerY) && ((y + length) <= (CargoTablePage.containerY + CargoTablePage.containerLength))) {
            return true;
        } else return false;
    }

    public void rotate()
    {
        ConstraintLayout.LayoutParams lp = new ConstraintLayout.LayoutParams(this.getLayoutParams().height, this.getLayoutParams().width);
        //this.setpa
        //lp.setMargins(10, 10, 10, 10);
        this.setLayoutParams(lp);
        int temp = this.width1;
        this.setWidth1(this.length1);
        this.setLength1(temp);
    }


    public void setTouchListener() {
        this.setOnTouchListener(new View.OnTouchListener() {
            int xDelta;
            int yDelta;
            boolean moved = false,hoffeem=false;

            @Override
            public boolean onTouch(View view, MotionEvent event) {
                view.bringToFront();
                final int x = (int) event.getRawX();
                final int y = (int) event.getRawY();
                hoffeem=false;
                switch (event.getAction() & MotionEvent.ACTION_MASK) {

                    case MotionEvent.ACTION_DOWN:
                        moved = false;

                        xDelta = (int) (x - view.getX());
                        yDelta = (int) (y - view.getY());
                        selected=objectId;
                        CargoInContainer.deleteButton.setVisibility(VISIBLE);
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        //builder.setTitle("id:" + cargo.objectid);

                        builder.setMessage("id:" + cargo.objectid + "\n" + "height:" + cargo.height + "\n" + "width:" + cargo.width + "\n" + "length:" + cargo.length + "\n" + "x:" + view.getX() + "\n" + "y:" + view.getY() + "\n" + "z:" + z + "\n" );
                        AlertDialog dialog = builder.create();
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();
                        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                        wmlp.gravity = Gravity.TOP | Gravity.LEFT;
                        wmlp.x =  (int) Math.ceil(MainActivity.MainInfo.screenWidth*MainActivity.MainInfo.alertXPerc);   //x position
                        wmlp.y = (int) Math.ceil(MainActivity.MainInfo.screenHeight*MainActivity.MainInfo.alertYPerc); ;   //y position
                        dialog.show();
                        dialog.getWindow().setLayout((int) Math.ceil(MainActivity.MainInfo.screenWidth*MainActivity.MainInfo.alertWidthPerc),(int) Math.ceil(MainActivity.MainInfo.screenHeight*MainActivity.MainInfo.alertHeightPerc));

                        break;

                    case MotionEvent.ACTION_UP:
                        boolean hoffemX = false;
                        boolean hoffemY = false;
                        if (moved == true) {
                            Toast.makeText(getContext(),
                                    "thanks for new location!", Toast.LENGTH_SHORT)
                                    .show();

                           /* GradientDrawable drawable = new GradientDrawable();
                            drawable.setShape(GradientDrawable.RECTANGLE);
                            drawable.setStroke(0,Color.rgb(r,g,b));
                            drawable.setColor(Color.rgb(r,g,b));
                            view.setBackgroundDrawable(drawable);*/
                            if(checkIfInContainer(view.getX(),view.getY(),view.getWidth(),view.getHeight())==true) {

                                CargoButton b, v = ((CargoButton) view);

                                for (int i = 0; i < CargoTablePage.buttons.size(); i++) {

                                    b = CargoTablePage.buttons.get(i);
                                    if (v != b) {
                                        if (v.checkhooffeem(b) == true) {
                                            hoffeem = true;
                                            if (v.canPutOnOther(b) == true) {
                                                v.putItOnOther(b);
                                                //v.down.up.remove(v);
                                                // v.down = b;
                                                // b.up.add(v);
                                                break;
                                            }
                                        }
                                    }
                                }
                                if (hoffeem == false) {
                                    if (down != null) {
                                        if (down.up != null)
                                            if (down.up.isEmpty() == false) {
                                                for(int i=0;i<down.up.size();i++)
                                                    if (down.up.get(i).objectId.equals(v.objectId))
                                                    {
                                                        z-=down.cargo.height;
                                                        down.up.remove(i);

                                                        break;
                                                    }
                                            }
                                        down = null;
                                            z=0;
                                    }

                                }
                                if (insideContainer == false) {
                                    MainActivity.MainInfo.totalWeight += cargo.weight;
                                    CargoInContainer.totalWeightText.setText("container weight: " + (int) MainActivity.MainInfo.totalWeight);
                                    insideContainer = true;
                                }
                            }
                            else
                            {

                                //down = null
                                //remove from up
                                Toast.makeText(getContext(),
                                        "out of the container", Toast.LENGTH_SHORT)
                                        .show();
                                if (insideContainer==true) {
                                    MainActivity.MainInfo.totalWeight -= cargo.weight;
                                    CargoInContainer.totalWeightText.setText("container weight: " + (int) MainActivity.MainInfo.totalWeight);
                                    insideContainer = false;
                                }

                                view.setX(MainActivity.MainInfo.screenWidth*MainActivity.MainInfo.buttonWidthPercentage);
                                view.setY(MainActivity.MainInfo.screenHeight*MainActivity.MainInfo.buttonHeightPercentage);
                               // down.up.remove(this);
                                if (down!=null) {
                                    if (down.up != null)
                                        if (down.up.isEmpty() == false) {
                                            for(int i=0;i<down.up.size();i++)
                                                if (down.up.get(i).objectId.equals(((CargoButton)view).objectId))
                                                {
                                                    z-=down.cargo.height;
                                                    down.up.remove(i);

                                                    break;
                                                }
                                        }
                                    down = null;

                                }
                                z=0;
                            }

                        } else {
                            Toast.makeText(getContext(),
                                    "didn't move!", Toast.LENGTH_SHORT)
                                    .show();
                           }
                        break;

                    case MotionEvent.ACTION_MOVE:
                        moved=true;
                        if(y-yDelta>MainActivity.MainInfo.ContainerStartY*MainActivity.MainInfo.screenHeight)
                        {
                            view.setX(x - xDelta);
                            view.setY(y - yDelta);
                            setxInContainer(x);
                            setyInContainer(y);
                        }
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

    public void setWidth1(int width) {
        this.width1 = width;
    }

    public void setxInContainer(float x) {
        this.xInContainer = x;
    }

    public void setyInContainer(float y) {
        this.yInContainer = y;
    }

    public void setLength1(int length1) {
        this.length1 = length1;
    }

    int getRandomNum(int num) {
        rand = new Random();
        return rand.nextInt(num);
    }

    public static boolean checkHoffemAxis(float a, float b, float c, float d) {
        boolean flag = false;

        if ((a - c) < 0) {
            if (b > c) {
                flag = true;
            } else {
                flag = false;
            }
        } else {
            if (a < d) {
                flag = true;
            } else {
                flag = false;
            }
        }

        return flag;
    }

    public static int dpToPx(float dp, Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

}

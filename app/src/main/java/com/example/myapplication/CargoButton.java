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
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class CargoButton extends androidx.appcompat.widget.AppCompatButton implements Serializable {

    static String selected = null;

    String objectId;
    Cargo cargo;
    int width1, length1;
    int widthInCm, lengthInCm;
    float xInContainer, yInContainer;
    double z = 0;

    double upWeight;
    ArrayList<CargoButton> up;
    CargoButton down;
    boolean insideContainer = false;

    int r = 0, g = 0, b = 0;
    Random rand;
    PopupWindow popUp;
    boolean click = true;

    public CargoButton(Context context) {
        super(context);
        setTouchListener();
    }

    public CargoButton(Context context, String objectId, float xInContainer, float yInContainer) {
        super(context);

        this.objectId = objectId;

        this.xInContainer = xInContainer;
        this.yInContainer = yInContainer;
        if (xInContainer==-1&&yInContainer==-1)
        {
            this.setX(MainActivity.MainInfo.screenWidth * MainActivity.MainInfo.buttonWidthPercentage);
            this.setY(MainActivity.MainInfo.screenHeight * MainActivity.MainInfo.buttonHeightPercentage);
        }
        else {
            this.setX(CargoTablePage.containerX + (int) Math.ceil(dpToPx(this.xInContainer, getContext()) * MainActivity.MainInfo.CargoPercentagecontainer));
            this.setY(CargoTablePage.containerY + (int) Math.ceil(dpToPx(this.yInContainer, getContext()) * MainActivity.MainInfo.CargoPercentagecontainer));
        }
        //popUp = new PopupWindow();
        this.setText(objectId);
        up = new ArrayList<>();

        //get the cargo with objectId
        for (int i = 0; i < MainActivity.CargoList.size(); i++)
            if (MainActivity.CargoList.get(i).objectid.equals(objectId))
                cargo = MainActivity.CargoList.get(i);

        //set layoutparams for the CargoButton in the Layout
        width1 = (int) Math.ceil(dpToPx(cargo.width.floatValue(), this.getContext()) * MainActivity.MainInfo.CargoPercentagecontainer);
        length1 = (int) Math.ceil(dpToPx(cargo.length.floatValue(), this.getContext()) * MainActivity.MainInfo.CargoPercentagecontainer);
        widthInCm = cargo.width.intValue();
        lengthInCm = cargo.length.intValue();
        ConstraintLayout.LayoutParams lp = new ConstraintLayout.LayoutParams(width1, length1);
        this.setLayoutParams(lp);

        //set onTouch listener
        setTouchListener();


        //generate randomized color
        r = getRandomNum(255);
        b = getRandomNum(255);
        g = getRandomNum(255);
        this.setBackgroundColor(Color.rgb(r, g, b));


    }




    public CargoButton(Context context,CargoButtonInfoForSolution infob)
    {
        super(context);
        z=infob.z;
        this.objectId= infob.objectId;
        this.setText(objectId);
        up = new ArrayList<>();
        this.xInContainer = infob.xInContainer;
        this.yInContainer = infob.yInContainer;
        this.insideContainer=infob.insideContainer;
        //get the cargo with objectId
        for (int i = 0; i < MainActivity.CargoList.size(); i++)
            if (MainActivity.CargoList.get(i).objectid.equals(objectId))
                cargo = MainActivity.CargoList.get(i);

        if (xInContainer==-1&&yInContainer==-1 || insideContainer==false)
        {
            this.setX(MainActivity.MainInfo.screenWidth * MainActivity.MainInfo.buttonWidthPercentage);
            this.setY(MainActivity.MainInfo.screenHeight * MainActivity.MainInfo.buttonHeightPercentage);
        }
        else {
            this.setX(CargoTablePage.containerX + (int) Math.ceil(dpToPx(this.xInContainer, getContext()) * MainActivity.MainInfo.CargoPercentagecontainer));
            this.setY(CargoTablePage.containerY + (int) Math.ceil(dpToPx(this.yInContainer, getContext()) * MainActivity.MainInfo.CargoPercentagecontainer));
        }
        widthInCm = infob.widthInCm;
        lengthInCm = infob.lengthInCm;
        width1 = (int) Math.ceil(dpToPx(widthInCm, this.getContext()) * MainActivity.MainInfo.CargoPercentagecontainer);
        length1 = (int) Math.ceil(dpToPx(lengthInCm, this.getContext()) * MainActivity.MainInfo.CargoPercentagecontainer);

        ConstraintLayout.LayoutParams lp = new ConstraintLayout.LayoutParams(width1, length1);
        this.setLayoutParams(lp);

        //set onTouch listener
        setTouchListener();


        //generate randomized color
        r = infob.r;
        g = infob.g;
        b = infob.b;
        this.setBackgroundColor(Color.rgb(r, g, b));

    }
    //numeric
    public boolean canPutOnOther(CargoButton other) {
        CargoButton temp=other;boolean flag=true;
        if (((this.widthInCm * this.lengthInCm) <= (other.lengthInCm * other.widthInCm)) && (other.z + other.cargo.height + this.cargo.height) < InfoHolder.containerHeight)
            if (this.widthInCm <= other.widthInCm && this.lengthInCm <= other.lengthInCm && other.insideContainer == true) {
                temp=other;
                    while(temp!=null)
                    { double tempUpWeight = calculateOtherUpWeight(temp);
                        if(temp.cargo.WeightThreshold < tempUpWeight + this.cargo.weight) {
                            flag=false;
                            break;
                        }

                        temp = temp.down;
                    }
                    if(flag==true)
                    return true;
            }
        return false;
    }

    double calculateOtherUpWeight(CargoButton other)
    {
        double weightUpOther=0;
        for (CargoButton c : other.up) {
            weightUpOther = weightUpOther + c.cargo.weight;
        }
        return weightUpOther;
    }

    //numeric
    public boolean putItOnOther(CargoButton other) {
        boolean hoffeem = false;

        if (checkIfInContainer(this.xInContainer, this.yInContainer, this.widthInCm, this.lengthInCm, other.xInContainer, other.yInContainer, other.widthInCm, other.lengthInCm) == true && (other.z + other.cargo.height + this.cargo.height) < InfoHolder.containerHeight) {
            if (down != null) {
                if (down.up.isEmpty() != true) {
                    for (int i = 0; i < down.up.size(); i++)
                        if (down.up.get(i).objectId.equals(this.objectId)) {
                            down.up.remove(i);
                            // i=i-1;
                            break;
                        }
                    z -= down.cargo.height;
                }
            }
            down = other;
            down.up.add(this);
            z = (down.z + down.cargo.height);
            return true;
        } else {
            return false;
        }
    }

    public boolean AutoPutOnOther(CargoButton other) {
        boolean hoffeem = false;
        float minY = 0, minZ = 0;
        float x = 0, y = 0, x1, y1;
        boolean hooffem = false, first = true, rotated = false, onFloor = false, inOkPlace = false;
        int size = other.up.size();

        CargoButton cargoButton1 = null;
        this.setxInContainer(other.xInContainer);
        this.setyInContainer(other.yInContainer);
        this.setX(CargoTablePage.containerX + (int) Math.ceil(dpToPx(this.xInContainer, getContext()) * MainActivity.MainInfo.CargoPercentagecontainer));
        this.setY(CargoTablePage.containerY + (int) Math.ceil(dpToPx(this.yInContainer, getContext()) * MainActivity.MainInfo.CargoPercentagecontainer));


        for (float j = other.yInContainer; j < other.yInContainer + other.lengthInCm; j++) {
            rotated = false;

            //for on x
            for (float i = other.xInContainer; i < other.xInContainer + other.widthInCm; i++) {
                if (i + this.widthInCm > other.xInContainer + other.widthInCm) {
                    if (rotated == false) {
                        this.rotate();
                        i = other.xInContainer - 1;
                        rotated = true;
                        first = true;
                        continue;
                    } else {
                        this.rotate();
                        j = minY + 1;
                    }
                    first = true;
                    break;
                }
                if (j + this.lengthInCm > other.yInContainer + other.lengthInCm)
                    break;

                //check if can put here(float x , float y)
                this.setxInContainer(i);
                this.setyInContainer(j);
                this.setX(CargoTablePage.containerX + (int) Math.ceil(dpToPx(this.xInContainer, getContext()) * MainActivity.MainInfo.CargoPercentagecontainer));
                this.setY(CargoTablePage.containerY + (int) Math.ceil(dpToPx(this.yInContainer, getContext()) * MainActivity.MainInfo.CargoPercentagecontainer));
                for (int i1 = 0; i1 < size; i1++) {
                    cargoButton1 = other.up.get(i1);
                    //  if (cargoButton1.objectId.equals(newButton.objectId)==false) {
                    //  if (cargoButton1.objectId!=newButton.objectId){

                    if (cargoButton1 != this) {
                        if (cargoButton1.insideContainer==true) {
                            hooffem = cargoButton1.checkhooffeem(this);
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
            return false;
        }

        if (down != null) {
            if (down.up.isEmpty() != true) {
                for (int i = 0; i < down.up.size(); i++)
                    if (down.up.get(i).objectId.equals(this.objectId)) {
                        down.up.remove(i);
                        break;
                    }
                z -= down.cargo.height;
            }
        }

        down = other;
        down.up.add(this);
        z = (down.z + down.cargo.height);
        this.insideContainer = true;
        return true;

    }



    //numeric
    public boolean checkhooffeem(CargoButton other) {
        boolean hoffemY, hoffemX;
        double xright, xleft, viewXright, viewXLeft;
        double yUp, yDown, viewYup, viewYDown;


        xleft = other.xInContainer;
        viewXLeft = this.xInContainer;
        xright = xleft + other.widthInCm;
        viewXright = viewXLeft + this.widthInCm;

        yUp = other.yInContainer;
        viewYup = this.yInContainer;
        yDown = yUp + other.lengthInCm;
        viewYDown = viewYup + this.lengthInCm;

        hoffemX = checkHoffemAxis(viewXLeft, viewXright, xleft, xright);
        hoffemY = checkHoffemAxis(viewYup, viewYDown, yUp, yDown);

        if (hoffemX == true && hoffemY == true) {
            return true;
        }
        return false;
    }

    public boolean hoffeemX(CargoButton other)
    {
        boolean hoffemX;
        double xright, xleft, viewXright, viewXLeft;

        xleft = other.xInContainer;
        viewXLeft = this.xInContainer;
        xright = xleft + other.widthInCm;
        viewXright = viewXLeft + this.widthInCm;

        hoffemX = checkHoffemAxis(viewXLeft, viewXright, xleft, xright);
        return hoffemX;
    }

    public boolean hoffeemY(CargoButton other)
    {
        boolean hoffemY;
        double yUp, yDown, viewYup, viewYDown;

        yUp = other.yInContainer;
        viewYup = this.yInContainer;
        yDown = yUp + other.lengthInCm;
        viewYDown = viewYup + this.lengthInCm;

        hoffemY = checkHoffemAxis(viewYup, viewYDown, yUp, yDown);
        return hoffemY;
    }
    boolean ifInContainer() {
        return checkIfInContainer(this.xInContainer, this.yInContainer, this.widthInCm, this.lengthInCm, CargoTablePage.containerX, CargoTablePage.containerWidth, CargoTablePage.containerY, CargoTablePage.containerLength);
    }

    static boolean checkIfInContainer(float x, float y, float width, float length, float containerX, float containerY, float containerWidth, float containerLength) {
        if ((x >= containerX) && ((x + width) <= (containerX + containerWidth)) && (y >= containerY) && ((y + length) <= (containerY + containerLength))) {
            return true;
        } else return false;
    }

    public void rotate() {
        ConstraintLayout.LayoutParams lp = new ConstraintLayout.LayoutParams(this.getLayoutParams().height, this.getLayoutParams().width);

        this.setLayoutParams(lp);
        int temp = this.width1;
        this.setWidth1(this.length1);
        this.setLength1(temp);

        int temp2 = this.widthInCm;
        this.widthInCm = this.lengthInCm;
        this.lengthInCm = temp2;
    }


    public void setTouchListener() {
        this.setOnTouchListener(new View.OnTouchListener() {
            int xDelta;
            int yDelta;
            boolean moved = false, hoffeem = false;
            int flag = 0;

            @Override
            public boolean onTouch(View view, MotionEvent event) {
                CargoButton c = ((CargoButton) view);
                flag = 0;
                if (c.up.isEmpty()) {
                    view.bringToFront();
                } else {
                    flag = 1;
                }
                final int x = (int) event.getRawX();
                final int y = (int) event.getRawY();
                hoffeem = false;
                switch (event.getAction() & MotionEvent.ACTION_MASK) {

                    case MotionEvent.ACTION_DOWN:
                        moved = false;

                        xDelta = (int) (x - view.getX());
                        yDelta = (int) (y - view.getY());
                        selected = objectId;
                        CargoInContainer.deleteButton.setVisibility(VISIBLE);
                        CargoInContainer.RotateButton.setVisibility(View.VISIBLE);
                        CargoInContainer.MoveToNearestBtn.setVisibility(View.VISIBLE);
                        MainActivity.MainInfo.Dialogbox.setText("id:" + cargo.objectid + "\n" + "height:" + cargo.height + "\n" + "width:" + cargo.width + "\n" + "length:" + cargo.length + "\n" + "x:" + c.xInContainer + "\n" + "y:" + c.yInContainer + "\n" + "z:" + z + "\n");

                        break;

                    case MotionEvent.ACTION_UP:
                        boolean hoffemX = false;
                        boolean hoffemY = false;
                        if (moved == true) {



                            if (checkIfInContainer(view.getX(), view.getY(), view.getWidth(), view.getHeight(), CargoTablePage.containerX, CargoTablePage.containerY, CargoTablePage.containerWidth, CargoTablePage.containerLength) == true) {


                                if (tryToPutHere(null) == true) {
                                    insideContainer = true;
                                }
                            } else {

                                Toast.makeText(getContext(),
                                        "out of the container", Toast.LENGTH_SHORT)
                                        .show();
                                if (insideContainer == true) {
                                    MainActivity.MainInfo.totalWeight -= cargo.weight;
                                    CargoInContainer.totalWeightText.setText("container weight: " + (int) MainActivity.MainInfo.totalWeight+"(kg)");
                                    MainActivity.MainInfo.totalCost -= (cargo.cost + MainActivity.MainInfo.ProcessingCost + (1.0/MainActivity.MainInfo.AverageAmountOfBoxes)*MainActivity.MainInfo.OneFullContainerTimeInMinutesPerWorker*(MainActivity.MainInfo.WorkersHourlySalary/60.0)*MainActivity.MainInfo.totalWorkers);
                                    CargoInContainer.totalCostText.setText("container cost: " + (int) MainActivity.MainInfo.totalCost+"(NIS)");
                                    MainActivity.MainInfo.totalTime -= (((1.0/MainActivity.MainInfo.AverageAmountOfBoxes)*MainActivity.MainInfo.OneFullContainerTimeInMinutesPerWorker)/MainActivity.MainInfo.totalWorkers) ;
                                    CargoInContainer.totalTimeText.setText("Approximate Time: " + String.format("%.2f", MainActivity.MainInfo.totalTime)+"(H)");
                                    insideContainer = false;
                                }

                                ((CargoButton) view).insideContainer = false;
                                ((CargoButton) view).setxInContainer(-1);
                                ((CargoButton) view).setyInContainer(-1);
                                view.setX(MainActivity.MainInfo.screenWidth * MainActivity.MainInfo.buttonWidthPercentage);
                                view.setY(MainActivity.MainInfo.screenHeight * MainActivity.MainInfo.buttonHeightPercentage);
                                if (down != null) {
                                    if (down.up != null)
                                        if (down.up.isEmpty() == false) {
                                            for (int i = 0; i < down.up.size(); i++)
                                                if (down.up.get(i).objectId.equals(((CargoButton) view).objectId)) {
                                                    z -= down.cargo.height;
                                                    down.up.remove(i);

                                                    break;
                                                }
                                        }
                                    down = null;

                                }
                                z = 0;
                            }
                            CargoInContainer.saveSolution();
                        } else {

                        }
                        break;

                    case MotionEvent.ACTION_MOVE:
                        if (flag == 0) {

                            moved = true;
                            if (y - yDelta > CargoTablePage.containerY) {
                                view.setX(x - xDelta);
                                view.setY(y - yDelta);
                            }

                        }
                        else
                            MainActivity.MainInfo.Dialogbox.setText("Alert!: You are trying to move an object that cannot be moved");
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

    public static boolean checkHoffemAxis(double a, double b, double c, double d) {
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

    public boolean tryToPutHere(CargoButton other) {


        CargoButton b, v = this;
        int cnt = 0;
        boolean inOkPlace = true;
        float numericX = (((this.getX() - CargoTablePage.containerX) / CargoTablePage.containerWidth) * 234);
        float numericY = (((this.getY() - CargoTablePage.containerY) / CargoTablePage.containerLength) * 586);

        float oldX = v.xInContainer;
        float oldy = v.yInContainer;

        v.setxInContainer(numericX);
        v.setyInContainer(numericY);
        v.setX(CargoTablePage.containerX + (int) Math.ceil(dpToPx(v.xInContainer, getContext()) * MainActivity.MainInfo.CargoPercentagecontainer));
        v.setY(CargoTablePage.containerY + (int) Math.ceil(dpToPx(v.yInContainer, getContext()) * MainActivity.MainInfo.CargoPercentagecontainer));

        if (other == null) {
            for (int i = 0; i < CargoTablePage.buttons.size(); i++) {

                b = CargoTablePage.buttons.get(i);
                if (v != b) {
                    if (b.z == 0) {
                        if (b.insideContainer==true) {
                            if (v.checkhooffeem(b) == true) {
                                cnt++;
                                if (cnt > 1) {
                                    inOkPlace = false;
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        } else {
            for (int i = 0; i < other.up.size(); i++) {

                b = other.up.get(i);
                if (v != b) {
                    if (b.insideContainer==true) {
                        if (v.checkhooffeem(b) == true) {
                            cnt++;
                            if (cnt > 1) {
                                inOkPlace = false;
                                break;
                            }
                        }
                    }
                }
            }
        }

        if (inOkPlace == false) {

            setxInContainer(oldX);
            setyInContainer(oldy);
            if(oldX==-1 && oldy==-1) {
                v.setX(MainActivity.MainInfo.screenWidth * MainActivity.MainInfo.buttonWidthPercentage);
                v.setY(MainActivity.MainInfo.screenHeight * MainActivity.MainInfo.buttonHeightPercentage);
              }
            else
            {
                v.setX(CargoTablePage.containerX + (int) Math.ceil(dpToPx(v.xInContainer, getContext()) * MainActivity.MainInfo.CargoPercentagecontainer));
                v.setY(CargoTablePage.containerY + (int) Math.ceil(dpToPx(v.yInContainer, getContext()) * MainActivity.MainInfo.CargoPercentagecontainer));

            }
            return false;
        }

        if (cnt == 0) {
            inOkPlace = true;
            if (insideContainer == false) {
                MainActivity.MainInfo.totalWeight += cargo.weight;
                CargoInContainer.totalWeightText.setText("container weight: " + (int) MainActivity.MainInfo.totalWeight+"(kg)");
                MainActivity.MainInfo.totalCost += cargo.cost + MainActivity.MainInfo.ProcessingCost+(1.0/MainActivity.MainInfo.AverageAmountOfBoxes)*MainActivity.MainInfo.OneFullContainerTimeInMinutesPerWorker*(MainActivity.MainInfo.WorkersHourlySalary/60.0)*MainActivity.MainInfo.totalWorkers;
                CargoInContainer.totalCostText.setText("container cost: " + (int) MainActivity.MainInfo.totalCost+"(NIS)");
                MainActivity.MainInfo.totalTime += ((1/MainActivity.MainInfo.AverageAmountOfBoxes*MainActivity.MainInfo.OneFullContainerTimeInMinutesPerWorker)/MainActivity.MainInfo.totalWorkers) ;
                CargoInContainer.totalTimeText.setText("Approximate Time: " + String.format("%.2f", MainActivity.MainInfo.totalTime)+"(H)");
                insideContainer = true;
            }
            insideContainer = true;
            if (other != null) {
                if (canPutOnOther(other)) {
                    if (putItOnOther(other) == false) {
                        setxInContainer(oldX);
                        setyInContainer(oldy);
                        if(oldX==-1 && oldy==-1) {
                            v.setX(MainActivity.MainInfo.screenWidth * MainActivity.MainInfo.buttonWidthPercentage);
                            v.setY(MainActivity.MainInfo.screenHeight * MainActivity.MainInfo.buttonHeightPercentage);
                        }
                        else
                        {
                            v.setX(CargoTablePage.containerX + (int) Math.ceil(dpToPx(v.xInContainer, getContext()) * MainActivity.MainInfo.CargoPercentagecontainer));
                            v.setY(CargoTablePage.containerY + (int) Math.ceil(dpToPx(v.yInContainer, getContext()) * MainActivity.MainInfo.CargoPercentagecontainer));

                        }        return false;
                    }
                }
                else
                {
                    setxInContainer(oldX);
                    setyInContainer(oldy);
                    if(oldX==-1 && oldy==-1) {
                        v.setX(MainActivity.MainInfo.screenWidth * MainActivity.MainInfo.buttonWidthPercentage);
                        v.setY(MainActivity.MainInfo.screenHeight * MainActivity.MainInfo.buttonHeightPercentage);
                    }
                    else
                    {
                        v.setX(CargoTablePage.containerX + (int) Math.ceil(dpToPx(v.xInContainer, getContext()) * MainActivity.MainInfo.CargoPercentagecontainer));
                        v.setY(CargoTablePage.containerY + (int) Math.ceil(dpToPx(v.yInContainer, getContext()) * MainActivity.MainInfo.CargoPercentagecontainer));

                    }
                    return false;
                }
            }
            else {


                    if (down != null) {
                        if (down.up.isEmpty() != true) {
                            for (int i = 0; i < down.up.size(); i++)
                                if (down.up.get(i).objectId.equals(this.objectId)) {
                                    down.up.remove(i);
                                    // i=i-1;
                                    break;
                                }
                            z -= down.cargo.height;
                        }
                        z=0;
                        down=null;
                    }


                z=0;
                }
            return true;
        }

        if (cnt == 1) {
            if (other!=null) {
                for (int i = 0; i < other.up.size(); i++) {

                    b = other.up.get(i);
                    if (v != b) {
                        if (b.insideContainer == true) {
                            if (v.checkhooffeem(b) == true) {
                                if (tryToPutHere(b) == true) {
                                    return true;
                                } else {
                                    setxInContainer(oldX);
                                    setyInContainer(oldy);
                                    if (oldX == -1 && oldy == -1) {
                                        v.setX(MainActivity.MainInfo.screenWidth * MainActivity.MainInfo.buttonWidthPercentage);
                                        v.setY(MainActivity.MainInfo.screenHeight * MainActivity.MainInfo.buttonHeightPercentage);
                                    } else {
                                        v.setX(CargoTablePage.containerX + (int) Math.ceil(dpToPx(v.xInContainer, getContext()) * MainActivity.MainInfo.CargoPercentagecontainer));
                                        v.setY(CargoTablePage.containerY + (int) Math.ceil(dpToPx(v.yInContainer, getContext()) * MainActivity.MainInfo.CargoPercentagecontainer));

                                    }
                                    return false;
                                }
                            }
                        }
                    }
                }
            }
            else
            {
                for (int i = 0; i < CargoTablePage.buttons.size(); i++) {

                    b = CargoTablePage.buttons.get(i);
                    if (v != b) {
                        if (b.insideContainer == true) {
                            if (v.checkhooffeem(b) == true) {
                                if (tryToPutHere(b))
                                    return true;

                                else {
                                    setxInContainer(oldX);
                                    setyInContainer(oldy);
                                    if (oldX == -1 && oldy == -1) {
                                        v.setX(MainActivity.MainInfo.screenWidth * MainActivity.MainInfo.buttonWidthPercentage);
                                        v.setY(MainActivity.MainInfo.screenHeight * MainActivity.MainInfo.buttonHeightPercentage);
                                    } else {
                                        v.setX(CargoTablePage.containerX + (int) Math.ceil(dpToPx(v.xInContainer, getContext()) * MainActivity.MainInfo.CargoPercentagecontainer));
                                        v.setY(CargoTablePage.containerY + (int) Math.ceil(dpToPx(v.yInContainer, getContext()) * MainActivity.MainInfo.CargoPercentagecontainer));

                                    }
                                    return false;
                                }
                            }
                        }
                    }
                }
            }
        }
        setxInContainer(oldX);
        setyInContainer(oldy);
        if(oldX==-1 && oldy==-1) {
            v.setX(MainActivity.MainInfo.screenWidth * MainActivity.MainInfo.buttonWidthPercentage);
            v.setY(MainActivity.MainInfo.screenHeight * MainActivity.MainInfo.buttonHeightPercentage);
        }
        else
        {
            v.setX(CargoTablePage.containerX + (int) Math.ceil(dpToPx(v.xInContainer, getContext()) * MainActivity.MainInfo.CargoPercentagecontainer));
            v.setY(CargoTablePage.containerY + (int) Math.ceil(dpToPx(v.yInContainer, getContext()) * MainActivity.MainInfo.CargoPercentagecontainer));

        }
        return false;
    }

}
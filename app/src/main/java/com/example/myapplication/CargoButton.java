package com.example.myapplication;

import android.content.Context;

public class CargoButton extends androidx.appcompat.widget.AppCompatButton {

    float x,y;
    String objectId;
    public CargoButton(Context context) {
        super(context);

    }
    public CargoButton(Context context,String objectId,float x,float y) {
        super(context);
        this.objectId = objectId;
        this.x=x;
        this.y=y;
        this.setX(x);
        this.setY(y);
    }
}

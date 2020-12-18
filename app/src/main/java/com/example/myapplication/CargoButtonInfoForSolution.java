package com.example.myapplication;

import java.util.ArrayList;

public class CargoButtonInfoForSolution {
    int r,g,b;
    String objectId;
    float xInContainer, yInContainer;
    double z ;
    String downObjectId;
    ArrayList<String> upIds;
    int widthInCm, lengthInCm;
    boolean insideContainer;
    CargoButtonInfoForSolution()
    {

    }

    CargoButtonInfoForSolution(CargoButton button)
    {
        this.objectId=String.copyValueOf(button.objectId.toCharArray());
        this.xInContainer=button.xInContainer;
        this.yInContainer=button.yInContainer;
        this.widthInCm=button.widthInCm;
        this.lengthInCm=button.lengthInCm;
        this.z=button.z;
        this.insideContainer=button.insideContainer;
        r=button.r;
        g=button.g;
        b=button.b;
        if (button.down!=null) {
            this.downObjectId = String.copyValueOf(button.down.objectId.toCharArray());
        }
        upIds=new ArrayList<>();
        if (button.up!=null) {
            if(button.up.isEmpty()==false) {
                for (CargoButton u : button.up) {
                    upIds.add(String.copyValueOf(u.objectId.toCharArray()));
                }
            }
        }

    }

}

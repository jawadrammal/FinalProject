package com.example.myapplication;

import java.io.Serializable;

public class Cargo implements Serializable
{
    static int selectedCnt=0;
    String objectid;
    Double height, width, weight, length, cost;
    boolean fragile;
    Double WeightThreshold;
    boolean Selected;
    boolean inCargoPage=false;

    public Cargo(Cargo c) { //constructor of cargo
        this.objectid=String.copyValueOf(c.objectid.toCharArray());
        this.height=c.height;
        width=c.width;
        weight=c.weight;
        length=c.length;
        fragile=c.fragile;
        WeightThreshold=c.WeightThreshold;
        cost=c.cost;
        Selected=c.Selected;
        inCargoPage=c.inCargoPage;
    }

    public Cargo()
    {

    }

    public void setInCargoPage(boolean inCargoPage) {
        this.inCargoPage = inCargoPage;
    }
    public boolean isInCargoPage()
    {
        return this.inCargoPage;
    }
    public boolean isSelected(){return this.Selected;}
}

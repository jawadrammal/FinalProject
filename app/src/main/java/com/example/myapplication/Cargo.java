package com.example.myapplication;

public class Cargo
{
    static int selectedCnt=0;
    String objectid;
    Double height, width, weight, length;
    boolean fragile;
    Double WeightThreshold;
    boolean Selected;
    boolean inCargoPage=false;
    public void setInCargoPage(boolean inCargoPage) {
        this.inCargoPage = inCargoPage;
    }
    public boolean isInCargoPage()
    {
        return this.inCargoPage;
    }
}

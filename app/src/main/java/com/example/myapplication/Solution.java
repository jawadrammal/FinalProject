package com.example.myapplication;

import java.util.ArrayList;

public class Solution {
    public ArrayList<Cargo> CargoList = new ArrayList<Cargo>();
    public InfoHolder MainInfo = new InfoHolder();
    public ArrayList<CargoButton> buttons = new ArrayList<>();

    Solution(ArrayList<Cargo> CargoList,InfoHolder MainInfo,ArrayList<CargoButton> buttons)
    {
        this.CargoList=CargoList;
        this.MainInfo=MainInfo;
        this.buttons=buttons;
    }

}

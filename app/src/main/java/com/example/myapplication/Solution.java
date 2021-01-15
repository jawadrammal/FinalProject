package com.example.myapplication;

import java.io.Serializable;
import java.util.ArrayList;

public class Solution implements Serializable {
    public ArrayList<Cargo> CargoList = new ArrayList<Cargo>();
    public InfoHolder MainInfo = new InfoHolder();
   // public ArrayList<CargoButton> buttons = new ArrayList<>();
    public ArrayList<CargoButtonInfo> buttonsInfo;
    public double totalWeight;
    public double totalCost;
    public double totalTime;
    Solution(ArrayList<Cargo> CargoList,InfoHolder MainInfo,ArrayList<CargoButton> buttons)
    {
        this.CargoList=CargoList;
        this.MainInfo=MainInfo;
       // this.buttons=buttons;
    }
    Solution()
    {
        totalWeight = com.example.myapplication.CargoList.MainInfo.totalWeight;
        totalCost   = com.example.myapplication.CargoList.MainInfo.totalCost;
        totalTime = com.example.myapplication.CargoList.MainInfo.totalTime;
        buttonsInfo=new ArrayList<>();
        for (Cargo c: com.example.myapplication.CargoList.CargoList
             ) {
            CargoList.add(new Cargo(c));
        }
        for (CargoButton b : ContainerInfo.buttons)
        {
            buttonsInfo.add(new CargoButtonInfo(b));
        }

    }


}

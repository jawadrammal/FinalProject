package com.example.myapplication;

import java.io.Serializable;
import java.util.ArrayList;

public class Solution implements Serializable {
    public ArrayList<Cargo> CargoList = new ArrayList<Cargo>();
    public InfoHolder MainInfo = new InfoHolder();
   // public ArrayList<CargoButton> buttons = new ArrayList<>();
    public ArrayList<CargoButtonInfoForSolution> buttonsInfo;
    public double totalWeight;
    Solution(ArrayList<Cargo> CargoList,InfoHolder MainInfo,ArrayList<CargoButton> buttons)
    {
        this.CargoList=CargoList;
        this.MainInfo=MainInfo;
       // this.buttons=buttons;

    }
    Solution()
    {
        totalWeight = MainActivity.MainInfo.totalWeight;
        buttonsInfo=new ArrayList<>();
        for (Cargo c:MainActivity.CargoList
             ) {
            CargoList.add(new Cargo(c));
        }
        for (CargoButton b : CargoTablePage.buttons)
        {
            buttonsInfo.add(new CargoButtonInfoForSolution(b));
        }

    }


}

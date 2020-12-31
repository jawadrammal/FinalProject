package com.example.myapplication;

import android.widget.TableLayout;
import android.widget.TextView;

import java.io.Serializable;

public class InfoHolder implements Serializable {
    TableLayout mytable;
    double totalWeight=0;//holds the total weight of the container excluding the container's weight
    double totalCost=0; //holds the total price of the cargo+processing cost of the container excluding the container's price
    double totalTime=0; // holds approximate time to load container via a certain plan
    int totalWorkers=2; // holds the recommended amount of workers for the container
    float screenWidth;
    float screenHeight;
    float alertWidthPerc;
    float alertHeightPerc;
    float containerViewWidth;
    float containerViewLength;
    float alertXPerc;
    float alertYPerc;
    float ContainerStartX;
    float ContainerStartY;
    float buttonWidthPercentage;
    float buttonHeightPercentage;
    float CargoPercentagecontainer; // help to place cargo
    TextView Dialogbox; // holds the information dialog with the user
    static double containerHeight= 238.44; //container height
    static double ProcessingCost= 5.0; // processing cost is taken into account for the forklifts and ropes and other equipments that could be rented
    static double OneFullContainerTimeInMinutesPerWorker= 180.0; //takes on average 180 minutes to load cargo in container by one worker
    static double AverageAmountOfBoxes= 252.0; // average amount of boxes that go into a container
    static double WorkersHourlySalary = 85; //worker's average salary
}

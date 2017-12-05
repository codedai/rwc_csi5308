package sample;

import jbotsim.Node;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Random;

public class LCMNode extends Node{
    ArrayList<Point2D> locations;
    Point2D target;

    @Override
    public void onPreClock() {  // LOOK
        System.out.print("Look");
    }
    @Override
    public void onClock(){      // COMPUTE
        System.out.print("-Compute");
    }
    @Override
    public void onPostClock(){  // MOVE
        System.out.print("-Move" + "\n");
    }
}
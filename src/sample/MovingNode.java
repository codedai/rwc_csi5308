package sample;

import jbotsim.Node;

/**
 * Created by Wange on 2017/12/1.
 */
public class MovingNode extends Node{
    @Override
    public void onStart() {
        setDirection(Math.random()*2*Math.PI);
    }

    @Override
    public void onClock() {
        move();
        wrapLocation();
    }
}

package sample;

import jbotsim.Link;
import jbotsim.Node;

import java.awt.*;

/**
 * Created by Wange on 2017/12/1.
 */
public class LonweGraphBased extends Node {

    @Override
    public void onStart() {
        setColor(Color.GREEN);
    }

    @Override
    public void onLinkAdded(Link link) {
        setColor(Color.red);
    }

    @Override
    public void onLinkRemoved(Link link) {
        if (!hasNeighbors()){
            setColor(Color.green);
        }
    }
}

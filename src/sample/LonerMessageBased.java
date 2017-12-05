package sample;

import jbotsim.Message;
import jbotsim.Node;

import java.awt.*;

/**
 * Created by Wange on 2017/12/1.
 */
public class LonerMessageBased extends Node {
    private boolean mayBeAlone = true;
    @Override
    public void onClock(){
        setColor(mayBeAlone? Color.green : Color.MAGENTA);
        sendAll(new Message());
        mayBeAlone = true;
    }

    @Override
    public void onMessage(Message msg) {
        mayBeAlone = !mayBeAlone;
    }
}

import jbotsim.Message;
import jbotsim.Node;
import jbotsim.event.MessageListener;

import java.awt.*;

/**
 * Created by Wange on 2017/12/2.
 */
public class RingNode extends Node implements MessageListener{

    int numberOfRobots = 0;
    boolean isVisited = false;

    public RingNode(){
        this.setColor(Color.black);
        this.setSize(10);
    }

    public void setVisited() {
        this.setColor(Color.blue);
        this.isVisited = true;
    }

    public void setVisting() {
        setVisited();
        this.numberOfRobots = this.numberOfRobots + 1;
    }

    public void setLeft() {
        this.setColor(Color.red);
        this.numberOfRobots = this.numberOfRobots -1;
    }

    @Override
    public void onMessage(Message msg) {
//        System.out.println(6);
        Object msgContent = msg.getContent();
        if(msgContent == Define.VISITING) {
            setVisting();
        }else if (msgContent == Define.LEAVING) {
            setLeft();
        }
    }



    public int getNumberOfRobots() {
        return numberOfRobots;
    }

    public boolean getIsVisited() {
        return isVisited;
    }

}

package sample;

import jbotsim.Message;
import jbotsim.Node;

/**
 * Created by Wange on 2017/12/1.
 */
public class SpanningTreeNode extends Node{
    private Node parent;

    @Override
    public void onStart() {
        parent = null;
//        onSelection();
        System.out.print(12);
    }

    @Override
    public void onSelection() {
        System.out.print(5);
        parent = this;
        sendAll(new Message());
    }

    @Override
    public void onMessage(Message msg) {
        System.out.print(4);
        if(parent == null) {
            parent = msg.getSender();
            getCommonLinkWith(parent).setWidth(4);
            sendAll(new Message());
        }
    }
}

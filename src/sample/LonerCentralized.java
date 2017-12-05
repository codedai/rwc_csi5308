package sample;

import jbotsim.Link;
import jbotsim.Node;
import jbotsim.Topology;
import jbotsim.event.ConnectivityListener;
import jbotsim.event.TopologyListener;

import java.awt.*;
/**
 * Created by Wange on 2017/12/1.
 */
public class LonerCentralized implements TopologyListener,ConnectivityListener {
    public LonerCentralized(Topology tp){
        tp.addTopologyListener(this);
        tp.addConnectivityListener(this);
    }

    @Override
    public void onNodeAdded(Node node) {
        node.setColor(node.hasNeighbors()? Color.red:Color.green);
    }

    @Override
    public void onNodeRemoved(Node node) {
    }

    @Override
    public void onLinkAdded(Link link) {
        for (Node node : link.endpoints()){
            node.setColor(Color.red);
        }
    }

    @Override
    public void onLinkRemoved(Link link) {
        for (Node node : link.endpoints()){
            if(!node.hasNeighbors()) {
                node.setColor(Color.green);
            }
        }
    }

}

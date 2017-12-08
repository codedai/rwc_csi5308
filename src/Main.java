import jbotsim.Link;
import jbotsim.Node;
import jbotsim.Topology;
import jbotsim.ui.JViewer;
import sample.QELeaderElection;

import java.util.List;
import java.util.Random;

/**
 * Created by Wange on 2017/12/2.
 */

public class Main {

    public static void generateRing(Topology topology, int nbNodes, boolean directed){
        topology.setCommunicationRange(0.00001);
        double angle=Math.PI*2.0/nbNodes;
        int scale=nbNodes * 8;
        for (int i=0; i<nbNodes; i++)
            topology.addNode(50 + scale + Math.cos(angle*i)*scale,
                    50 + scale + Math.sin(angle*i)*scale,topology.newInstanceOfModel("default"));

        List<Node> nodes = topology.getNodes();
        Link.Type type = directed?Link.Type.DIRECTED:Link.Type.UNDIRECTED;
        for (int i=0; i<nbNodes-1; i++)
            topology.addLink(new Link(nodes.get(i), nodes.get(i+1), type));
        topology.addLink(new Link(nodes.get(nbNodes - 1), nodes.get(0), type));
    }

    public static void initRobotPostion(Topology topology, int robotSize) {
        Random rnd = new Random();
        List<Node> ringNodes = topology.getNodes();
        int nodeSize = ringNodes.size();

        int[] ids = new int[nodeSize];
        for (int i = 0; i < nodeSize; i++) {
            ids[i] = i;
        }

        for (int i = 0; i < ids.length; i++){
            int j = rnd.nextInt(nodeSize);
            int temp = ids[i];
            ids[i] = ids[j];
            ids[j] = temp;
        }

        for(int i = 0; i < robotSize; i++) {
            Node tempNode = ringNodes.get(ids[i]);
            double xPosition = tempNode.getX();
            double yPosition = tempNode.getY();
            Robot robot = new Robot(ringNodes, tempNode.getID());
            topology.addNode(xPosition, yPosition, robot);
        }

    }

    public static void iniRobotPositionInBlockOfSizeTwo(Topology topology, int robotSize){
        List<Node> ringNodes = topology.getNodes();
        int count = 0;
        for(int i = 0 ; i < (int)robotSize/2; i++){
            Node tempNode = ringNodes.get(count);
            double xPosition = tempNode.getX();
            double yPosition = tempNode.getY();
            Robot robot = new Robot(ringNodes, tempNode.getID());
            topology.addNode(xPosition, yPosition, robot);
            count = count + 1;
            tempNode = ringNodes.get(count);
            xPosition = tempNode.getX();
            yPosition = tempNode.getY();
            robot = new Robot(ringNodes, tempNode.getID());
            topology.addNode(xPosition, yPosition, robot);

            count = count + 2;
        }
    }


    public static void iniRobotPositionInBlockOfSizeThree(Topology topology, int robotSize){
        List<Node> ringNodes = topology.getNodes();
        int count = 0;
        for(int i = 0 ; i < (int)robotSize/3; i++){
            Node tempNode = ringNodes.get(count);
            double xPosition = tempNode.getX();
            double yPosition = tempNode.getY();
            Robot robot = new Robot(ringNodes, tempNode.getID());
            topology.addNode(xPosition, yPosition, robot);
            count = count + 1;
            tempNode = ringNodes.get(count);
            xPosition = tempNode.getX();
            yPosition = tempNode.getY();
            robot = new Robot(ringNodes, tempNode.getID());
            topology.addNode(xPosition, yPosition, robot);
            count = count + 1;
            tempNode = ringNodes.get(count);
            xPosition = tempNode.getX();
            yPosition = tempNode.getY();
            robot = new Robot(ringNodes, tempNode.getID());
            topology.addNode(xPosition, yPosition, robot);
            count = count + 2;
        }
    }

    public static void iniRobotPositionForTypeC(Topology topology, int robotSize){
        List<Node> ringNodes = topology.getNodes();
        int nodeSize = ringNodes.size();
        int count = 0;
        for(int i = 0 ; i < (int)robotSize; i++){
            Node tempNode = ringNodes.get(count);
            double xPosition = tempNode.getX();
            double yPosition = tempNode.getY();
            Robot robot = new Robot(ringNodes, tempNode.getID());
            topology.addNode(xPosition, yPosition, robot);
            count = count + 2;
        }
    }


    public static void main(String[] args) {

        Topology tpg = new Topology();
        tpg.setClockSpeed(500);
        tpg.setDefaultNodeModel(RingNode.class);
        int nodeSize = 31;
        generateRing(tpg, nodeSize, false);
        tpg.setDefaultNodeModel(Robot.class);
        int robotSize = 30;
        initRobotPostion(tpg, 18);
//        iniRobotPositionInBlockOfSizeTwo(tpg, robotSize);
//        iniRobotPositionInBlockOfSizeThree(tpg, robotSize);
//        iniRobotPositionForTypeC(tpg, robotSize);
        new JViewer(tpg);

    }
}

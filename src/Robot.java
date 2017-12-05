import jbotsim.Message;
import jbotsim.Node;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wange on 2017/12/2.
 */
public class Robot extends Node {

    private List<Node> vision;
    private int position;
    private int target;
    private boolean inFirstStep = true;
    private boolean isPlayer = false;
    private Configration configration;

    public Robot() {
        setIcon("./img/robot.png");
        setSize(20);
        setSensingRange(1);
        this.vision = null;
    }

    public Robot(List<Node> vision, int id) {
        setIcon("./img/robot.png");
        setSize(20);
        setSensingRange(1);
        this.vision = vision;
        this.position = id;
    }

    @Override
    public void onStart() {
        List<Node> onNode = this.getSensedNodes();
        for (Node n : onNode) {
            this.send(n, new Message(Define.VISITING));
        }
    }

    @Override
    public void onPreClock() {  // LOOK
        configration = new Configration(vision, position);
        this.inFirstStep = configration.isInFirstStep();
//        System.out.println(configration);
    }

    @Override
    public void onClock(){      // COMPUTE
        System.out.println(getID() + "  -Compute");
        if(inFirstStep) { //Set up phase
            setUp();
//            System.out.println(configration.getMySigmaNegative());
        }
    }
    @Override
    public void onPostClock(){  // MOVE
        System.out.print("-Move" + "\n");
        if(isPlayer) {

            System.out.println(getID() + "  "  + isPlayer);

            int temp = position + target;

            Node node = vision.get((vision.size() +temp) % vision.size());

            List<Node> onNode = this.getSensedNodes();

            for(int i = 0; i < onNode.size(); i++){
//                System.out.println(123);
                RingNode n = (RingNode)onNode.get(i);
                n.setLeft();
            }

            setLocation(node.getX(), node.getY());

            onNode = this.getSensedNodes();
            for(int i = 0; i < onNode.size(); i++){
//                System.out.println(123);
                RingNode n = (RingNode)onNode.get(i);
                n.setVisting();
            }

            this.position = (vision.size() +temp) % vision.size();
        }
        isPlayer = false;

    }

    private void setUp() {
        configration.calInterDistance();
        configration.calIsIsolated();

        configration.calBlocks();
//        for (Block b :
//                configration.getBlocks()) {
//            System.out.println(b);
//        }
        System.out.println(getID() + " isHasIsolated - " + configration.calHasIsolated());
        System.out.println(getID() + " vision - " + configration.getMySigmaPositive());
        if(configration.calHasIsolated()) {
            // Type A
            setUpPhaseTypeA();
        } else {
            System.out.println("Type B");
            System.out.println(configration.getBlocks());
            if (configration.calHasNonleadingBlock()){
                // Type B
                setUpPhaseTypeB();
            } else {

            }

        }

    }

    private void setUpPhaseTypeB(){
        if(configration.calAllBlocksInSameSize()){
            typeBI();
        } else {
            typeBII();
        }
    }

    private void typeBICaseI(){}

    private void typeBICaseII(){}

    // prior
    private void typeBII(){
        System.out.println("++++++++++++++++++");
        System.out.println("isPlayer in BII: " + configration.calIsPlayerForTypeBII());
        if(configration.calIsPlayerForTypeBII()){
            isPlayer = true;
            target = configration.getTarget();
        }
    }

    private void typeBI(){}

    private void setUpPhaseTypeA() {
        System.out.println(getID() + " isolated - " + configration.isolated());
        if(configration.isolated()) {
            configration.calMaxBlockNearIsolated();
            configration.calIsPlayerInTypeA();
            isPlayer = configration.isPlayer();
            target = configration.getTarget();
        }
    }



    public void setPostion(int position){
        this.position = position;
    }

    public void setIsPlayer(boolean isPlayer) {
        this.isPlayer = isPlayer;
    }

    public Configration getConfigration() {
        return configration;
    }
}

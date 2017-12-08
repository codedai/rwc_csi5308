import jbotsim.Message;
import jbotsim.Node;
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
    private boolean stopFlag = false;
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
        if(!stopFlag){
            configration = new Configration(vision, position);
            this.inFirstStep = configration.isInFirstStep();
//        System.out.println(configration);
            System.out.println(getID() + " vision - " + configration.getMySigmaPositive());
        }
    }

    @Override
    public void onClock(){      // COMPUTE
        if(!stopFlag){
            System.out.println(getID() + "  -Compute");
            if(inFirstStep) { //Set up phase
                setUp();
//            System.out.println(configration.getMySigmaNegative());
            }else {
                Exploration();
            }
        }
    }
    @Override
    public void onPostClock(){  // MOVE
        if(!stopFlag) {
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
                    if(onNode.get(i).getClass().toString().equals("class RingNode")){
//                System.out.println(onNode.get(i).getClass().toString().equals("class RingNode") + "()()()");
                        RingNode n = (RingNode)onNode.get(i);
                        n.setVisting();
                    }
                }

                this.position = (vision.size() +temp) % vision.size();
            }
            isPlayer = false;
        }
    }

    private void Exploration(){
        if(configration.calIsPlayerForExploration()){
            isPlayer = true;
            target = configration.getTarget();
        }else {
            stopFlag = true;
        }
    }


    private void setUp() {

        // The logic of the code have some problem

        configration.calInterDistance();
        configration.calIsIsolated();
        configration.calBlocks();
//        for (Block b :
//                configration.getBlocks()) {
//            System.out.println(b);
//        }
//        System.out.println(getID() + " isHasIsolated - " + configration.calHasIsolated());
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
                if(configration.getInterDistance() > 1){
                    // Type C
                    setUpPhaseTypeC();
                }else {
                    System.out.println("Set up is done, move to Tower-Creation Phase");
                    TowerCreation();
                }
            }

        }

    }

    private void TowerCreation(){
        System.out.println("<><><><><<><><><><><<>>");
        if(configration.calIsPlayerForTowerCreation()){
            isPlayer = true;
            target = configration.getTarget();
        }
    }

    private void setUpPhaseTypeC() {
        if(configration.calIsPlayerForTypeC()){
            isPlayer = true;
            target = configration.getTarget();
        }
    }

    private void setUpPhaseTypeB(){
        if(configration.calAllBlocksInSameSize()){
            System.out.println("Same size");
            typeBI();
        } else {
            typeBII();
        }
    }

    private void typeBICaseI(){
        if(configration.calIsPlayerForTypeBICaseI()){
            isPlayer = true;
            target = configration.getTarget();
        }
    }

    private void typeBICaseII() {
        if(configration.calIsPlayerForTypeBICaseII()){
            isPlayer = true;
            target = configration.getTarget();
        }
    }


    // prior
    private void typeBII(){
//        System.out.println("++++++++++++++++++");
//        System.out.println("isPlayer in BII: " + configration.calIsPlayerForTypeBII());
        if(configration.calIsPlayerForTypeBII()){
            isPlayer = true;
            target = configration.getTarget();
        }
    }

    private void typeBI(){

        if(configration.calIsThereOnlyOneLeader()){
            System.out.println("Has one ");
            // Same size with only one leader
            if(configration.calIsPlayerForTypeBICaseZ()){
                isPlayer = true;
                target = configration.getTarget();
            }
        }else {
//            System.out.println("has two");
            if(configration.getSizeOfTheBlocks() == 2){
//                System.out.println("has caseI");
                typeBICaseI();
            } else {
                typeBICaseII();
            }
        }

    }

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

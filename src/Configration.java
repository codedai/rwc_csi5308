import jbotsim.Node;

import java.util.*;

/**
 * Created by Wange on 2017/12/2.
 */
public class Configration {

    private boolean hasIsolate = false;
    private boolean isIsolated = false;
    private int numberOfLeader = 0;

    private boolean inFirstStep = true;

    private String mySigmaPositive;
    private String mySigmaNegative;
    private String leadingView;
    private char[] chList;
    private int chListSize;
    private int numberOfRobots;

    private boolean isSymmetric;
    private boolean isLeader = false;
    private boolean isPlayer = false;


    private List<Node> vision;
    private ArrayList<String> visionList = new ArrayList<String>();
    private ArrayList<Block> blocks = new ArrayList<Block>();

    // Just a try a new class

    private ArrayList<CNode> cNodes = new ArrayList<CNode>();

    private int interDistance;
    private int maxSizeOfBlockNearAIsolated;
    private int minDistanceBetweenBlockAndIsolated;
    private int target;
    private int sizeOfTheBlocks;

    // TODO: 2017/12/4






    public Configration(List<Node> vision, int position) {
        String sigmaPositive = "";
        String sigmaNegative = "";
        int visionSize = vision.size();
        for(int i = 0; i < visionSize; i++) {
            sigmaNegative = sigmaNegative + ((RingNode)vision.get((visionSize + position - i) % visionSize)).getNumberOfRobots();
            sigmaPositive = sigmaPositive + ((RingNode)vision.get((position + i) % visionSize)).getNumberOfRobots();
            if(((RingNode)vision.get((position + i) % visionSize)).getNumberOfRobots() == 2) {
                inFirstStep = false;
            }
        }
        mySigmaNegative = sigmaNegative;
        mySigmaPositive = sigmaPositive;
        for(int i = 0; i < sigmaNegative.length(); i++) {
            String s1 = Algorithm.leftMove(sigmaPositive, i);
            String s2 = Algorithm.leftMove(sigmaNegative, sigmaNegative.length() - i);
            visionList.add(s1);
            visionList.add(s2);
            cNodes.add(new CNode(s1, s2));
        }

        ArrayList<String> tempVisionList;

        tempVisionList = (ArrayList<String>)visionList.clone();

        Collections.sort(tempVisionList);
//        System.out.println(tempVisionList);

//        for (String s :
//                visionList) {
//            System.out.println(s);
//        }
//
//        System.out.println();


        leadingView = tempVisionList.get(tempVisionList.size() - 1);
        isSymmetric = (leadingView.equals(tempVisionList.get(tempVisionList.size() - 2)));

        chList = getMySigmaPositive().toCharArray();
        chListSize = chList.length;

        int c = 0;
        for (int i = 0; i < chListSize; i++){
            if(chList[i] == '1'){
                c = c + 1;
            } else if(chList[i] == '2'){
                c = c + 2;
            }
        }

        numberOfRobots = c;
    }

    public void calInterDistance(){
        int interDistance = chList.length;
        int findIndex = 0;
        for(int i = 1; i < chList.length; i++) {
            if(chList[i] == '1') {
                int temp = i - findIndex;
                interDistance = Math.min(interDistance, temp);
                findIndex = i;
            }
        }
        int temp = chListSize - findIndex;
        interDistance = Math.min(interDistance, temp);
        this.interDistance = interDistance;
    }

    public void calIsIsolated() {
        boolean isIsolated = false;

        if(chList[interDistance] == '0' && chList[chListSize - interDistance] == '0') {
            isIsolated = true; }

        this.isIsolated = isIsolated;
        this.hasIsolate = isIsolated;
    }

    public void calBlocks() {
        int startIndex = 0;
        int stopIndex;
        int j = interDistance;
        boolean findingNewBlock = false;
        Block tempBlock = null;
        int i;
        for (i = 0; i < chListSize; i = i + j) {
            if(!findingNewBlock) {
                if (chList[i] == '0') {
                    stopIndex = i - j;
                    Block block = new Block(interDistance, startIndex, stopIndex);
                    for(int f = startIndex; f < stopIndex + 1;f++){
                        CNode cNode = cNodes.get(f);
                        cNode.calIsLeader(leadingView);
                        block.addCNode(cNode);
                    }
                    if(block.isolatedNode() && tempBlock != null){
                        this.hasIsolate = block.isolatedNode();
                    }
                    findingNewBlock = true;
                    j = 1;
                    block.setFirstFindNeighborBlock(tempBlock);
                    if(tempBlock != null){
                        tempBlock.setLastFindNeighborBlock(block);
                    }
                    tempBlock = block;
                    blocks.add(block);
                }
            } else {
                if (chList[i] == '1') {
                    startIndex = i;
                    j = interDistance;
                    findingNewBlock = false;
                }
            }
        }

        if(i >= chListSize && chList[i-j] == '1') {
            stopIndex = i - j;
            Block block = new Block(interDistance, startIndex, stopIndex);
            for(int f = startIndex; f < stopIndex + 1;f++){
                CNode cNode = cNodes.get(f);
                cNode.calIsLeader(leadingView);
                block.addCNode(cNode);
            }
            if(block.isolatedNode()){
                this.hasIsolate = block.isolatedNode();
            }
            block.setFirstFindNeighborBlock(tempBlock);
            if(tempBlock != null){
                tempBlock.setLastFindNeighborBlock(block);
            }
            tempBlock = block;
            blocks.add(block);
        }

        Block block = blocks.get(0);

        if(isIsolated && block.getFirstFindNeighborBlock() == null) {
            block.setFirstFindNeighborBlock(tempBlock);
            tempBlock.setLastFindNeighborBlock(block);
        } else if(chList[chListSize - interDistance] == '0') {
            block.setFirstFindNeighborBlock(tempBlock);
            tempBlock.setLastFindNeighborBlock(block);
        } else {
            Block newBlock = new Block(tempBlock, block);
            blocks.get(blocks.size()-2).setLastFindNeighborBlock(newBlock);
            blocks.get(1).setFirstFindNeighborBlock(newBlock);
            blocks.remove(blocks.size()-1);
            blocks.set(0, newBlock);
        }
    }

    public void calMaxBlockNearIsolated() {
        int maxSize = 0;
        ArrayList<Block> maxBlocksNearIsolated = new ArrayList<Block>();

        for(Block block : blocks){

            int blockSize = block.getSize();
            boolean isBlockFirstNeighborIsolated = block.getFirstFindNeighborBlock().isolatedNode();
            boolean isBlockLastNeighborIsolated = block.getLastFindNeighborBlock().isolatedNode();

            if(blockSize != 1 && (isBlockFirstNeighborIsolated || isBlockLastNeighborIsolated)) {
                if(blockSize > maxSize) {
                    maxSize = blockSize;
                    maxBlocksNearIsolated = new ArrayList<Block>();
                    maxBlocksNearIsolated.add(block);
                } else if(blockSize == maxSize) {
                    maxBlocksNearIsolated.add(block);
                }
            }
        }

        int minDistance = chListSize;

        for (Block block : maxBlocksNearIsolated) {
            int distanceBetweenFrontBlock = block.getFirstFindNeighborBlock().isolatedNode()? block.getStartIndex() - block.getFirstFindNeighborBlock().getStopIndex():chListSize;
            int distanceBetweenBackBlock = block.getLastFindNeighborBlock().isolatedNode()? block.getLastFindNeighborBlock().getStartIndex() - block.getStopIndex() : chListSize;
            minDistance = Math.min(minDistance, Math.min(Math.abs(distanceBetweenBackBlock), Math.abs(distanceBetweenFrontBlock)));
        }

        if(blocks.get(0).isolatedNode() && !blocks.get(0).getFirstFindNeighborBlock().isolatedNode() && blocks.get(0).getFirstFindNeighborBlock().getSize() == maxSize){
            minDistance = Math.min(minDistance, chListSize - blocks.get(0).getFirstFindNeighborBlock().getStopIndex());
        }


        this.maxSizeOfBlockNearAIsolated = maxSize;
        this.minDistanceBetweenBlockAndIsolated = minDistance;

//        System.out.println("maxSizeOfBlockNearAIsolated: " + maxSize + " minDistanceBetweenBlockAndIsolated: " + minDistance);

    }

    public void calIsPlayerInTypeA(){

        Block block = blocks.get(0);
        int distanceBetweenFrontBlock = chListSize - block.getFirstFindNeighborBlock().getStopIndex();
        int distanceBetweenBackBlock =  block.getLastFindNeighborBlock().getStartIndex() - block.getStopIndex();

        int frontBlockSize = block.getFirstFindNeighborBlock().getSize();
        int backBlockSize = block.getLastFindNeighborBlock().getSize();
//        System.out.print("frontBlockSize: " + frontBlockSize + " distanceBetweenFrontBlock: " + distanceBetweenFrontBlock + '\n');
//        System.out.print("BackBlockSize: " + backBlockSize + " distanceBetweenBackBlock: " + distanceBetweenBackBlock + '\n');

        if((frontBlockSize == maxSizeOfBlockNearAIsolated && distanceBetweenFrontBlock == minDistanceBetweenBlockAndIsolated) || (backBlockSize == maxSizeOfBlockNearAIsolated && distanceBetweenBackBlock == minDistanceBetweenBlockAndIsolated)) {

            this.isPlayer = true;
            if((frontBlockSize == maxSizeOfBlockNearAIsolated && distanceBetweenFrontBlock == minDistanceBetweenBlockAndIsolated) && (backBlockSize == maxSizeOfBlockNearAIsolated && distanceBetweenBackBlock == minDistanceBetweenBlockAndIsolated)){
                int i = new Random().nextInt(2);
                if (i == 0){
                    target = -1;
                } else {
                    target = 1;
                }
            } else if (frontBlockSize == maxSizeOfBlockNearAIsolated && distanceBetweenFrontBlock == minDistanceBetweenBlockAndIsolated) {
                target = -1;
            } else if (backBlockSize == maxSizeOfBlockNearAIsolated && distanceBetweenBackBlock == minDistanceBetweenBlockAndIsolated) {
                target = 1;
            }
        }


    }

    public boolean calHasNonleadingBlock(){
        for(Block b : blocks){
            if(!b.isLeadingBlock()) {
                return true;
            }
        }
        return false;
    }

    public boolean calAllBlocksInSameSize(){
        for(int i = 0; i < (blocks.size() - 1); i++) {
            if(blocks.get(i).getSize() != blocks.get(i+1).getSize()){
                return false;
            }
        }
        this.sizeOfTheBlocks = blocks.get(0).getSize();
        return true;
    }

    public boolean calIsThereOnlyOneLeader(){
        int count = 0;
        for(Block b : blocks){
            if(b.isLeadingBlock()) {
               count++;
            }
        }
        return count == 1;
    }

    public boolean calIsPlayerForTypeBII() {
        int minSizeOfBlock = chListSize;
        int maxSizeofBlcokNearS = 0;
        for(Block b : blocks){
            minSizeOfBlock = Math.min(minSizeOfBlock, b.getSize());
        }

        for (Block b : blocks){
            maxSizeofBlcokNearS = Math.max(maxSizeofBlcokNearS, Math.max(b.getFirstFindNeighborBlock().getSize(), b.getLastFindNeighborBlock().getSize()));
        }

        int minDistanceBetweenSAnds = chListSize;
        for(Block block : blocks) {
            if(block.getSize() == minSizeOfBlock){
                System.out.println("<><><><><>");
                int distanceBetweenFrontBlock = block.getFirstFindNeighborBlock().getSize() == maxSizeofBlcokNearS ? (chListSize + block.getStartIndex() - block.getFirstFindNeighborBlock().getStopIndex()) % chListSize :chListSize;
                int distanceBetweenBackBlock = block.getLastFindNeighborBlock().getSize() == maxSizeofBlcokNearS ? (chListSize + block.getLastFindNeighborBlock().getStartIndex() - block.getStopIndex()) % chListSize :chListSize;
                minDistanceBetweenSAnds = Math.min(minDistanceBetweenSAnds, Math.min(Math.abs(distanceBetweenBackBlock), Math.abs(distanceBetweenFrontBlock)));
            }
        }

//        System.out.println("minSizeOfBlock: " + minSizeOfBlock);
//        System.out.println("maxSizeofBlcokNearS: " + maxSizeofBlcokNearS);
//        System.out.println("minDistanceBetweenSAnds: " + minDistanceBetweenSAnds);

        Block thisBlock = blocks.get(0);
//        System.out.println(thisBlock.getStartIndex() + " && " + thisBlock.getFirstFindNeighborBlock().getSize() + " && " + (chListSize - thisBlock.getFirstFindNeighborBlock().getStopIndex()));
//        System.out.println((thisBlock.getStartIndex() == '0' && thisBlock.getFirstFindNeighborBlock().getSize() == maxSizeofBlcokNearS && (chListSize - thisBlock.getFirstFindNeighborBlock().getStopIndex()) == minDistanceBetweenSAnds));
//        System.out.println(thisBlock.getStopIndex() + " && " + thisBlock.getLastFindNeighborBlock().getSize() + "&&" + (thisBlock.getLastFindNeighborBlock().getStartIndex() - thisBlock.getStopIndex()));
//        System.out.println(thisBlock.getStopIndex() == '0' && thisBlock.getLastFindNeighborBlock().getSize() == maxSizeofBlcokNearS && (thisBlock.getLastFindNeighborBlock().getStartIndex() - thisBlock.getStopIndex()) == minDistanceBetweenSAnds);
        if(thisBlock.getSize() == minSizeOfBlock){
            if(thisBlock.getStartIndex() == 0 && thisBlock.getFirstFindNeighborBlock().getSize() == maxSizeofBlcokNearS && (chListSize - thisBlock.getFirstFindNeighborBlock().getStopIndex()) == minDistanceBetweenSAnds){
                this.target = -1;
                return true;
            }
            if(thisBlock.getStopIndex() == 0 && thisBlock.getLastFindNeighborBlock().getSize() == maxSizeofBlcokNearS && (thisBlock.getLastFindNeighborBlock().getStartIndex() - thisBlock.getStopIndex()) == minDistanceBetweenSAnds){
                this.target = 1;
                return true;
            }
        }



        return false;
    }

    public boolean calIsPlayerForTypeBICaseZ(){
        Block thisBlock = blocks.get(0);
        if(this.mySigmaNegative.equals(leadingView) || this.mySigmaPositive.equals(leadingView)){
            if(thisBlock.getStartIndex() == 0){
                this.target = -1;
                return true;
            }
            if(thisBlock.getStopIndex() == 0){
                this.target = 1;
                return true;
            }
        }
        return false;
    }

    public boolean calIsPlayerForTypeBICaseI(){
        // Same size which is 2 with two leaders
        Block thisBlock = blocks.get(0);
        String mySmallestView = getMySmallestView();

        if(mySmallestView.equals(thisBlock.getSmallestView())) {

            int numberOfBlock = (int)Math.ceil((double)numberOfRobots/(double)2);
            System.out.println("numberOfBlock " + numberOfRobots);
            for(int i = 4; i < numberOfBlock - 3; i++) {
                System.out.println(mySmallestView);
                System.out.println(blocks.get(i).getSmallestView());
                if (blocks.get(i).getSmallestView().equals(mySmallestView)){
//                    System.out.println(mySmallestView);
//                    System.out.println(thisBlock.getSmallestView());
                    if(thisBlock.getStartIndex() == 0){
                        this.target = -1;
                        return true;
                    }
                    if(thisBlock.getStopIndex() == 0){
                        this.target = 1;
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public String getMySmallestView() {
        return (mySigmaNegative.compareTo(mySigmaPositive) < 0)? mySigmaNegative:mySigmaPositive;
    }

    public boolean calHasIsolated(){
        for(Block b : blocks) {
            if (b.isolatedNode()){
                this.hasIsolate = true;
                return true;
            }
        }
        this.hasIsolate = false;
        return false;
    }

    public boolean isHasIsolate() {
        return hasIsolate;
    }

    public void setHasIsolate(boolean hasIsolate) {
        this.hasIsolate = hasIsolate;
    }

    public boolean isolated() {
        return isIsolated;
    }

    public void setIsolated(boolean isolated) {
        isIsolated = isolated;
    }

    public int getNumberOfLeader() {
        return numberOfLeader;
    }

    public void setNumberOfLeader(int numberOfLeader) {
        this.numberOfLeader = numberOfLeader;
    }

    public boolean isLeader() {
        return isLeader;
    }

    public void setLeader(boolean leader) {
        isLeader = leader;
    }

    public boolean isPlayer() {
        return isPlayer;
    }

    public void setPlayer(boolean player) {
        isPlayer = player;
    }

    public boolean isInFirstStep() {
        return inFirstStep;
    }

    public void setInFirstStep(boolean inFirstStep) {
        this.inFirstStep = inFirstStep;
    }

    public String getMySigmaPositive() {
        return mySigmaPositive;
    }

    public void setMySigmaPositive(String mySigmaPositive) {
        this.mySigmaPositive = mySigmaPositive;
    }

    public String getMySigmaNegative() {
        return mySigmaNegative;
    }

    public void setMySigmaNegative(String mySigmaNegative) {
        this.mySigmaNegative = mySigmaNegative;
    }

    public List<Node> getVision() {
        return vision;
    }

    public void setVision(List<Node> vision) {
        this.vision = vision;
    }

    public ArrayList<String> getVisionList() {
        return visionList;
    }

    public ArrayList<Block> getBlocks() {
        return blocks;
    }

    public int getTarget() {
        return target;
    }

    public void setTarget(int target) {
        this.target = target;
    }

    public boolean isSymmetric() {
        return isSymmetric;
    }

    public void setSymmetric(boolean symmetric) {
        isSymmetric = symmetric;
    }

    public int getSizeOfTheBlocks() {
        return sizeOfTheBlocks;
    }

    public void setSizeOfTheBlocks(int sizeOfTheBlocks) {
        this.sizeOfTheBlocks = sizeOfTheBlocks;
    }

    @Override
    public String toString() {
        return "Configration{" +
                "hasIsolate=" + hasIsolate +
                ", isIsolated=" + isIsolated +
                ", numberOfLeader=" + numberOfLeader +
                ", inFirstStep=" + inFirstStep +
                ", mySigmaPositive='" + mySigmaPositive + '\'' +
                ", mySigmaNegative='" + mySigmaNegative + '\'' +
                ", leadingView='" + leadingView + '\'' +
                ", chList=" + Arrays.toString(chList) +
                ", chListSize=" + chListSize +
                ", isSymmetric=" + isSymmetric +
                ", isLeader=" + isLeader +
                ", isPlayer=" + isPlayer +
                ", vision=" + vision +
                ", visionList=" + visionList +
                ", blocks=" + blocks +
                ", cNodes=" + cNodes +
                ", interDistance=" + interDistance +
                ", maxSizeOfBlockNearAIsolated=" + maxSizeOfBlockNearAIsolated +
                ", minDistanceBetweenBlockAndIsolated=" + minDistanceBetweenBlockAndIsolated +
                ", target=" + target +
                '}';
    }
}

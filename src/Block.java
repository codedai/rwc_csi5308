import java.util.ArrayList;

/**
 * Created by Wange on 2017/12/2.
 */
public class Block {
    private int startIndex;
    private int stopIndex;
    private int size;
    private int interDistance;
    private Block firstFindNeighborBlock;
    private Block lastFindNeighborBlock;

    private boolean isolatedNode;
    private boolean isLeadingBlock;

    public Block(int interDistance, int startIndex) {
        this.startIndex = startIndex;
        this.interDistance = interDistance;
    }

    public Block(int interDistance, int startIndex, int stopIndex) {
        this.interDistance =interDistance;
        this.startIndex = startIndex;
        this.stopIndex = stopIndex;
        this.isolatedNode = (startIndex == stopIndex);
        this.size = this.isolatedNode? 1 : (stopIndex - startIndex) / interDistance + 1;
    }

    public Block(Block firstBlock, Block lastBlock){
        this.interDistance = firstBlock.getInterDistance();
        this.startIndex = firstBlock.getStartIndex();
        this.stopIndex = lastBlock.getStopIndex();
        this.size = firstBlock.getSize() + lastBlock.getSize();
        this.isolatedNode = false;
        this.firstFindNeighborBlock = firstBlock.getFirstFindNeighborBlock();
        this.lastFindNeighborBlock = lastBlock.getLastFindNeighborBlock();
    }

    @Override
    public String toString() {
        return "Block{" +
                "startIndex=" + startIndex +
                ", stopIndex=" + stopIndex +
                ", size=" + size +
                ", interDistance=" + interDistance +
                ", isolatedNode=" + isolatedNode +
                ", isLeadingBlock=" + isLeadingBlock +
                '}';
    }

    public int getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }

    public int getStopIndex() {
        return stopIndex;
    }

    public void setStopIndex(int stopIndex) {
        this.stopIndex = stopIndex;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getInterDistance() {
        return interDistance;
    }

    public void setInterDistance(int interDistance) {
        this.interDistance = interDistance;
    }

    public Block getLastFindNeighborBlock() {
        return lastFindNeighborBlock;
    }

    public void setLastFindNeighborBlock(Block lastFindNeighborBlock) {
        this.lastFindNeighborBlock = lastFindNeighborBlock;
    }

    public Block getFirstFindNeighborBlock() {
        return firstFindNeighborBlock;
    }

    public void setFirstFindNeighborBlock(Block firstFindNeighborBlock) {
        this.firstFindNeighborBlock = firstFindNeighborBlock;
    }

    public boolean isolatedNode() {
        return isolatedNode;
    }

    public void setIsolatedNode(boolean isolatedNode) {
        this.isolatedNode = isolatedNode;
    }

    public boolean isLeadingBlock() {
        return isLeadingBlock;
    }

    public void setLeadingBlock(boolean leadingBlock) {
        isLeadingBlock = leadingBlock;
    }
}

/**
 * Created by Wange on 2017/12/4.
 */
public class CNode {

    private String pointVisonI;
    private String pointVisonII;

    private boolean isLeader;

    public CNode(String pointVisionI, String pointVisonII) {
        this.pointVisonI = pointVisionI;
        this.pointVisonII = pointVisonII;
    }

    public String getPointVisonI() {
        return pointVisonI;
    }

    public void setPointVisonI(String pointVisonI) {
        this.pointVisonI = pointVisonI;
    }

    public String getPointVisonII() {
        return pointVisonII;
    }

    public void setPointVisonII(String pointVisonII) {
        this.pointVisonII = pointVisonII;
    }

    public boolean isLeader() {
        return isLeader;
    }

    public void setIsLeader(boolean isLeader) {
        isLeader = isLeader;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CNode)) return false;

        CNode cNode = (CNode) o;

        if (isLeader != cNode.isLeader) return false;
        if (!pointVisonI.equals(cNode.pointVisonI)) return false;
        return pointVisonII.equals(cNode.pointVisonII);

    }

    public void calIsLeader(String leadingVision) {
        this.isLeader = (pointVisonI.equals(leadingVision) || pointVisonII.equals(leadingVision));
    }
}

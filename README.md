# Project Introduction 
## Indroduce
For the project I choose to realize the algorithm in [Computing Without Communicating: Ring Exploration by Asynchronous Oblivious Robots](https://link.springer.com/book/10.1007/978-3-540-77096-1#page=117), by using [jbotsim.jar](http://jbotsim.sourceforge.net/)

The author provide an algorithm for exploring an anonymous ring of size n by k oblivious anonymous asynchronous robots scattered in the ring.

There are some restrictions for the algorithm. 


- Robots: endowed with vision but unable to communicate. 
- The robot can not remember any thing after one Look-Computer-Move operation.
- Within finite time and regardless of the initial placement of the robots, each node must be visited by at least one robot and the robots must be in a configuration in which they all remain idle. 
- The configurations start in a no-tower situation.
- The number of robots is bigger than 17, and gcd(k,n) =1

In the following section, I will explain the main code in the project.

## Code Explanation
The main idea in the project is making every robot to judge whether itself is the player. And the number of the ring node does not change after initialization.

### Robot Class
The robot "LOOK" during `onPreClock()`, "COMPUTE" during `onClock`, "MOVE" during `onPostClock`.
	
#### Vision

Although the robot does not have memory, to realize the ability -- "vision" -- there are two variable can be remembered by the robots, in order to make the robot have the "vision".

- `List<Node> vision` which is used to store the configuration. Because the robots can "SEE" the configuration, in my opinion, this means that, when the robot perform "LOOK" there is no message on in the graph. I just store it after initialization, and make it unchanged after every operation.
- `int position` which is used to store the position of the robot, that is the index of the ring node in `vision` the robot on. This variable is stored after first adding the robot and changed after every "MOVE" step.

This two variables are used to generate the **relative layout** of the graph of the robot itself, which can not be remembered and used in the computation.


```java
Class Main
    public static void initRobotPostion(Topology topology, int robotSize) {
        List<Node> ringNodes = topology.getNodes();
        ...
        Robot robot = new Robot(ringNodes, tempNode.getID());
        ...
    }
   
Class Robot 
   public Robot(List<Node> vision, int id) {
       ...
        this.vision = vision;
        this.position = id;
    }
```

#### Look
The robot preform "LOOK" during `onPreClock()`, if the robot does not shut down, the robot get the view of the graph by `configration = new Configration(vision, position);`
This will help the robot get the $\delta^+ $ and $\delta^- $, which is view of the ring.
In the step the robot will also know if there are tower in the ring.

#### Compute

The robot preform "Compute" during `onClock()`. The robot will compute which step it is on. and compute whether it is the player or not.

```java
Class Robot
    public void onClock(){      // COMPUTE
            if(inFirstStep) { //Set up phase
                setUp();}
                else {Exploration();}}
    }
    
    private void setUp() {
        if(configration.calHasIsolated()) {
            // Type A
            setUpPhaseTypeA();
        } else {
            if (configration.calHasNonleadingBlock()){
                // Type B
                setUpPhaseTypeB();
            } else {
                if(configration.getInterDistance() > 1){
                    // Type C
                    setUpPhaseTypeC();
                }else {
                    // Set up is done, move to Tower-Creation Phase
                    TowerCreation();} } }
    }
    private void setUpPhaseTypeB(){
        if(configration.calAllBlocksInSameSize()){typeBI();} 
        else { typeBII(); }
    }
    private void typeBI(){
        if(configration.calIsThereOnlyOneLeader()){
        ...
        }else {
            if(configration.getSizeOfTheBlocks() == 2){typeBICaseI();} 
            else { typeBICaseII();}
        }

    }
```
After the judgement, the robot will know whether it is the player, and if it is the player, it will know there move direction.

#### Move
The robot move based on the computation result in "Compute", and the move is preformed in `onPreClock`.
In the paper, 
> The only constraint is that moves are instantaneous, and hence any robot performing a Look operation sees all other robots at nodes of the ring and not on edges.
 
So in the code, the robot just use the position of the target node, and reset the position of the robot itself.


```
Class Robot

    public void onPostClock(){  // MOVE
        if(!stopFlag) {
            System.out.print("-Move" + "\n");
            if(isPlayer) {
            	int temp = position + target;
                Node node = vision.get((vision.size() +temp) % vision.size());
                List<Node> onNode = this.getSensedNodes();
                for(int i = 0; i < onNode.size(); i++){
                    RingNode n = (RingNode)onNode.get(i);
                    n.setLeft();
                }
                setLocation(node.getX(), node.getY());
                onNode = this.getSensedNodes();
                for(int i = 0; i < onNode.size(); i++){
                    if(onNode.get(i).getClass().toString().equals("class RingNode")){
                        RingNode n = (RingNode)onNode.get(i);
                        n.setVisting();
                    }
                }
                this.position = (vision.size() +temp) % vision.size();
            }
            isPlayer = false;
        }
    }
```

### Configration Class
In this class I provide some approach. This class store the view of the robot it belongs to, include deltaPositive, deltaNegative, lead view, number of robots, interdistance etc. And this class will be renewed by the robot at the start of "LOOK-COMPUTE-MOVE". The judge methods are also defined in this class.

#### Get view (init)

```
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
```

#### Calculation
The calculation includes

- get all the block
- the number of the block in the configuration
- the interdistance of the configuration
- find the max size block
- find the max size block neighbor to isolated robot

#### Judgement
The judgement include

- is the robot the player
- is the robot isolate
- is the robot leader
- is there non-leading-block
- is all block in same size
- is there only one leader
- is the configuration symmetric
- does the robot have the smallest view in the block


## Conclusion
I generally realize the algorithm, and I set some test in it, and result goes well. Every time the robots can explore the ring. But sometime the code will break down, but when I restart the program in the same situation the code goes well, I will try to find the reason why this happen.





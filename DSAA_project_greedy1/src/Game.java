import java.util.*;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdRandom;

public class Game {

    private final State initState;
    private ArrayList<State> occurredStates;
    private ArrayList<State> yesStates;
    private ArrayList<int[]> steps;
    private Stack<Integer> MHTCheckList;
    private static final int[][] UP_DOWN_LEFT_RIGHT = {{-1,0}, {1,0}, {0,-1}, {0,1}};
    private final State winState;
    private int movedStepNum;
    private final int grdyPeriod = 50;

    public Game(int[][] panel, int[][] complexBlocks){

        Hashtable<int[],int[]> complexBlock=new Hashtable<>();
        for (int i = 0; i < complexBlocks.length; i++) {
            int[] size=new int[2];
            for (int j = 0; j < 2; j++) {
                size[0]=complexBlocks[i][1];
                size[1]=complexBlocks[i][2];
            }
            int[] location=new int[2];
            for (int j = 0; j < panel.length ; j++) {
                for (int k = 0; k < panel[0].length; k++) {
                    if (panel[j][k]==complexBlocks[i][0]){
                        location[0]=j;
                        location[1]=k;
                    }
                }
            }

            complexBlock.put(location,size);
        }
        //set complex blocks to hashtable

        initState=new State(panel,complexBlock);

        // todo: initialize the game's fields
        int[][] winPanel=new int[initState.width][initState.length];
        int allNumber = initState.width*initState.length-initState.getZeroLocs().length;
        for (int i = 0; i < initState.width ; i++) {
            for (int j = 0; j <initState.length ; j++) {
                if (i*initState.length+j+1 <= allNumber ) winPanel[i][j]=initState.length*i+j+1;
            }
        }

        Hashtable<int[], int[]> winComplexBlocks = new Hashtable<>();
        for (int[] clxBlock:complexBlocks) {
            int iClx = clxBlock[0]/initState.length;
            int jClx = clxBlock[0]%initState.length-1;
            int[] locClx = new int[2];
            locClx[0] = iClx;
            locClx[1] = jClx;
            int[] size = new int[2];
            size[0] = clxBlock[1];
            size[1] = clxBlock[2];
            winComplexBlocks.put(locClx, size);
        }
        //todo: ??? location repeat several time??
        this.winState =new State(winPanel,winComplexBlocks);
        occurredStates = new ArrayList<>();
        yesStates = new ArrayList<>();
        steps = new ArrayList<>();
        occurredStates.add(initState);
        yesStates.add(initState);
        movedStepNum = 0;
        MHTCheckList = new Stack<>();
        MHTCheckList.push(initState.getMHTDis());


    }


    class State  {

        private final int[][] panel;
        private final Hashtable<int[], int[]> complexBlocks;

        private final int width;
        private final int length;
        State(int[][] panel, Hashtable<int[], int[]> complexBlocks){
            this.panel = panel.clone();
            this.complexBlocks = complexBlocks;
            this.width = panel.length;
            this.length = panel[0].length;
        }

        public int[][] getPanel() {
            return panel.clone();
        }
        
        public int[][] getZeroLocs() {
            ArrayList<int[]> zeroLocations = new ArrayList<>();
            for (int i = 0; i < panel.length; i++) {
                for (int j = 0; j <panel[0].length ; j++) {//
                    if (panel[i][j]==0){
                        int[] zeroLoc = new int[2];
                        zeroLoc[0] = i;
                        zeroLoc[1] = j;
                        zeroLocations.add(zeroLoc);
                    }
                }
            }
            int[][] zeroLocs = new int[zeroLocations.size()][];
            for (int i = 0; i < zeroLocs.length; i++) {
                zeroLocs[i] = zeroLocations.get(i);
            }
            return zeroLocs;
        }

        public Hashtable<int[], int[]> getComplexBlocks() { return complexBlocks; }


        public boolean canNormMove(int i, int j, int[] direction){
            
            int height = this.panel.length;
            int width = this.panel[0].length;
            if ((i+direction[0]<0)||
                (i+direction[0]>=height)||
                (j+direction[1]<0)||
                (j+direction[1]>=width)) return false;
            
            if (this.panel[i+direction[0]][j+direction[1]]==0) return false;
            
            int iNext = i + direction[0];
            int jNext = j + direction[1];
            for ( int[] clxLoc: this.complexBlocks.keySet()) {
                int hi = this.complexBlocks.get(clxLoc)[0];
                int wi = this.complexBlocks.get(clxLoc)[1];
                for (int m = 0; m < hi; m++) {
                    for (int n = 0; n < wi; n++) {
                        if ( (iNext==clxLoc[0]+m)&&(jNext==clxLoc[1]+n) ) return false;
                    }
                }
            }

            return true;
        }


        public boolean canClxMove(int iClx, int jClx, int[] direction){

            int rowMove = direction[0];
            int colMove = direction[1];
            int height = this.panel.length;
            int width = this.panel[0].length;
            int hi = 0, wi = 0;
            for (int[] clxLoc:this.complexBlocks.keySet()) {
                if ((clxLoc[0]==iClx)&&(clxLoc[1]==jClx)){
                    hi = this.complexBlocks.get(clxLoc)[0];
                    wi = this.complexBlocks.get(clxLoc)[1];
                    break;
                }
            }

            if ( (rowMove==-1)&&(colMove==0) ){
                if ( iClx - 1 < 0 ) return false;
                for (int n = 0; n < wi; n++) {
                    if ( this.panel[iClx-1][jClx+n]!=0 ) return false;
                }
            } else if ( (rowMove==1)&&(colMove==0) ) {
                if ( iClx + hi >= height ) return false;
                for (int n = 0; n < wi; n++) {
                    if ( this.panel[iClx+hi][jClx+n]!=0 ) return false;
                }
            } else if ( (rowMove==0)&&(colMove==-1) ) {
                if ( jClx - 1 < 0 ) return false;
                for (int m = 0; m < hi; m++) {
                    if ( this.panel[iClx+m][jClx-1]!=0 ) return false;
                }
            } else {
                if ( jClx + wi >= width ) return false;
                for (int m = 0; m < hi; m++) {
                    if ( this.panel[iClx+m][jClx+wi]!=0 ) return false;
                }
            }

            return true;
        }


        public boolean equal(State state){

            if (!Arrays.deepEquals(this.getZeroLocs(),state.getZeroLocs()))return false;//zeroLoc

            if (this.complexBlocks.size()!=0) {
                for(Map.Entry<int[],int[]> entry: state.complexBlocks.entrySet()){
                    int[] thisAnswer = this.complexBlocks.get(entry.getKey());
                    int[] stateAnswer = entry.getValue();
                    if(thisAnswer != null && stateAnswer != null && !Arrays.equals(stateAnswer, thisAnswer)){
                        return false;
                    }
                }//compare complex block ???
            }

            for (int i = 0; i < panel.length ; i++) {
                for (int j = 0; j < panel[0].length; j++) {
                    if (state.panel[i][j]!=this.panel[i][j])return false;
                }
            }
            return true;
        }


        public State cloneState(){

            int[][] newPanel=new int[width][length];
            Hashtable<int[],int[]> newComplexBlock=new Hashtable<>();

            for (int i = 0; i <width ; i++) {
                for (int j = 0; j < length; j++) {
                    newPanel[i][j]=this.panel[i][j];
                }
            }//copy panel

            for(int[] clxLoc : this.complexBlocks.keySet()){
                for (int i = 0; i < 2; i++) {
                    newComplexBlock.put(clxLoc, this.complexBlocks.get(clxLoc));
                }
            }//copy complex blocks


            State newState=new State(newPanel,newComplexBlock);
            return newState;
        }


        private int[] winLoc(int num){
            int[] winLoc = new int[2];
            winLoc[0] = num / initState.length;
            winLoc[1] = num % initState.length-1;
            return winLoc;
        }


        private int getMHTDis(){

            int sum=0;
            for (int i = 0; i < panel.length ; i++) {
                for (int j = 0; j < panel[0].length ; j++) {
                    sum+=Math.abs(findLocation(panel[i][j])[0]-i)+   //x
                            Math.abs(findLocation(panel[i][j])[1]-j);  //y
                }
            }

            return sum;
        }


        public int[] findLocation(int num){
            int[] location=new int[2];
            location[0]=-1;
            location[1]=-1;
            for (int i = 0; i < panel.length; i++) {
                for (int j = 0; j < panel[0].length; j++) {
                    if (panel[i][j]==num) {

                        location[0]=i;
                        location[1]=j;
                        return location;
                    }

                }
            }return location;//-1,-1 not found
        }


    }

    public boolean isOccurred(State state){
        for (State s : occurredStates) {
            if (state.equal(s)) return true;
        }
        return false;
    }


    public ArrayList<State> getSolution(){
        this.solve(initState);
        boolean isSolvable = occurredStates.get(occurredStates.size()-1).equal(winState);
        if (isSolvable) {
            System.out.println("Yes");
            System.out.println(steps.size());
            for (int[] step:steps) {
                char direction;

                if (step[2]==0){
                    if (step[1]==1) direction = 'D';
                    else direction = 'U';
                }
                else {
                    if (step[2]==1) direction = 'R';
                    else direction = 'L';
                }
                System.out.println(step[0]+" "+direction);
            }

            return yesStates;
        }
        else {
            System.out.println("No");
            return null;
        }

    }


    private void solve(State currentState){
        /*if (currentState.getMHTDis()==2){
            solveM2Trivial(currentState);
        }*/
        if (occurredStates.get(occurredStates.size()-1).equal(winState)) return;
        int[][] directions = UP_DOWN_LEFT_RIGHT.clone();
        StdRandom.shuffle(directions);
        for (int[] clxBlockLoc : currentState.getComplexBlocks().keySet()){
            for (int[] direction : directions) {
                int iClx = clxBlockLoc[0];
                int jClx = clxBlockLoc[1];
                if (currentState.canClxMove(iClx, jClx, direction)){
                    this.complexTry(currentState, iClx, jClx, direction);
                    if (occurredStates.get(occurredStates.size()-1).equal(winState)) return;
                }
            }
        }
            // do normal move

        for (int[] zeroLoc : currentState.getZeroLocs()) {
            for (int[] direction : directions) {
                int i = zeroLoc[0];
                int j = zeroLoc[1];
                if (currentState.canNormMove(i, j, direction)){
                    this.normalTry(currentState, i, j, direction);
                    if (occurredStates.get(occurredStates.size()-1).equal(winState)) return;
                }
            }
        }

        if (!steps.isEmpty()) steps.remove(steps.size()-1);
        if (!yesStates.isEmpty()) yesStates.remove(yesStates.size()-1);


    }


    /*private void solveM2Trivial(State currentState){

        int[] badNumbers = new int[2];
        for (int i = 0, add = 0; i < currentState.panel.length; i++) {
            for (int j = 0; j < currentState.panel[0].length; j++) {
                if ( (currentState.panel[i][j]!=0) &&
                        (i!=currentState.winLoc(currentState.panel[i][j])[0] ||
                                j!=currentState.winLoc(currentState.panel[i][j])[1]) ) {
                    badNumbers[add++] = currentState.panel[i][j];
                }
            }
        }

        for (int num:badNumbers) {
            int i = currentState.findLocation(num)[0];
            int j = currentState.findLocation(num)[1];
            for (int[] clxLoc : currentState.complexBlocks.keySet()) {
                if (Arrays.equals(clxLoc, currentState.findLocation(num))){
                    complexTry(currentState, i, j,
                            new int[]{currentState.winLoc(num)[0]-i,currentState.winLoc(num)[1]-j});
                    return;
                }
            }

        }

    }*/


    private void normalTry(State currentState, int i, int j, int[] direction){

        State nextState = currentState.cloneState();
        nextState.panel[i][j] = nextState.panel[i+direction[0]][j+direction[1]];
        nextState.panel[i+direction[0]][j+direction[1]] = 0;

        if (!this.isOccurred(nextState)){
            occurredStates.add(nextState);
            if (movedStepNum%grdyPeriod!=grdyPeriod-1||nextState.getMHTDis()<MHTCheckList.peek()){
                if (movedStepNum%grdyPeriod==grdyPeriod-1) MHTCheckList.push(nextState.getMHTDis());
                int[] step = new int[3];
                step[0] = currentState.panel[i+direction[0]][j+direction[1]];
                step[1] = - direction[0];
                step[2] = - direction[1];
                this.steps.add(step);
                yesStates.add(nextState);
                movedStepNum++;
                this.solve(nextState);
                movedStepNum--;
                if (movedStepNum%grdyPeriod==grdyPeriod-1) MHTCheckList.pop();
            }
        }


    }



    private void complexTry(State currentState, int iClx, int jClx, int[] direction){

        State nextState = currentState.cloneState();

        int width = 0, height = 0;
        for (int[] loc : currentState.complexBlocks.keySet()) {
            if (Arrays.equals(loc, new int[]{iClx, jClx})){
                width = currentState.complexBlocks.get(loc)[1];
                height = currentState.complexBlocks.get(loc)[0];
                break;
            }
        }

        int[][] move = new int[height][width];
        for (int k = 0; k < height; k++) {
            for (int l = 0; l < width; l++) {
                 move[k][l] = nextState.panel[iClx+k][jClx+l];
            }
        }
        for (int k = 0; k < height; k++) {
            for (int l = 0; l < width; l++) {
                nextState.panel[iClx+k+direction[0]][jClx+l+direction[1]] = move[k][l];
            }
        }
        
        if (direction[1]==0){
            for (int k = 0; k < width; k++) {
                nextState.panel[iClx+(1-direction[0])/2*(height-1)][jClx+k] = 0;
            }
        } else {
            for (int k = 0; k < height; k++) {
                nextState.panel[iClx+k][jClx+(1-direction[1])/2*(width-1)] = 0;
            }
        }

        for (int[] clxLoc:nextState.complexBlocks.keySet()) {
            if (Arrays.equals(clxLoc, new int[]{iClx, jClx})){
                int[] size = nextState.complexBlocks.get(clxLoc);
                int[] newClxLoc = new int[]{iClx + direction[0], jClx + direction[1]};
                nextState.complexBlocks.remove(clxLoc);
                nextState.complexBlocks.put(newClxLoc, size);
                break;
            }
        }

        if (!this.isOccurred(nextState)){
            occurredStates.add(nextState);
            if (movedStepNum%grdyPeriod!=grdyPeriod-1||nextState.getMHTDis()<MHTCheckList.peek()){
                if (movedStepNum%grdyPeriod==grdyPeriod-1) MHTCheckList.push(nextState.getMHTDis());
                int[] step = new int[3];
                step[0] = currentState.panel[iClx][jClx];
                step[1] = direction[0];
                step[2] = direction[1];
                this.steps.add(step);
                yesStates.add(nextState);
                movedStepNum++;
                this.solve(nextState);
                movedStepNum--;
                if (movedStepNum%grdyPeriod==grdyPeriod-1) MHTCheckList.pop();
            }
        }

    }




    public boolean whetherEnd(int[][]panel){
        boolean flag;
        int count=0;
        ArrayList<String> num=new ArrayList<>();
        for (int i = 0; i < panel.length ; i++) {
            for (int j = 0; j < panel[0].length; j++) {

                if ((panel[i][j]!=winState.panel[i][j]&&panel[i+1][j]!=winState.panel[i+1][j])||
                        (panel[i][j]!=winState.panel[i][j]&&panel[i][j+1]!=winState.panel[i][j+1]))
                    return true;//end

            }
        }
        return false;
    }


}


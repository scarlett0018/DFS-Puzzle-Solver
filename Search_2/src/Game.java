import java.util.*;

public class Game {
    public Tree root;
    private ArrayList<Tree> winPath = new ArrayList<>();
    public ArrayList<Integer> occurredIDs = new ArrayList<>();


    public int[][] winPanel;
    public int[] shape;

    public Game(int[][] panel, int[][] complexBlocks) {
        int n = panel.length;
        int m = panel[0].length;
        int size = n*m;
        this.shape = new int[size + 1];

        for(int[] block : complexBlocks){
            int id = block[0];
            int _n = block[1];
            int _m = block[2];

            if(_n == 1 && _m == 2){
                shape[id] = 1;
            }else if(_n == 2 && _m == 1){
                shape[id] = 3;
            }else{
                shape[id] = 5;
            }

        }

        this.root = new Tree(panel);
        for(int i = 0; i < root.panel.length; i++){
            for(int j = 0; j < root.panel[0].length; j++){
                int id1 = panel[i][j];
                occurredIDs.add(id1);

                if(shape[id1] != 0){
                    switch (shape[id1]){
                        case 1:
                            int id2 = panel[i][j + 1];
                            shape[id2] = 2;
                            break;
                        case 3:
                            int id4 = panel[i + 1][j];
                            shape[id4] = 4;
                            break;
                        case 5:
                            int id6 = panel[i][j + 1];
                            int id7 = panel[i + 1][j];
                            int id8 = panel[i + 1][j + 1];
                            shape[id6] = 6;
                            shape[id7] = 7;
                            shape[id8] = 8;
                            break;
                    }
                }
            }
        }

        this.generatedWinPanel();
    }


    public void visualPath(ArrayList<Tree> lst){
        for(Tree s : lst){
            int[][] panel = s.panel;
            int n = panel.length;
            int m = panel[0].length;
            for (int[] ints : panel) {
                for (int j = 0; j < m; j++) {
                    System.out.print(ints[j]);
                    System.out.print(" ");
                }
                System.out.println();
            }
        }
    }


    public static int manhattonDis(int[][] panel) {
        int sum = 0;

        for (int i = 0; i < panel.length; i++) {
            for (int j = 0; j < panel[0].length; j++) {
//                int part=0;
                sum += Math.abs(findLocation(panel, panel[i][j])[0] - i) +   //x
                        Math.abs(findLocation(panel, panel[i][j])[1] - j);  //y

            }
        }
        return sum;
    } public static int[] findLocation(int[][] panel, int num) {
        int[] location = new int[2];
        location[0] = -1;
        location[1] = -1;
        for (int i = 0; i < panel.length; i++) {
            for (int j = 0; j < panel[0].length; j++) {
                if (panel[i][j] == num) {

                    location[0] = i;
                    location[1] = j;
                    return location;
                }

            }
        }
        return location;//-1,-1 not found
    }

    public int[][] generatedWinPanel(){
        int n = this.root.panel.length;
        int m = this.root.panel[0].length;
        int[][] panel = new int[n][m];
        for(int i : this.occurredIDs){
            if(i == 0) continue;
            int x = (i - 1)/n;
            int y = (i - 1)%m;
            panel[x][y] = i;
        }
        this.winPanel = panel;
        return panel;
    }

    public static void printStep(ArrayList<Tree> winPath){
        for(Tree node : winPath){
            int[][] panel = node.panel;
            for(int[] row : panel){
                for(int num : row){
                    System.out.print(num);
                    System.out.print(" ");
                }
                System.out.println();
            }
            System.out.println();
        }
    }

}




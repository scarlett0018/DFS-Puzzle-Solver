import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.stream.Collectors;

public class Tree{
    int depth;
    int[] direction = new int[2];
    int[][] panel;
    Tree father;
    ArrayList<Tree> children = new ArrayList<>();

    Tree(){
        this.father = null;
        this.depth = 0;
    }

    Tree(int[][] panel){
        this.father = null;
        this.panel = panel;
        this.depth = 0;
    }

    Tree(Tree father, int[][] panel){
        this.father = father;
        if (father==null)this.depth=0;
        else this.depth = father.depth + 1;
        this.panel = panel;
    }

    Tree(Tree father, int[][] panel, int[] direction){
        this.father = father;
        if (father==null)this.depth=0;
        else this.depth = father.depth + 1;
        this.panel = panel;
        this.direction = direction;
    }

    public ArrayList<Tree> generateChildren(int[] shape){
        ArrayList<Tree> children = new ArrayList<>();
        int n = this.panel.length;
        int m = this.panel[0].length;
        for(int i = 0; i < n; i++){
            for(int j = 0; j < m; j++){
                if(panel[i][j] == 0){
                    children.addAll(this.childrenAt(i, j, shape));
                }
            }
        }
        for(Tree child : children){
            if(child != null){
                child.father = this;
                this.children.add(child);
            }
        }

        return children;
    }

    public Tree dfsExpand(int[] shape, int[][] winPanel){
        Tree end = new Tree(null, null);
        ArrayDeque<Tree> queue = new ArrayDeque<>();
        HashSet<Tree> hashSet = new HashSet<>();
        queue.add(this);
        hashSet.add(this);

        int cnt = 0;
        while(!queue.isEmpty()){
            Tree node = queue.pollLast();
            cnt ++;

            if(cnt % 10000 == 0){
                System.out.println(cnt);
            }

            if(Arrays.deepEquals(node.panel, winPanel)){
                return node;
            }


            node.generateChildren(shape);
            for(Tree child : node.children){
                if(child != null && !hashSet.contains(child)){
                    queue.add(child);
                    hashSet.add(child);
                }
            }
        }

        return end;
    }


    public ArrayList<Tree> searchPath(int[] shape, int[][] winPanel){
        ArrayList<Tree> result = new ArrayList<>();
        Tree endNode = this.dfsExpand(shape, winPanel);

        while(endNode != null){
            result.add(endNode);
            endNode = endNode.father;
        }

        return result;
    }

    @Override
    public boolean equals(Object that){
        if(!(that instanceof Tree)) return false;
        return Arrays.deepEquals(this.panel, ((Tree) that).panel);
    }

    @Override
    public int hashCode(){
        return Arrays.deepHashCode(this.panel);
    }


    public List<Tree> childrenAt(int x, int y, int[] shape){
        HashSet<Tree> set = new HashSet<>();
        int direction = this.direction[1];

        if(direction != 2){
            Tree upState = tryMoveUp(x, y, this.panel, shape);
            if(upState != null) set.add(upState);
        }

        if(direction != 1){
            Tree downState = tryMoveDown(x, y, this.panel, shape);
            if(downState != null) set.add(downState);
        }

        if(direction != 4){
            Tree leftState = tryMoveLeft(x, y, this.panel, shape);
            if(leftState != null) set.add(leftState);
        }

        if(direction != 3){
            Tree rightState = tryMoveRight(x, y, this.panel, shape);
            if(rightState != null) set.add(rightState);
        }

        return new ArrayList<>(set);
    }

    public Tree tryMoveUp(int x, int y, int[][] panel, int[] shape){
        int[][] _panel = this.clonePanel();
        if(isOnArea(x + 1, y)){
            int id = panel[x + 1][y];
            if(id == 0) return null;

            int[] center = getCenter(x + 1, y, shape[id]);
            int center_id = panel[center[0]][center[1]];

            // TODO: 对于下面的实体方块的尝试移动
            int[][] data = centerMoveUp(center[0], center[1], _panel, panel, shape[center_id]);
            if(data == null) return null;
            int[] direction = new int[2];
            direction[0] = center_id;
            direction[1] = 1;

            return new Tree(this, data, direction);
        }else{
            return null;
        }
    }

    public int[][] centerMoveUp(int x, int y, int[][] _panel, int[][] panel, int shape){
        switch (shape){
            case 0:
                if(isOnArea(x - 1, y)){
                    if(_panel[x - 1][y] == 0){
                        _panel[x - 1][y] = panel[x][y];

                        _panel[x][y] = 0;
                        return _panel;
                    }
                }else{
                    return null;
                }
                break;
            case 1:
                if(isOnArea(x - 1, y)){
                    if(_panel[x - 1][y] == 0 && _panel[x - 1][y + 1] == 0){
                        _panel[x - 1][y] = panel[x][y];
                        _panel[x - 1][y + 1] = panel[x][y + 1];

                        _panel[x][y] = 0;
                        _panel[x][y + 1] = 0;
                        return _panel;
                    }else{
                        return null;
                    }
                }else{
                    return null;
                }
            case 3:
                if(isOnArea(x - 1, y)){
                    if(_panel[x - 1][y] == 0){
                        _panel[x - 1][y] = panel[x][y];
                        _panel[x][y] = panel[x + 1][y];

                        _panel[x + 1][y] = 0;
                        return _panel;
                    }else{
                        return null;
                    }
                }else{
                    return null;
                }
            case 5:
                if(isOnArea(x - 1, y)){
                    if(_panel[x - 1][y] == 0 && _panel[x - 1][y + 1] == 0){
                        _panel[x - 1][y] = panel[x][y];
                        _panel[x - 1][y + 1] = panel[x][y + 1];
                        _panel[x][y] = panel[x + 1][y];
                        _panel[x][y + 1] = panel[x + 1][y + 1];

                        _panel[x + 1][y] = 0;
                        _panel[x + 1][y + 1] = 0;

                        return _panel;
                    }else{
                        return null;
                    }
                }else{
                    return null;
                }
        }
        return null;
    }

    public Tree tryMoveDown(int x, int y, int[][] panel, int[] shape){
        int[][] _panel = this.clonePanel();
        if(isOnArea(x - 1, y)){
            int id = panel[x - 1][y];
            if(id == 0) return null;

            int[] center = getCenter(x - 1, y, shape[id]);
            int center_id = panel[center[0]][center[1]];

            int[][] data = centerMoveDown(center[0], center[1], _panel, panel, shape[center_id]);
            if(data == null) return null;
            int[] direction = new int[2];
            direction[0] = center_id;
            direction[1] = 2;

            return new Tree(this, data, direction);
        }else{
            return null;
        }
    }

    public int[][] centerMoveDown(int x, int y, int[][] _panel, int[][] panel, int shape){
        switch (shape){
            case 0:
                if(isOnArea(x + 1, y)){
                    if(_panel[x + 1][y] == 0){
                        _panel[x + 1][y] = panel[x][y];

                        _panel[x][y] = 0;
                        return _panel;
                    }
                }else{
                    return null;
                }
                break;
            case 1:
                if(isOnArea(x + 1, y)){
                    if(_panel[x + 1][y] == 0 && _panel[x + 1][y + 1] == 0){
                        _panel[x + 1][y] = panel[x][y];
                        _panel[x + 1][y + 1] = panel[x][y + 1];

                        _panel[x][y] = 0;
                        _panel[x][y + 1] = 0;
                        return _panel;
                    }else{
                        return null;
                    }
                }else{
                    return null;
                }
            case 3:
                if(isOnArea(x + 2, y)){
                    if(_panel[x + 2][y] == 0){
                        _panel[x + 1][y] = panel[x][y];
                        _panel[x + 2][y] = panel[x + 1][y];

                        _panel[x][y] = 0;
                        return _panel;
                    }else{
                        return null;
                    }
                }else{
                    return null;
                }
            case 5:
                if(isOnArea(x + 2, y)){
                    if(_panel[x + 2][y] == 0 && _panel[x + 2][y + 1] == 0){
                        _panel[x + 1][y] = panel[x][y];
                        _panel[x + 1][y + 1] = panel[x][y + 1];
                        _panel[x + 2][y] = panel[x + 1][y];
                        _panel[x + 2][y + 1] = panel[x + 1][y + 1];

                        _panel[x][y] = 0;
                        _panel[x][y + 1] = 0;

                        return _panel;
                    }else{
                        return null;
                    }
                }else{
                    return null;
                }
        }
        return null;
    }

    public Tree tryMoveLeft(int x, int y, int[][] panel, int[] shape){
        int[][] _panel = this.clonePanel();
        if(isOnArea(x, y + 1)){
            int id = panel[x][y + 1];
            if(id == 0) return null;
            int[] center = getCenter(x, y + 1, shape[id]);
            int center_id = panel[center[0]][center[1]];

            int[][] data = centerMoveLeft(center[0], center[1], _panel, panel, shape[center_id]);
            if(data == null) return null;
            int[] direction = new int[2];
            direction[0] = center_id;
            direction[1] = 3;

            return new Tree(this, data, direction);
        }else{
            return null;
        }
    }

    public int[][] centerMoveLeft(int x, int y, int[][] _panel, int[][] panel, int shape){
        switch (shape){
            case 0:
                if(isOnArea(x, y - 1)){
                    if(_panel[x][y - 1] == 0){
                        _panel[x][y - 1] = panel[x][y];

                        _panel[x][y] = 0;
                        return _panel;
                    }
                }else{
                    return null;
                }
                break;
            case 1:
                if(isOnArea(x, y - 1)){
                    if(_panel[x][y - 1] == 0){
                        _panel[x][y - 1] = panel[x][y];
                        _panel[x][y] = panel[x][y + 1];

                        _panel[x][y + 1] = 0;
                        return _panel;
                    }else{
                        return null;
                    }
                }else{
                    return null;
                }
            case 3:
                if(isOnArea(x, y - 1)){
                    if(_panel[x][y - 1] == 0 && _panel[x + 1][y - 1] == 0){
                        _panel[x][y - 1] = panel[x][y];
                        _panel[x + 1][y - 1] = panel[x + 1][y];

                        _panel[x][y] = 0;
                        _panel[x + 1][y] = 0;
                        return _panel;
                    }else{
                        return null;
                    }
                }else{
                    return null;
                }
            case 5:
                if(isOnArea(x, y - 1)){
                    if(_panel[x][y - 1] == 0){
                        _panel[x][y - 1] = panel[x][y];
                        _panel[x + 1][y - 1] = panel[x + 1][y];
                        _panel[x][y] = panel[x][y + 1];
                        _panel[x + 1][y] = panel[x + 1][y + 1];

                        _panel[x][y + 1] = 0;
                        _panel[x + 1][y + 1] = 0;
                        return _panel;
                    }else{
                        return null;
                    }
                }else{
                    return null;
                }
        }
        return null;
    }

    public Tree tryMoveRight(int x, int y, int[][] panel, int[] shape){
        int[][] _panel = this.clonePanel();
        if(isOnArea(x, y - 1)){
            int id = panel[x][y - 1];
            if(id == 0) return null;
            int[] center = getCenter(x, y - 1, shape[id]);
            int center_id = panel[center[0]][center[1]];

            int[][] data = centerMoveRight(center[0], center[1], _panel, panel, shape[center_id]);
            if(data == null) return null;
            int[] direction = new int[2];
            direction[0] = center_id;
            direction[1] = 4;

            return new Tree(this, data, direction);
        }else{
            return null;
        }
    }

    public int[][] centerMoveRight(int x, int y, int[][] _panel, int[][] panel, int shape){
        switch (shape){
            case 0:
                if(isOnArea(x, y + 1)){
                    if(_panel[x][y + 1] == 0){
                        _panel[x][y + 1] = panel[x][y];

                        _panel[x][y] = 0;
                        return _panel;
                    }
                }else{
                    return null;
                }
                break;
            case 1:
                if(isOnArea(x, y + 2)){
                    if(_panel[x][y + 2] == 0){
                        _panel[x][y + 1] = panel[x][y];
                        _panel[x][y + 2] = panel[x][y + 1];

                        _panel[x][y] = 0;
                        return _panel;
                    }else{
                        return null;
                    }
                }else{
                    return null;
                }
            case 3:
                if(isOnArea(x, y + 1)){
                    if(_panel[x][y + 1] == 0 && _panel[x + 1][y + 1] == 0){
                        _panel[x][y + 1] = panel[x][y];
                        _panel[x + 1][y + 1] = panel[x + 1][y];

                        _panel[x][y] = 0;
                        _panel[x + 1][y] = 0;
                        return _panel;
                    }else{
                        return null;
                    }
                }else{
                    return null;
                }
            case 5:
                if(isOnArea(x, y + 2)){
                    if(_panel[x][y + 2] == 0 && _panel[x + 1][y + 2] == 0){
                        _panel[x][y + 1] = panel[x][y];
                        _panel[x + 1][y + 1] = panel[x + 1][y];
                        _panel[x][y + 2] = panel[x][y + 1];
                        _panel[x + 1][y + 2] = panel[x + 1][y + 1];

                        _panel[x][y] = 0;
                        _panel[x + 1][y] = 0;
                        _panel[x][y + 1] = 0;
                        _panel[x + 1][y + 1] = 0;
                        return _panel;
                    }else{
                        return null;
                    }
                }else{
                    return null;
                }
        }
        return null;
    }

    public int[] getCenter(int x, int y, int shape){
        int[] ans = new int[2];
        switch (shape){
            case 0:
            case 1:
            case 3:
            case 5:
                ans[0] = x;
                ans[1] = y;
                return ans;
            case 2:
            case 6:
                ans[0] = x;
                ans[1] = y - 1;
                return ans;
            case 4:
            case 7:
                ans[0] = x - 1;
                ans[1] = y;
                return ans;
            case 8:
                ans[0] = x - 1;
                ans[1] = y - 1;
                return ans;
        }
        return null;
    }


    public boolean isOnArea(int x, int y){
        return (x >= 0 && x < this.panel.length) && (y >= 0 && y < this.panel[0].length);
    }




    public int[][] clonePanel(){
        int n = this.panel.length;
        int m = this.panel[0].length;
        int[][] panel = new int[n][m];
        for(int i = 0 ; i < n; i++){
            for(int j = 0; j < m; j++){
                panel[i][j] = this.panel[i][j];
            }
        }
        return panel;
    }

    public int compareTo(Tree a, Tree b){
        if (a.depth!=b.depth)return a.depth-b.depth;//positive--a is deeper
        else {
            int disA=0;
            int disB=0;
            disA=Game.manhattonDis(a.panel);
            disB=Game.manhattonDis(b.panel);
            return disB-disA;//positive--a is better
        }
    }
}


import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicInteger;

public class GameFrame extends JFrame {
    public int m;
    public int n;
    public int[][] chess;
    private int[][] complexBlocks;
    private final int WIDTH;
    private final int HEIGHT;
    private ArrayList<Integer> Squares_num = new ArrayList<>();
    private ArrayList<Tree> winPath;

    private Game game;

    private ArrayList<Tree> solution;


    public GameFrame(int m, int n, int[][] idOfBlocks, int[][] complexBlocks) throws HeadlessException {
        this.m = m;
        this.n = n;

        this.HEIGHT = m*120;
        this.WIDTH = n*200;

        this.game = new Game(idOfBlocks, complexBlocks);
//        this.solution = game.getSolution();
        this.winPath = game.root.searchPath(game.shape, game.winPanel);
        Collections.reverse(this.winPath);


        Game.printStep(this.winPath);
        for (Tree t:
             winPath) {
            System.out.print(t.direction[0]+" ");
            if (t.direction[1]==1) System.out.println("U");
            if (t.direction[1]==2) System.out.println("D");
            if (t.direction[1]==3) System.out.println("L");
            if (t.direction[1]==4) System.out.println("R");
        }



        this.chess = game.root.panel;
        this.complexBlocks = complexBlocks;

        display();

        setTitle("2022 Dsaa Project");
        setSize(WIDTH,HEIGHT);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
    }

    //初始化布局
    private void display(){
        GridBagLayout gridBag = new GridBagLayout();    // 布局管理器
        GridBagConstraints c = null;         //约束
        JPanel panel = new JPanel(gridBag);
        JPanel P2 = new JPanel(new GridLayout(m,n,3,3));

        addGoButton(panel, gridBag, c, P2);
        putChess(gridBag, c, P2,game.root.panel);

        panel.add(P2);
        this.setContentPane(panel);
    }

    private void addGoButton(JPanel panel, GridBagLayout gridBag,  GridBagConstraints c,  JPanel P2){
        JButton button = new JButton("Go");

        button.setLocation(0,0);
        button.setSize(WIDTH, 40);
        button.setFont(new Font("Rockwell",Font.BOLD,20));
        button.setBackground(new Color(237, 130, 60));
        button.setForeground(Color.white);
        button.setBorderPainted(false);

        c = new GridBagConstraints();
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 50;
        c.weighty = 0.3;
        gridBag.addLayoutComponent(button,c);
        panel.add(button);

//        ArrayList<Game.State> pathList=new ArrayList<>();
        final ArrayList<Tree> winPath = this.winPath;

        AtomicInteger cnt = new AtomicInteger();
        button.addActionListener((e)->{
            int[][] nextChess = winPath.get(cnt.get()).panel;
            cnt.getAndIncrement();
            setChess(nextChess);
            P2.removeAll();

            GridBagConstraints c1 = null;

            putChess(gridBag, c1, P2,nextChess);

            revalidate();
        });
    }

    //更新当前的棋子按钮
    private void putChess(GridBagLayout gridBag,GridBagConstraints c,JPanel P2,int[][] panel){
        P2.setLocation(0,40);
        for(int[] i : panel){
            for (int j:i){
                String m = String.valueOf(j);
                JButton temp = new JButton(m);
                temp.setFont(new Font("Rockwell",Font.BOLD,20));
                temp.setBackground(new Color(235, 205, 171));
                if (j==0){
                    temp.setEnabled(false);
                    temp.setBackground(new Color(235, 224, 211));
                    temp.setBorderPainted(false);
                }else if (Squares_num.contains(j)){
                    temp.setBorder(BorderFactory.createLineBorder(new Color(230, 86, 44),2));
                }else{
                    temp.setBorderPainted(false);
                }
                temp.setForeground(new Color(240, 115, 60));
                P2.add(temp);

                c = new GridBagConstraints();
                c.gridwidth = GridBagConstraints.REMAINDER;
                c.fill = GridBagConstraints.BOTH;
                c.weighty = 1;
                gridBag.addLayoutComponent(P2,c);
            }
        }
    }

    //更新棋子的二维数组
    public void setChess(int[][] out_chess) {
        this.chess =out_chess;
    }

    //得到绑定在一起的数字
    private void setSquares_num(int[][] complex) {
        for (int x = 0; x < complex.length;x++){
            int s1 = complex[x][0];
            int a = complex[x][1];
            int b = complex[x][2];
            complexBlocks[x][0] = s1;
            complexBlocks[x][1] = a;
            complexBlocks[x][2] = b;
            for (int i=0;i<a;i++){
                for (int j=0;j<b;j++){
                    Squares_num.add(s1+i*n+j);
                }
            }
        }
    }

}

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class GameFrame extends JFrame {
    public int m;
    public int n;
    public int[][] chess;
    public int[][] current_chess;
    public String[] squares;
    private int[][] squaresZC ;
    private final int WIDTH;
    private final int HEIGHT;
    private ArrayList<Integer> Squares_num = new ArrayList<>();

    private Game game;

    private ArrayList<Game.State> solution;

    public GameFrame(int m, int n, int[][] chess, String[] squares) throws HeadlessException {
        this.m = m;
        this.n = n;
        this.chess = chess;
        this.squares = squares;
        this.squaresZC = new int[squares.length][3];

        this.HEIGHT = m*120;
        this.WIDTH = n*200;

        setSquares_num();
        setCurrent_chess(chess);

        setTitle("2022 Dsaa Project");
        setSize(WIDTH,HEIGHT);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
        this.game = new Game(chess, squaresZC);
        this.solution = game.getSolution();
        if ( solution!=null) display();
        else return;
    }

    //初始化布局
    private void display(){
        GridBagLayout gridBag = new GridBagLayout();    // 布局管理器
        GridBagConstraints c = null;         //约束
        JPanel panel = new JPanel(gridBag);
        JPanel P2 = new JPanel(new GridLayout(m,n,3,3));

        addGoButton(panel,gridBag,c,P2);
        putChess(gridBag,c,P2);

        panel.add(P2);
        this.setContentPane(panel);
    }

    private void addGoButton(JPanel panel,GridBagLayout gridBag,GridBagConstraints c,  JPanel P2){
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

        AtomicInteger cnt = new AtomicInteger();
        button.addActionListener((e)->{
            int[][] temp;
            if (cnt.get() < solution.size()){
                temp = solution.get(cnt.get()).getPanel();
                cnt.getAndIncrement();
                setCurrent_chess(temp);
                P2.removeAll();

                GridBagConstraints c1 = null;
                putChess(gridBag,c1,P2);

                revalidate();
            }
            else {JOptionPane.showMessageDialog(null, "Over");}
        });
    }

    //更新当前的棋子按钮
    private void putChess(GridBagLayout gridBag,GridBagConstraints c,JPanel P2){
        P2.setLocation(0,40);
        for(int[] i : current_chess){
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
    public void setCurrent_chess(int[][] out_chess) {
        this.current_chess =out_chess;
    }

    //得到绑定在一起的数字
    private void setSquares_num() {
        for (int x=0;x< squares.length;x++){
            int s1 = Integer.parseInt(squares[x].split(" ")[0]);
            String s2 = squares[x].split(" ")[1];
            int a = Integer.parseInt(s2.split("\\*")[0]);
            int b = Integer.parseInt(s2.split("\\*")[1]);
            squaresZC[x][0] = s1;
            squaresZC[x][1] = a;
            squaresZC[x][2] = b;
            for (int i=0;i<a;i++){
                for (int j=0;j<b;j++){
                    Squares_num.add(s1+i*n+j);
                }
            }
        }
    }

}

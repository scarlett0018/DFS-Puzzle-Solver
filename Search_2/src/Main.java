//import GameFrame;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        //导入文件
        File input = new File("data/2.in");
        if (!input.exists()) {
            System.out.println("File isn't exist");
            System.exit(0);}

        //读取数据
        Scanner in = new Scanner(input);
        int m = in.nextInt();
        int n = in.nextInt();

        int[][] idOfBlocks = new int[m][n];
        for (int i =0;i<m;i++){
            for (int j=0;j<n;j++){
                idOfBlocks[i][j] = in.nextInt();
            }
        }

        int squares_amount= in.nextInt();
        int[][] complexBlocks = new int[squares_amount][3];

        if (squares_amount !=0) {
            for (int i = 0; i < squares_amount; i++) {
                complexBlocks[i][0] = in.nextInt();
                String[] str = in.next().split("\\*");
                complexBlocks[i][1] = Integer.parseInt(str[0]);
                complexBlocks[i][2] = Integer.parseInt(str[1]);
            }
        }


        SwingUtilities.invokeLater(() -> {
            GameFrame mainFrame = new GameFrame(m, n, idOfBlocks, complexBlocks);
            mainFrame.setVisible(true);
        });
    }
}
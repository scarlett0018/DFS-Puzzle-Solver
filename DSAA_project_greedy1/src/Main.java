//import GameFrame;

import edu.princeton.cs.algs4.StdOut;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        //导入文件
        File input = new File("data/f2.in");
        if (!input.exists()) {
            System.out.println("File isn't exist");
            System.exit(0);}

        //读取数据
        Scanner in = new Scanner(input);
        int m = in.nextInt();
        int n = in.nextInt();

        int[][] chess = new int[m][n];
        for (int i =0;i<m;i++){
            for (int j=0;j<n;j++){
            chess[i][j] = in.nextInt();
        }}

        int squares_amount= in.nextInt();
        String[] squares=new String[squares_amount];
        if (squares_amount!=0){
            String useless=in.nextLine();

            for (int j =0;j<squares_amount;j++){
                squares[j]= in.nextLine();
            }
        }

        SwingUtilities.invokeLater(() -> {
            GameFrame mainFrame = new GameFrame(m, n,chess,squares);
            mainFrame.setVisible(true);
//            test test = new test();
//            test.setVisible(true);
        });
    }
}

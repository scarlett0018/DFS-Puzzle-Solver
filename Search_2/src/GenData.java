import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

public class GenData {
    public static void main(String[] args) {
        int[] arr = new int[10000];

        for (int i = 0, num = 1; i < arr.length; ++i, num++)
            arr[i] = num;



        new File("./src/resources/data/").mkdirs();

        for (int i = 1; i <= 8; ++i) {
            try (PrintWriter fout = new PrintWriter("./data/" + "Gen" + i + ".in")) {

                Random r = new Random();
                int m = r.nextInt(4) + 2;
                int n = r.nextInt(4) + 2;
                fout.print(m);
                fout.print(' ');
                fout.print(n);
                fout.println();



                int zero = r.nextInt(n - 1) + 1;
                int[][] chess = new int[m][n];

                //给正常棋子赋值
                for (int x = 1; x <= m; x++) {
                    for (int y = 1; y <= n; y++) {
                        if ((x == m) && (y == n - zero + 1)) break;
                        chess[x - 1][y - 1] = (x - 1) * n + y;
                    }
                }

                //将0加入棋盘中
                for (int y = n - zero + 1; y <= n; y++) {
                    chess[m - 1][y - 1] = 0;
                }

                //对棋子进行交换，打乱
                int zero_x = m - 1;
                int zero_y = n - 1;
                for (int l = 1; l <= 1000; l++) {
                    int rad = r.nextInt(4) + 1;

                    int height = chess.length;
                    int weight = chess[0].length;

                    if ((rad == 1) && (zero_x - 1 >= 0)) {
                        swap(chess, zero_x, zero_y, -1, 0);
                        zero_x = zero_x - 1;
                    } else if ((rad == 2) && (zero_x + 1 <= height - 1)) {
                        swap(chess, zero_x, zero_y, 1, 0);
                        zero_x = zero_x + 1;
                    } else if ((rad == 3) && (zero_y - 1 >= 0)) {
                        swap(chess, zero_x, zero_y, 0, -1);
                        zero_y = zero_y - 1;
                    } else if ((rad == 4) && (zero_y + 1 <= weight - 1)) {
                        swap(chess, zero_x, zero_y, 0, 1);
                        zero_y = zero_y + 1;
                    }


                }
                for (int x = 0; x < m; x++) {
                    for (int y = 0; y < n; y++) {
                        fout.print(chess[x][y]);
                        fout.print(' ');
                        if (y == n - 1) fout.println();
                    }
                }
                fout.println(0);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


    public static void swap(int[][] chess, int zero_x, int zero_y, int x, int y) {
        int temp = chess[zero_x + x][zero_y + y];
        chess[zero_x + x][zero_y + y] = chess[zero_x][zero_y];
        chess[zero_x][zero_y] = temp;
    }
}
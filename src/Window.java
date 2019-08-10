import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;


        //前提:mysql启动,存在mine数据库,存在mine(time int,mine int ,rank int)数据表
public class Window extends JFrame {
    private static final int PX = 50;        //42以下,好像就不能正常的显示数字了
    private static int row;
    private static int col;
    private static int heigh;
    private static int width;
    private static int difficuty;
    private static boolean[][] clicked;
    private static boolean[][] rightClicked;
    private static int[][] arr;
    private static long time;
    private static int mistory;
    private static int mine;
    private static int pointed;
    private static ArrayList<JButton> c = new ArrayList<>();
    public static void main(String[] args) {
        new Window();
    }
    private Window(){
        init();
        this.setSize(width,heigh);
        this.setTitle("MineSweeping");
        this.setLayout(new BorderLayout());
        this.setResizable(false);
        this.add(MenuArea(),BorderLayout.NORTH);
        this.add(gameArea(row,col));
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    private JPanel MenuArea() {
        JPanel menu = new JPanel();
        menu.setLayout(null);
        return menu;
    }

    private JPanel gameArea(int row,int col) {
        //设定随机个arr的值为9;
        for(int i = 0; i < row; i++)
            for (int j = 0; j < col; j++)
                if (Math.random()*100 < difficuty){
                    arr[i][j] = 9;
                    mine++;
                }
        this.setTitle("All mines: " + mine);

        //在这里确定每个格子的数值
        //给每个格子赋值.这个值怎么来呢.得先确定这个格子相邻的所有格子.
        /*
         * 假设一个5*5的布局
         *  1  2  3  4  5
         *  6  7  8  9  10
         *  11 12 13 14 15
         *  16 17 18 19 20
         *  21 22 23 24 25
         *  一个格子的+1和-1如果有的话,肯定是相邻的
         *  一个格子如果有+row和-row的话,肯定也是相邻的
         *  一个格子如果有+row+1和+row-1和-row+1和-row-1的话,也属于,就这么多.
         *  一个格子用8个if来确定所有的9
         * */
        //这里行列变量还需要优化,如果行列不一致可能报错
        int sum_mine;
        for(int i = 0; i < row; i++)
            for (int j = 0; j < col; j++){
                sum_mine = 0;
                if (j - 1 >= 0) {
                    if (arr[i][j - 1] == 9)     //左
                        sum_mine++;
                    if (i - 1 >= 0)           //左上
                        if (arr[i - 1][j - 1] == 9)
                            sum_mine++;
                    if (i + 1 < row)              //左下
                        if (arr[i + 1][j - 1] == 9)
                            sum_mine++;
                }
                if (j + 1 < col) {
                    if (arr[i][j + 1] == 9) //右
                        sum_mine++;
                    if (i - 1 >= 0)           //右上
                        if (arr[i - 1][j + 1] == 9)
                            sum_mine++;
                    if (i + 1 < row)              //右下
                        if (arr[i + 1][j + 1] == 9)
                            sum_mine++;
                }
                    if (i - 1 >= 0)           //上
                        if (arr[i - 1][j] == 9)
                            sum_mine++;
                    if (i + 1 < row)              //下
                        if (arr[i + 1][j] == 9)
                            sum_mine++;
                if (arr[i][j] != 9)
                    arr[i][j] = sum_mine;
            }
        JPanel jp = new JPanel();
        jp.setLayout(new GridLayout(row,col,0,0));
        //这个for用来创建格子.并且随机埋炸弹
        for(int i = 0; i < row; i++)
            for (int j = 0; j < col; j++){
//                JButton btn = new JButton(Integer.toString(arr[i][j]));
                JButton btn = new JButton();
                btn.setBackground(Color.white);
                c.add(btn);
                btn.addActionListener(e -> {
                    int x,y;
                    x = c.indexOf(btn)/col;
                    y = c.indexOf(btn)%col;
                    //arr = 9代表炸弹,炸弹就游戏结束,如果不是炸弹的话.则显示这个格子的数字.
                    //那么首先得给每个格子确定数字
                    if (arr[x][y] == 9){
                        gameOver();
                    }
                    else if (arr[x][y] == 0)
                        showAroundNumber(x,y);
                    else
                        showNumber(x,y);
                    //通关条件
                    if (mistory - mine ==0) {
                        try {
                            gamePass();
                        } catch (SQLException e1) {
                            e1.printStackTrace();
                        }
                    }
                });
                btn.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        int x,y;
                        x = c.indexOf(btn)/col;
                        y = c.indexOf(btn)%col;
                        if (e.getButton() == 3){
                            JButton jbtemp = (JButton)e.getSource();
                            if (jbtemp.isEnabled() || rightClicked[x][y])
                                if (jbtemp.getText().equals("")){
                                    jbtemp.setText("!");
                                    jbtemp.setEnabled(false);
                                    jbtemp.setBackground(Color.yellow);
                                    rightClicked[x][y] = true;
                                    pointed++;
                                    Window.this.setTitle("mine:" + mine + "   pointed:" + pointed);
                                }
                                else if (jbtemp.getText().equals("!")) {
                                    jbtemp.setEnabled(true);
                                    jbtemp.setText("?");
                                    rightClicked[x][y] = false;
                                    jbtemp.setBackground(Color.white);
                                    pointed--;
                                    Window.this.setTitle("mine:" + mine + "   pointed:" + pointed);
                                }
                                else
                                    jbtemp.setText("");
                        }
                    }

                });
                jp.add(btn);
            }
        return jp;
    }

    private void showNumber(int row,int col) {
        JButton btn = c.get(row*Window.col + col);
        btn.setText(Integer.toString(arr[row][col]));
        if (arr[row][col] == 9)
            btn.setText("X");
        clicked[row][col] = true;
        btn.setEnabled(false);
        btn.setBackground(Color.gray);
        mistory--;
    }

    private void showAroundNumber(int row, int col) {
        //当前这个,,这一个被点,当递归调用的时候,如果0旁边还有0,则模拟旁边的0被点的情况,所以旁边如果不是0,则那些公共
        //的点会被多次mistory,如果吧mistory放在外面,则只会计数0的个数.
        clicked[row][col] = true;
        JButton btn = c.get(row*Window.col + col);
//        btn.setText(Integer.toString(arr[row][col]));
        btn.setText("");
        btn.setEnabled(false);
        btn.setBackground(Color.gray);
        mistory--;          //0都会被mistory--;但是非0则不会.
        int[] i = new int[]{row,row-1,row+1};
        int[] j = new int[]{col,col-1,col+1};
        for (int r = 0;r< i.length;r++)
            for (int s=0;s < j.length;s++){
                if (i[r] >=0 && j[s] >= 0 &&i[r] < Window.row && j[s] < Window.col){
                    JButton btn1 = c.get(i[r]*Window.col + j[s]);
                    if (arr[i[r]][j[s]] != 0 && !clicked[i[r]][j[s]]){
                        btn1.setText(Integer.toString(arr[i[r]][j[s]]));
                        btn1.setEnabled(false);
                        btn1.setBackground(Color.gray);
                        clicked[i[r]][j[s]] = true;
                        mistory--;
                    }
                    //下面的clicked判断得是true,因为,第一次点0的时候,在上面就已经
                    //吧clicked设为true了,所以就不会执行这里面的东西了.
                    if (arr[i[r]][j[s]] == 0 && !clicked[i[r]][j[s]]){
                        showAroundNumber(i[r],j[s]);
                    }
                }

            }
    }

    private void showAllMines(){
        for (int i =0;i < Window.row*Window.col;i++){
            JButton btn = c.get(i);
            btn.setEnabled(false);
            if (arr[i/col][i%col] == 9)
                showNumber(i/col,i%col);
        }
    }

    private void gamePass() throws SQLException {
        showAllMines();
        long usedTime = new Date().getTime() - time;        //所用时间
        //排名算法.:    雷的数量和 所用时间的一波计算得出来:
        Connection ct = DBUtil.getConnection();
        Statement s = ct.createStatement();
        String sql_init  ="use " + DBUtil.DATABASE;
        String sql = "insert into mine values (" + usedTime + "," + mine + "," + usedTime/mine + ")";
        //至此,已经完成了将时间和雷数导入数据库的操作了.接下来要完成显示排名的操作
        //rank越大,说明越f菜,
        s.execute(sql_init);
        s.execute(sql);
        ResultSet rs;
        String sql_query = "select * from mine order by `rank` ;";
        rs = s.executeQuery(sql_query);
        int i  = 0;
        while(rs.next()) {
            i++;
            if (rs.getInt("rank") > usedTime/mine)
                break;
        }

        JFrame go = new GameOver(((int)usedTime/1000)/60 + "分" + ((int)usedTime/1000)%60 + "秒",i -1,this,"You Win!");
        go.setAlwaysOnTop(true);
        this.setEnabled(false);

    }

    private void gameOver() {
        showAllMines();
        long usedTime = new Date().getTime() - time;        //所用时间
        JFrame go = new GameOver(((int)usedTime/1000)/60 + "分" + ((int)usedTime/1000)%60 + "秒",-1,this,"Game Over!");
        go.setAlwaysOnTop(true);
        this.setEnabled(false);

    }

    private void init(){
        difficuty = 7;                          //困难系数10-50(推荐)
        pointed = 0;                            //标记点数
        mine = 0;
        row = 11;                                //行数
        col = 13;                                //列数
        heigh = row * PX;
        width = col * PX;
        mistory = row*col;
        arr = new int[row][col];
        clicked = new boolean[row][col];
        rightClicked = new boolean[row][col];
        init_intarr(arr);
        init_booarr(clicked);
        init_booarr(rightClicked);
        time = new Date().getTime();
    }

    private void init_intarr(int[][] arr){
        for (int i = 0;i < row; i++)
            for(int j =0;j < col;j++)
                arr[i][j] = 0;
    }

    private void init_booarr(boolean[][] arr){
        for (int i = 0;i < row; i++)
            for(int j =0;j < col;j++)
                arr[i][j] = false;
    }

    public void res(){
        init();
        gameArea(row,col);
        for (int i =0;i < Window.row*Window.col;i++) {
            JButton btn = c.get(i);
            btn.setEnabled(true);
            btn.setText("");
            btn.setBackground(Color.white);
        }
    }
}

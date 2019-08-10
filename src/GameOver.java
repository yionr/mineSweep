
import java.awt.BorderLayout;
import java.awt.event.*;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.*;



public class GameOver extends JFrame{
	private static String time;
	private static int rank;
	private static String str;
	private JButton jbtAgain = new JButton("Again");
	private JButton jbtCanel = new JButton("Cancel");

	public GameOver( String time,int rank,JFrame window,String str){
		GameOver.str = str;
		GameOver.time =time;
		GameOver.rank = rank;
		this.setTitle("结算");
		this.setSize(250,150);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		
		this.setLayout(new BorderLayout());
		this.add(setGameoverPanel() ,BorderLayout.NORTH);
		this.add(setResultPanel() ,BorderLayout.CENTER);
		this.add(setContinuePanel() ,BorderLayout.SOUTH);
		
		jbtCanel.addActionListener(e -> System.exit(0));
		jbtAgain.addActionListener(e -> {
			try {
				GameOver.super.setVisible(false);
				Window.class.getMethod("res").invoke(window);
			} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e1) {
				e1.printStackTrace();
			}
			window.setEnabled(true);
});
		
	}
	
	private JPanel setGameoverPanel() {
		JPanel jp = new JPanel();
		jp.add(new JLabel(str));
		return jp;
		
	}

	private JPanel setResultPanel() {
		JPanel jp = new JPanel(new BorderLayout());
		jp.add(setScorePanel(),BorderLayout.NORTH);
		jp.add(setRankPanel(),BorderLayout.SOUTH);
		return jp;
	}

	private JPanel setScorePanel() {
		JPanel jp = new JPanel();
		jp.add(new JLabel("用时:"));
		jp.add(new JLabel(time+" "));
		return jp;
	}

	private JPanel setRankPanel() {
		JPanel jp = new JPanel();
		JLabel r = new JLabel("排名:");
		jp.add(r);
		jp.add(new JLabel(rank +" "));
        r.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Connection ct = DBUtil.getConnection();
				String sum = "";
				try {
					Statement s = ct.createStatement();
					String sql_init = "use " + DBUtil.DATABASE;
					String sql_query = "select * from mine order by `rank`";
					ResultSet rs;
					s.execute(sql_init);
					rs = s.executeQuery(sql_query);
					int i = 0;
					while(rs.next() && ++i < 31){
						sum += "第" + i + "名:  " + "time: " + (long)rs.getInt("time")/1000 + "s   mine: " + rs.getInt("mine") + "\n";
					}
				} catch (SQLException e1) {
					e1.printStackTrace();
				}

				JOptionPane.showMessageDialog(r,sum);
            }
        });
		return jp;
	}

	private JPanel setContinuePanel() {
		JPanel jp = new JPanel();
		jp.add(jbtAgain);
		jp.add(jbtCanel);
		return jp;
	}

}
package database;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.util.Map;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JScrollPane;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;

public class Score_update extends JFrame {

	private JPanel contentPane;
	private JTextField code;
	private JLabel name1;
	private JTable table;
	private Map<String, String> coures=null,score,scorebu;
	private String cc;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Sql_connetcton.login_s("admin","admin");
					Score_update frame = new Score_update();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	private String[] name=new String[]{"课程号","课程名","成绩","补考成绩"};
	private void refresh(){ 
		if(coures==null) coures=Sql_connetcton.getCoures(); //课表Map
		cc=code.getText();
		Vector detail=Sql_connetcton.getPersons(cc); //学号,姓名
		if(detail==null||detail.size()==0){
			JOptionPane.showMessageDialog(getParent(), "学号错误,没有此人", "错误", JOptionPane.ERROR_MESSAGE);
			return;
		}
		name1.setText((String) detail.get(1));
		score=Sql_connetcton.getScore(cc); //成绩Map
		scorebu=Sql_connetcton.getScoreBu(cc);
		Vector<Vector> ans=new Vector<Vector>();
		Vector<String> names=new Vector<String>();
		for(String s:coures.keySet()){
			Vector<String> temp=new Vector<String>();
			temp.add(s);
			temp.add(coures.get(s));
			if(score.containsKey(s)){
				temp.add(score.get(s));
				temp.add(scorebu.get(s));
			}
			else{
				temp.add("NULL");
				temp.add("NULL");
			}
			ans.add(temp);
		}
		for(String s:name)names.add(s);
		TableModel tableModle=new DefaultTableModel(ans, names){
			public boolean isCellEditable(int row, int column){
				if(column>=2)return true;
                return false;}//表格不允许被编辑
		};
		table.setModel(tableModle);
	}
	/**
	 * Create the frame.
	 */
	public Score_update() {
		setTitle("\u5B66\u751F\u6210\u7EE9\u5F55\u5165(\u6309\u5B66\u53F7)");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		
		JLabel label = new JLabel("\u5B66\u53F7:");
		label.setBounds(21, 10, 54, 15);
		contentPane.add(label);
		
		code = new JTextField();
		code.setBounds(51, 7, 66, 21);
		contentPane.add(code);
		code.setColumns(10);
		
		JLabel label_1 = new JLabel("\u59D3\u540D:");
		label_1.setBounds(127, 10, 54, 15);
		contentPane.add(label_1);
		
		name1 = new JLabel();
		name1.setBounds(160, 7, 66, 21);
		contentPane.add(name1);
		
		
		final JButton button = new JButton("\u67E5\u8BE2");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				refresh();
			}
		});
		button.setBounds(236, 6, 66, 23);
		contentPane.add(button);
		
		JButton button_1 = new JButton("\u4FEE\u6539");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int ans=JOptionPane.showConfirmDialog(getParent(), "确定修改？", "警告", JOptionPane.YES_NO_OPTION);
				if(ans==0){
					DefaultTableModel tableModel=(DefaultTableModel) table.getModel();
					Vector data=tableModel.getDataVector();
					int rows=tableModel.getRowCount();
					int suc=0,bad=0;
					for(int i=0;i<rows;i++){
						Vector s=(Vector) data.elementAt(i);
						if(score.containsKey(s.get(0))&&s.get(2).equals("-1")){
							s.remove(3);
							s.remove(1);
							s.remove(1);
							s.add(0,cc);
							if(Sql_connetcton.delScore(s))suc++;
							else bad++;
							
						}
						else if((!s.get(2).equals("NULL")&&
								(!score.containsKey(s.get(0))||!s.get(2).equals(score.get(s.get(0)))))
							||(!s.get(3).equals("NULL")&&
								(!scorebu.containsKey(s.get(0))||!s.get(3).equals(scorebu.get(s.get(0)))))
								){
							s.remove(1);
							s.add(0,cc);
							if(Sql_connetcton.update(s, "成绩"))suc++;
							else bad++;
						}

							
					}
					JOptionPane.showMessageDialog(getParent(), "修改完成,成功"+suc+"个,失败"+bad+"个！");
					refresh();
					
				}
			}
		});
		button_1.setBounds(312, 6, 93, 23);
		contentPane.add(button_1);
		
		table=new JTable();
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBounds(10, 50, 414, 211);
		contentPane.add(scrollPane);
		
		JLabel label_2 = new JLabel("\u6210\u7EE9\u4E3A-1\u4EE3\u8868\u5220\u9664\u6B64\u6761\u8BB0\u5F55!");
		label_2.setForeground(Color.RED);
		label_2.setBounds(266, 35, 197, 15);
		contentPane.add(label_2);
		code.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				button.doClick();
				 
			}
		});
	}

}

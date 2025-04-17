
import javax.swing.*;
import java.awt.*;


/*
 ** 关于对话框类，用于显示游戏相关的一些信息，如游戏版权、游戏资源等信息
 */
public class AboutDialog extends JDialog
{
	// 主面板，用于放置其他组件
	JPanel jMainPane = new JPanel();
	// 选项卡面板，用于组织不同的信息页面
	JTabbedPane jTabbedPane = new JTabbedPane();
	// 用于显示游戏版权信息的面板
	private JPanel jPanel1 = new JPanel();
	// 用于显示游戏资源等其他信息的面板
	private JPanel jPanel2 = new JPanel();
	// 用于显示游戏规则相关信息的文本区域，这里预先设置了一些关于游戏规则的文字信息
	private JTextArea jt1 = new JTextArea("将电脑多次发给你的牌按照相同花色由大到小排序，知道全部排完");
	// 用于显示游戏资源相关信息的文本区域，这里预先设置了一些关于游戏中纸牌图片资源的信息
	private JTextArea jt2 = new JTextArea("游戏中，纸牌图片来源于Windows XP的纸牌游戏图片版权归原作者所有，在本游戏中仅用于娱乐目的");

	/*
	 ** 构造函数，用于初始化关于对话框的各种属性并显示对话框
	 */
	public AboutDialog()
	{
		// 设置对话框的标题为“提示信息”
		setTitle("提示信息");
		setSize(300,200);
		setResizable(false);
		// 设置关闭对话框时的默认操作是释放对话框资源（关闭对话框）
		setDefaultCloseOperation (WindowConstants.DISPOSE_ON_CLOSE);
		// 获取对话框的内容面板
		Container c = this.getContentPane();

		jt1.setSize(260,200);
		jt2.setSize(260,200);

		jt1.setEditable(false);
		jt2.setEditable(false);

		jt1.setLineWrap(true);
		jt2.setLineWrap(true);
		// 设置jt1文本区域的字体为“宋体_GB2312”，加粗，字号为13
		jt1.setFont(new Font("宋体_GB2312", java.awt.Font.BOLD, 13));
		// 设置jt1文本区域的文字颜色为蓝色
		jt1.setForeground(Color.blue);

		jt2.setFont(new Font("宋体_GB2312", java.awt.Font.BOLD, 13));
		// 设置jt2文本区域的文字颜色为黑色
		jt2.setForeground(Color.black);
		// 将jt1文本区域添加到jPanel1面板中
		jPanel1.add(jt1);
		// 将jt2文本区域添加到jPanel2面板中
		jPanel2.add(jt2);

		jTabbedPane.setSize(300,200);
		// 在jTabbedPane选项卡面板中添加一个名为“游戏规则”的选项卡，内容为jPanel1面板
		jTabbedPane.addTab("游戏规则", null, jPanel1, null);
		// 在jTabbedPane选项卡面板中添加一个名为“说明”的选项卡，内容为jPanel2面板
		jTabbedPane.addTab("说明", null, jPanel2, null);
		// 将jTabbedPane选项卡面板添加到jMainPane主面板中
		jMainPane.add(jTabbedPane);
		// 将jMainPane主面板添加到对话框的内容面板中
		c.add(jMainPane);
		// 根据内部组件的布局自动调整对话框的大小（更合理地利用空间）
		pack();
		this.setVisible(true);
	}
} 

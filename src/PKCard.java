import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

//PKCard类继承自JLabel，同时实现了MouseListener和MouseMotionListener接口，用于表示纸牌对象
public class PKCard extends JLabel implements MouseListener,
		MouseMotionListener{

	// 纸牌的位置坐标点，point用于记录当前位置，initPoint用于记录初始位置
	Point point = null;
	Point initPoint = null;
	// 纸牌的值（牌面数字）和类型（表示花色）
	int value = 0;
	int type = 0;
	// 纸牌的名称，用于标识特定的纸牌
	String name = null;
	// 容器，用于容纳纸牌的面板之类的
	Container pane = null;
	// 对Spider类（游戏主类）的引用，用于在纸牌类中调用游戏主类的方法
	Spider main = null;
	// 标记纸牌是否可移动
	boolean canMove = false;
	// 标记纸牌是否正面朝上
	boolean isFront = false;
	// 指向当前纸牌的前一张纸牌
	PKCard previousCard = null;
	//成功移动牌前，牌堆的列序列。
	public int beforeColumn;
	//成功移动牌后，当前纸牌的列序列。
	public int movedColumn;
	// 鼠标点击事件的处理方法，这里为空实现，可能根据需求后续添加逻辑
	public void mouseClicked(MouseEvent arg0){
	}
	// 使纸牌闪烁的方法，通过创建并启动一个Flash线程来实现闪烁效果
	public void flashCard(PKCard card){
		// 创建并启动Flash线程
		new Flash(card).start();
		// 如果当前纸牌有下一张纸牌，则递归调用使下一张纸牌也闪烁
		if(main.getNextCard(card) != null){
			card.flashCard(main.getNextCard(card));
		}
	}

	// Flash类是一个内部类，继承自Thread，用于实现纸牌闪烁效果
	class Flash extends Thread{
		private PKCard card = null;
		// 构造函数，接收要闪烁的纸牌对象
		public Flash(PKCard card){
			this.card = card;
		}

		/*
		 ** 重写线程的run()方法
		 ** 为纸牌设置交替的白色图片和正常图片来实现闪烁效果
		 */
		public void run(){
			boolean is = false;
			// 创建白色图片的图标
			ImageIcon icon = new ImageIcon("images/white.gif");
			for (int i = 0; i < 4; i++){
				try{
					// 线程休眠200毫秒
					Thread.sleep(200);
				}
				catch (InterruptedException e){
					e.printStackTrace();
				}

				if (is){
					// 如果is为true，将纸牌正面朝上
					this.card.turnFront();
					is = !is;
				}
				else{
					// 如果is为false，设置纸牌为白色图标
					this.card.setIcon(icon);
					is = !is;
				}
				// 更新纸牌的UI显示
				card.updateUI();
			}
		}
	}

	/**
	 ** 鼠标按下事件的处理方法
	 */
	public void mousePressed(MouseEvent mp){
		// 获取鼠标按下时相对于纸牌的坐标点
		point = mp.getPoint();
		// 在游戏主类中重置某些变量（可能是与纸牌操作相关的变量）
		main.setNA();
		// 获取当前纸牌的前一张纸牌
		this.previousCard = main.getPreviousCard(this);
		//获取当前纸牌的列序列。
		Point p = this.getLocation();
		beforeColumn = this.whichColumnAvailable(p);
	}

	/**
	 ** 鼠标释放事件的处理方法
	 */
	public void mouseReleased(MouseEvent mr){

		// 获取鼠标释放时纸牌的位置坐标
		Point point = ((JLabel) mr.getSource()).getLocation();
		// 获取鼠标释放时纸牌的牌堆列序列
		movedColumn = this.whichColumnAvailable(point);
		// 检查纸牌可放置的列（whichColumnAvailable方法返回值）
		int n = this.whichColumnAvailable(point);
		if (n == -1 || n == this.whichColumnAvailable(this.initPoint)){
			// 如果不可放置或者与初始列相同，则将纸牌放回初始位置
			this.setNextCardLocation(null);
			main.table.remove(this.getLocation());
			this.setLocation(this.initPoint);
			main.table.put(this.initPoint, this);
			return;
		}
		// 获取目标列的最后一张纸牌位置
		point = main.getLastCardLocation(n);
		boolean isEmpty = false;
		PKCard card = null;
		if (point == null){
			// 如果目标位置为空，获取对应的地面标签位置（可能是底部的放置区域）
			point = main.getGroundLabelLocation(n);
			isEmpty = true;
		}
		else{
			// 获取目标位置的纸牌对象
			card = (PKCard) main.table.get(point);
		}
		// 如果目标位置为空或者当前纸牌的值比目标位置纸牌的值大1
		if (isEmpty || (this.value + 1 == card.getCardValue() && this.type == card.getCardType())){
			// 如果满足放置条件，将纸牌放置到目标位置
			point.y += 40;
			if (isEmpty) point.y -= 20;
			// 设置下一张纸牌的位置（如果有下一张纸牌的话）
			this.setNextCardLocation(point);
			main.table.remove(this.getLocation());
			point.y -= 20;
			this.setLocation(point);
			main.table.put(point, this);
			this.initPoint = point;
			if (this.previousCard != null){
				// 如果有前一张纸牌，将前一张纸牌正面朝上并设置为可移动
				this.previousCard.turnFront();
				this.previousCard.setCanMove(true);
			}
			this.setCanMove(true);

			// 成功移动牌后，减一分
			main.score -= 1;
			main.updateScoreLabel(); // 更新分数标签
		}
		else{
			// 如果不满足放置条件，将纸牌放回初始位置
			this.setNextCardLocation(null);
			main.table.remove(this.getLocation());
			this.setLocation(this.initPoint);
			main.table.put(this.initPoint, this);
			return;
		}
		point = main.getLastCardLocation(n);
		card = (PKCard) main.table.get(point);

		if (card.getCardValue() == 1){
			// 如果放置后的纸牌值为1，检查是否满足特定的完成条件
			point.y -= 240;
			card = (PKCard) main.table.get(point);
			if (card != null && card.isCardCanMove()){
				main.haveFinish(n);
			}
		}
		//获取移动后的纸牌位置
		Point movedPoint = point;
		main.recordMove(beforeColumn,movedColumn,movedPoint,this);
	}

	/*
	 ** 用于设置下一张纸牌的位置的方法
	 */
	public void setNextCardLocation(Point point){
		PKCard card = main.getNextCard(this);
		if (card != null){
			if (point == null){
				// 如果目标位置为空，将下一张纸牌放回初始位置
				card.setNextCardLocation(null);
				main.table.remove(card.getLocation());
				card.setLocation(card.initPoint);
				main.table.put(card.initPoint, card);
			}
			else{
				// 如果目标位置不为空，调整下一张纸牌的位置
				point = new Point(point);
				point.y += 20;
				card.setNextCardLocation(point);
				point.y -= 20;
				main.table.remove(card.getLocation());
				card.setLocation(point);
				main.table.put(card.getLocation(), card);
				card.initPoint = card.getLocation();
			}
		}
	}

	/**
	 ** 方法接收一个Point类型的参数，返回一个int值
	 ** 用于检查纸牌可放置的列
	 */
	public int whichColumnAvailable(Point point){
		int x = point.x;
		int y = point.y;
		int a = (x - 20) / 101;
		int b = (x - 20) % 101;
		if (a != 9){
			if (b > 30 && b <= 71){
				a = -1;
			}
			else if (b > 71){
				a++;
			}
		}
		else if (b > 71){
			a = -1;
		}

		if (a != -1){
			Point p = main.getLastCardLocation(a);
			if (p == null) p = main.getGroundLabelLocation(a);
			b = y - p.y;
			if (b <= -96 || b >= 96){
				a = -1;
			}
		}
		return a;
	}

	public void mouseEntered(MouseEvent arg0){
	}

	public void mouseExited(MouseEvent arg0){
	}

	/**
	 ** 鼠标拖动事件的处理方法
	 */
	public void mouseDragged(MouseEvent arg0){
		if (canMove){
			int x = 0;
			int y = 0;
			// 获取鼠标拖动时相对于纸牌的坐标点
			Point p = arg0.getPoint();
			x = p.x - point.x;
			y = p.y - point.y;
			// 调用moving方法移动纸牌
			this.moving(x, y);
		}
	}

	/**
	 ** 方法接收x和y两个int类型的参数，无返回值
	 ** 根据传入的坐标值移动纸牌
	 */
	public void moving(int x, int y){
		PKCard card = main.getNextCard(this);
		Point p = this.getLocation();

		// 设置纸牌的显示顺序，使其在最上层显示
		pane.setComponentZOrder(this, 1 );

		// 从哈希表中移除纸牌的当前位置信息
		main.table.remove(p);
		p.x += x;
		p.y += y;
		this.setLocation(p);
		main.table.put(p, this);
		if (card != null) card.moving(x, y);
	}

	public void mouseMoved(MouseEvent arg0){
	}

	/**
	 ** 构造函数
	 */
	public PKCard(String name, Spider spider){
		super();
		// 从名称字符串中获取纸牌的类型（第一个字符转换为整数）
		this.type = Integer.parseInt(name.substring(0, 1));
		// 从名称字符串中获取纸牌的值（从第三个字符开始转换为整数）
		this.value = Integer.parseInt(name.substring(2));
		this.name = name;
		this.main = spider;
		this.pane = this.main.getContentPane();
		// 为纸牌添加鼠标事件监听器
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		// 设置纸牌初始为背面朝上的图标
		this.setIcon(new ImageIcon("images/rear.gif"));
		this.setSize(71, 96);
		this.setVisible(true);
	}

	/**
	 ** 方法无返回值
	 ** 将纸牌正面朝上，设置对应的图标
	 */
	public void turnFront(){
		this.setIcon(new ImageIcon("images/" + name + ".gif"));
		this.isFront = true;
	}

	/**
	 ** 方法无返回值
	 ** 将纸牌背面朝上，设置对应的图标并标记为不可移动
	 */
	public void turnRear(){
		this.setIcon(new ImageIcon("images/rear.gif"));
		this.isFront = false;
		this.canMove = false;
	}

	/**
	 ** 方法无返回值
	 ** 将纸牌移动到指定的坐标点，并更新初始位置
	 */
	public void moveto(Point point){
		this.setLocation(point);
		this.initPoint = point;
	}

	/**
	 ** 方法接收一个布尔类型的参数，无返回值
	 ** 设置纸牌是否可移动，并根据前一张纸牌的状态调整前一张纸牌的可移动性
	 */
	public void setCanMove(boolean can){
		this.canMove = can;
		PKCard card = main.getPreviousCard(this);
		if (card != null && card.isCardFront()){
			if (!can){
				if (!card.isCardCanMove()){
					return;
				}
				else{
					card.setCanMove(can);
				}
			}
			else{
				if (this.value + 1 == card.getCardValue()
						&& this.type == card.getCardType()){
					card.setCanMove(can);
				}
				else{
					card.setCanMove(false);
				}
			}
		}
	}

	/**
	 ** 方法返回一个布尔值
	 ** 用于判断纸牌是否正面朝上
	 */
	public boolean isCardFront(){
		return this.isFront;
	}

	/*
	 ** 方法返回一个布尔值
	 ** 用于判断纸牌是否可移动
	 */
	public boolean isCardCanMove(){
		return this.canMove;
	}

	/**
	 ** 方法返回一个int值
	 ** 获取纸牌的值
	 */
	public int getCardValue(){
		return value;
	}

	/**
	 ** 方法返回一个int值
	 ** 获取纸牌的类型
	 */
	public int getCardType(){
		return type;
	}
}
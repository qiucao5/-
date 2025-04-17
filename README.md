# 蜘蛛纸牌屋（全Java代码）-
数据结构蜘蛛纸牌屋建立
蜘蛛纸牌游戏共由 4 个部分组成， 分别  是 : Spider. Java、SpiderMenuBar. Java、PKCard. Java、AboutDialog.Java。

(1)PKCard类设计

详细介绍

包含名为PKCard的 public类， 其主要功能为： 定义纸牌的属性，包括名称，位置等相关信息，并通过相关方法实现纸牌的移动等。

用于表示纸牌对象，继承自JLabel并实现MouseListener和MouseMotionListener接口，用于处理纸牌的鼠标交互事件。

包含纸牌的属性，如位置坐标（point和initPoint）、值（value）、类型（type）、名称（name）等。

实现了纸牌的各种操作方法，如闪烁（flashCard）、鼠标事件处理（mousePressed、mouseReleased等）、纸牌的移动（moving）以及状态的改变（turnFront、turnRear）等。

与Spider类紧密相关，它持有对Spider类的引用（main），以便在纸牌操作中调用游戏主类的方法，例如获取下一张纸牌（getNextCard）、前一张纸牌（getPreviousCard）等操作。

主要方法

public void flashCard(PKCard card)：使纸牌闪烁的方法，通过创建并启动一个Flash线程来实现闪烁效果。

public void mousePressed(MouseEvent mp)：鼠标按下事件的处理方法，获取鼠标位置，计算列序列。

public void mouseReleased(MouseEvent mr)：鼠标释放事件的处理方法，判断是否正确移动。

public void setNextCardLocation(Point point)：用于设置下一张纸牌的位置的方法。

public int whichColumnAvailable(Point point)：方法接收一个Point类型的参数，返回一个int值用于检查纸牌可放置的列。

public void mouseDragged(MouseEvent arg0)：鼠标拖动事件的处理方法，牌随鼠标移动。

public void moving(int x, int y)：方法接收x和y两个int类型的参数，无返回值根据传入的坐标值移动纸牌。

public PKCard(String name, Spider spider)：构造PKCard函数。

public void turnFront()：方法无返回值将纸牌正面朝上，设置对应的图标。

public void turnRear()：方法无返回值将纸牌背面朝上，设置对应的图标并标记为不可移动。

public void moveto(Point point)：方法无返回值将纸牌移动到指定的坐标点，并更新初始位置。

public void setCanMove(boolean can)：方法接收一个布尔类型的参数，无返回值设置纸牌是否可移动，并根据前一张纸牌的状态调整前一张纸牌的可移动性。

public boolean isCardFront()：方法返回一个布尔值用于判断纸牌是否正面朝上。

public boolean isCardCanMove()：方法返回一个布尔值用于判断纸牌是否可移动。

public int getCardValue()：方法返回一个int值获取纸牌的值。

public int getCardType()：方法返回一个int值获取纸牌的类型。

(2)SpiderMenuBar类

详细介绍

包含名为 SpiderMenuBar 的 public 类, 其主要功能为生成蜘蛛纸牌游戏的菜单栏， 实现菜单栏中各个组件的事件侦听。主要包括3个模块： 图形用户界面的构建； 组件监听接口的实现； 显示可执行操作的线程。

继承自JMenuBar，用于创建游戏的菜单栏。包含“游戏”、“帮助”、“成就”等菜单，每个菜单下有多个菜单项，如“开局”、“继续发牌”、“关于”等。

为菜单项添加动作监听器，以响应用户操作，如开始新游戏、重新发牌、显示关于对话框等操作，并根据游戏状态设置菜单项的可用性。

与Spider类紧密关联，持有对Spider类的引用（main），以便在菜单项的动作监听器中调用游戏主类的方法。

主要方法

public SpiderMenuBar(Spider spider)：构造函数，接收游戏主类（Spider）的实例，用于初始化菜单栏并关联相关操作。

(3)AboutDialog类

详细介绍

包含名为 AboutDialog 的 public类， 其主要功能为生成蜘蛛纸牌游戏的帮助栏。

用于显示游戏相关的信息，如游戏版权、游戏资源和游戏规则等。它创建了一个对话框，包含选项卡面板，分别展示不同类型的信息。

是一个独立的对话框类，与游戏主类Spider有一定联系，通过JMenuItem的动作监听器，在点击“关于”菜单项时创建并显示该对话框。

主要方法

public AboutDialog()：构造函数，用于初始化关于对话框的各种属性并显示对话框。

(4)Spider类

详细介绍

游戏的主类，继承自JFrame，是整个游戏的核心控制类。

包含名为 Spider的 public类， 其主要功能为生成蜘蛛纸牌游戏的框架， 实现游戏中的方法，包括：纸牌的随机生成，位置的摆放等。包含游戏的各种属性，如难度等级（grade）、纸牌数组（cards）、计分变量（score）、哈希表

（table）用于存储纸牌位置信息等。

实现游戏的主要逻辑，包括初始化游戏（initCards、randomCards、setCardsLocation）、处理游戏操作（newGame、deal、undo、showEnableOperator等）、与用户交互（鼠标和键盘事件处理）以及管理游戏的状态（如判断游戏胜利、增

加成就等）。

与其他类紧密协作，例如在SpiderMenuBar类中调用其方法来实现菜单栏功能，PKCard类在游戏逻辑中处理纸牌相关操作并依赖Spider类提供的全局信息和操作方法。

主要方法

public void recordMove(int previousplace,int movedplace, Point movedpoint,  PKCard moveCard)：记录历史发牌，为撤销做准备。

public void updateScoreLabel()：更新分数。

public void newGame()：重新开始游戏。

public int getC()：获取已发牌数量的方法。

public void setGrade(int grade)：设置游戏难度等级的方法。

public void initCards()：初始化纸牌的方法。

public void randomCards()：打乱纸牌顺序的方法。

public void setNA()：重置a和n变量的方法。

public void setCardsLocation()：设置纸牌位置的方法。

public void showEnableOperator()：显示可操作纸牌的方法。

public void deal()：发牌操作的方法。

public PKCard getPreviousCard(PKCard card)：获取指定纸牌的前一张纸牌的方法。

public PKCard getNextCard(PKCard card)：获取指定纸牌的下一张纸牌的方法。

public void undo()：撤销的方法。

public Point getLastCardLocation(int column)：获取指定列的最后一张纸牌位置的方法。

public void setGroundLabelZOrder()：设置groundLabel显示顺序（Z - Order）的方法。

public void haveFinish(int column)：当一列牌完成时的处理方法。

public void addAchievement(String achievement)：添加成就的方法。

public void viewAchievements()：查看成就的方法。

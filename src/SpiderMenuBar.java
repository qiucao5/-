
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.ButtonGroup;
//SpiderMenuBar类继承自JMenuBar，用于创建游戏的菜单栏
public class SpiderMenuBar extends JMenuBar{

    // 对游戏主类（Spider）的引用，用于在菜单栏类中调用游戏主类的方法
    Spider main = null;

    // 创建“游戏”菜单对象
    JMenu jNewGame = new JMenu("游戏");
    // 创建“帮助”菜单对象
    JMenu jHelp = new JMenu("帮助");
    // 创建“成就”菜单对象
    JMenu jAchievements = new JMenu("成就");
    // 创建“关于”菜单项对象
    JMenuItem jItemAbout = new JMenuItem("关于");
    // 创建“开局”菜单项对象
    JMenuItem jItemOpen = new JMenuItem("开局");
    // 创建“重新发牌”菜单项对象
    JMenuItem jItemPlayAgain = new JMenuItem("继续发牌");
    // 创建“撤销”菜单项对象
    JMenuItem jItemUndo = new JMenuItem("撤销");
    // 创建表示“简单·单一花色”的单选菜单项对象
    JRadioButtonMenuItem jRMItemEasy = new JRadioButtonMenuItem("简单·单一花色");
    // 创建表示“中等·双花色”的单选菜单项对象
    JRadioButtonMenuItem jRMItemNormal = new JRadioButtonMenuItem("中等·双花色");
    // 创建表示“困难·四花色”的单选菜单项对象
    JRadioButtonMenuItem jRMItemHard = new JRadioButtonMenuItem("困难·四花色");;
    // 创建“结束游戏”菜单项对象
    JMenuItem jItemExit = new JMenuItem("结束游戏");
    // 创建“显示可用操作”菜单项对象
    JMenuItem jItemValid = new JMenuItem("显示可用操作");
    // 创建“查看成就”菜单项对象
    JMenuItem jItemViewAchievements = new JMenuItem("查看成就");


    /**
     ** 构造函数，接收游戏主类（Spider）的实例，用于初始化菜单栏并关联相关操作
     */
    public SpiderMenuBar(Spider spider){

        this.main = spider;

        /**
         ** 初始化“游戏”菜单
         */
        jNewGame.add(jItemOpen);
        jNewGame.add(jItemPlayAgain);
        jNewGame.add(jItemUndo);//撤销
        jNewGame.add(jItemValid);

        jNewGame.addSeparator();
        jNewGame.add(jRMItemEasy);
        jNewGame.add(jRMItemNormal);
        jNewGame.add(jRMItemHard);

        jNewGame.addSeparator();

        jNewGame.add(jItemExit);
        // 初始化“成就”菜单
        jAchievements.add(jItemViewAchievements);
        // 创建一个按钮组，用于确保单选菜单项的互斥选择
        ButtonGroup group = new ButtonGroup();
        //难度选择
        group.add(jRMItemEasy);
        group.add(jRMItemNormal);
        group.add(jRMItemHard);

        jHelp.add(jItemAbout);
        // 将菜单添加到菜单栏中
        this.add(jNewGame);
        this.add(jAchievements);
        this.add(jHelp);

        // 为“开局”菜单项添加动作监听器，当点击时调用游戏主类的newGame方法
        jItemOpen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                main.newGame();
            }
        });

        // 为“重新发牌”菜单项添加动作监听器，当点击且游戏中的某个计数（getC方法获取）小于60时调用deal方法
        jItemPlayAgain.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                if(main.getC() < 60){
                    main.deal();
                }
            }
        });

        // 为“显示可用操作”菜单项添加动作监听器，当点击时启动一个新的Show线程来显示可用操作
        jItemValid.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                new Show().start();
            }
        });

        // 为“结束游戏”菜单项添加动作监听器，当点击时关闭游戏主窗口并退出程序
        jItemExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                main.dispose();
                System.exit(0);
            }
        });

        //为撤销按钮添加动作监听器，当点击时调用游戏主类的undo方法
        jItemUndo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                main.undo();
            }
        });

        // 设置“简单”单选菜单项为默认选中状态
        jRMItemEasy.setSelected(true);

        // 为“简单·单一花色”单选菜单项添加动作监听器，当选择时设置游戏难度为简单（Spider.EASY），初始化纸牌并开始新游戏
        jRMItemEasy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                main.setGrade(Spider.EASY);
                main.initCards();
                main.newGame();
            }
        });

        //中等难度
        jRMItemNormal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                main.setGrade(Spider.NATURAL);
                main.initCards();
                main.newGame();
            }
        });

        //困难难度
        jRMItemHard.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                main.setGrade(Spider.HARD);
                main.initCards();
                main.newGame();
            }
        });
        // 为“游戏”菜单添加菜单监听器，当菜单被选中时，根据游戏中的某个计数（getC方法获取）来设置“从新发牌”菜单项是否可用
        jNewGame.addMenuListener(new javax.swing.event.MenuListener() {
            public void menuSelected(javax.swing.event.MenuEvent e) {
                if(main.getC() < 60){
                    jItemPlayAgain.setEnabled(true);
                }
                else{
                    jItemPlayAgain.setEnabled(false);
                }
            }
            public void menuDeselected(javax.swing.event.MenuEvent e) {}
            public void menuCanceled(javax.swing.event.MenuEvent e) {}
        });

        // 为“关于”菜单项添加动作监听器，当点击时创建一个关于对话框（AboutDialog）
        jItemAbout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                new AboutDialog();
            }
        });

        // 为“查看成就”菜单项添加动作监听器
        jItemViewAchievements.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                main.viewAchievements();
            }
        });
    }

    /**
     ** Show内部类，继承自Thread，用于显示游戏中的可用操作
     */
    class Show extends Thread{
        public void run(){
            main.showEnableOperator();
        }
    }
}

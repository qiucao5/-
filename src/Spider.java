import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.util.List;

public class Spider extends JFrame{

    // 难度等级常量
    public static final int EASY = 1;
    public static final int NATURAL = 2;
    public static final int HARD = 3;
    // 成员变量
    public int grade = Spider.EASY;
    public Container pane = null;
    public PKCard cards[] = new PKCard[104];
    public JLabel clickLabel = null;
    public int c = 0;
    public int n = 0;
    public int a = 0;
    public int finish = 0;
    public int score = 500; //计分变量
    public Hashtable table = null;
    public JLabel groundLabel[] = null;
    public JLabel scoreLabel; //显示分数的标签

    List<String> achievements = new ArrayList<>();
    //记录移出和移入的牌堆2
    public Stack<LinkedList<PKCard>> OutHistory = new Stack<>();
    public Stack<LinkedList<PKCard>> InHistory = new Stack<>();
    //记录成功移动的牌 ，其移动后的位置
    public Stack<Point> movedCard = new Stack<>();
    //记录成功移动的牌，其移动前的前一张牌的位置
    public Stack<Point> previousCard = new Stack<>();
    int beforeColumn;
    int afterColumn;

    // 主函数，创建Spider类的实例并设置可见，启动游戏
    public static void main(String[] args){
        Spider spider = new Spider();
        spider.setVisible(true);
    }

    public Spider(){
        // 设置字体
        Font font = new Font("Dialog", Font.PLAIN, 12);
        // 设置所有UI组件的默认字体
        java.util.Enumeration keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof javax.swing.plaf.FontUIResource) {
                // 如果是字体资源类型，将其设置为指定字体
                UIManager.put(key, font);
            }
        }
        setTitle("蜘蛛纸牌");
        setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
        setSize(1024, 742);
        // 设置菜单栏和容器
        setJMenuBar(new SpiderMenuBar(this));
        pane = this.getContentPane();
        pane.setBackground(new Color(0, 112, 26));
        pane.setLayout(null);
        // 继续发牌
        clickLabel = new JLabel();
        clickLabel.setBounds(883, 606, 121, 96);
        pane.add(clickLabel);
        // 为点击区域添加鼠标监听器
        clickLabel.addMouseListener(new MouseAdapter(){
            public void mouseReleased(MouseEvent me){
                if (c < 60){
                    Spider.this.deal();
                }
            }
        });
        // 初始化卡片和游戏区域
        this.initCards();
        this.randomCards();
        this.setCardsLocation();
        groundLabel = new JLabel[10];

        int x = 20;
        for (int i = 0; i < 10; i++)
        {
            groundLabel[i] = new JLabel();
            groundLabel[i]
                    .setBorder(javax.swing.BorderFactory
                            .createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
            groundLabel[i].setBounds(x, 25, 71, 96);
            x += 101;
            this.pane.add(groundLabel[i]);
        }
        // 初始化分数标签
        scoreLabel = new JLabel("分数");
        scoreLabel.setFont(new Font("Dialog", Font.BOLD, 34));
        scoreLabel.setText("分数: " + score);
        scoreLabel.setForeground(Color.yellow);
        scoreLabel.setBounds(442, 600, 200, 50);
        pane.add(scoreLabel);
        // 设置分数标签的可见性
        scoreLabel.setVisible(true);

        //设置scoreLabel显示顺序为最上层
        this.pane.setComponentZOrder(scoreLabel, 0);
        // 设置游戏区域的背景图片
        this.setVisible(true);
        this.deal();
        // 添加键盘监听器
        this.addKeyListener(new KeyAdapter(){
            class Show extends Thread{
                public void run(){
                    // 当新线程启动时，调用showEnableOperator方法显示可操作的纸牌
                    Spider.this.showEnableOperator();
                }
            }

            public void keyPressed(KeyEvent e){
                if (finish != 8) if (e.getKeyCode() == KeyEvent.VK_D && c < 60){
                    // 如果按下D键且发牌次数小于60，调用deal方法发牌
                    Spider.this.deal();
                }
                else if (e.getKeyCode() == KeyEvent.VK_M){
                    new Show().start();
                }
            }
        });
    }

    //记录历史发牌
    public void recordMove(int previousplace,int movedplace, Point movedpoint,  PKCard moveCard){
        beforeColumn = previousplace;
        afterColumn = movedplace;
        //用于记录和移出和移入的牌
        //用于记录和移出和移入的牌
        LinkedList<PKCard> historyOut = new LinkedList<>();
        LinkedList<PKCard> historyIn = new LinkedList<>();

        //获取移动前的前一张牌的位置
        Point lastCard = getLastCardLocation(previousplace);
        PKCard currentCard = moveCard;
        //记录移动成功后的前一牌堆的所有值
        while (lastCard != null){
            PKCard card = (PKCard) this.table.get(lastCard);
            if (card!= null){
                historyOut.add(card);
                PKCard nextCard = getNextCard(card);
                if (nextCard != null){
                    lastCard = nextCard.getLocation();
                }else{
                    break;
                }
            }else{
                break;
            }
        }
        //获取移动后的牌的位置
        Point currentPoint = movedpoint;
        //记录成功移动的牌组
        while (currentCard != null){
            historyIn.add(currentCard);
            currentCard = getNextCard(currentCard);
            if (currentCard!= null){
                currentPoint = currentCard.getLocation();
            }else {
                break;
            }
        }
        //记录成功移动的牌，其移动前的前一牌堆的值
        OutHistory.push(historyOut);
        //记录成功移动的牌组
        InHistory.push(historyIn);
        //记录成功移动的牌，其移动前的前一张牌的位置
        previousCard.push(getLastCardLocation(previousplace));
        //记录成功移动的牌，其移动后的位置
        movedCard.push(movedpoint);
    }

    public void updateScoreLabel(){
        scoreLabel.setText("分数: " + score);
    }

    // 重新开始游戏的方法
    public void newGame(){
        this.score = 500;
        this.achievements.clear();
        this.scoreLabel.setText("分数:"+this.score);
        this.updateScoreLabel();
        this.initCards();
        this.randomCards();
        this.setCardsLocation();
        this.setGroundLabelZOrder();
        this.deal();
    }

    // 获取已发牌数量的方法
    public int getC(){
        return c;
    }

    // 设置游戏难度等级的方法
    public void setGrade(int grade){
        this.grade = grade;
    }

    // 初始化纸牌的方法
    public void initCards(){
        // 如果纸牌数组的第一个元素不为空，说明已经有纸牌存在，先从面板移除所有纸牌
        if (cards[0] != null){
            for (int i = 0; i < 104; i++){
                pane.remove(cards[i]);
            }
        }

        int n = 0;
        // 根据游戏难度等级确定n的值，用于后续纸牌的创建逻辑ֵ
        if (this.grade == Spider.EASY){
            n = 1;
        }
        else if (this.grade == Spider.NATURAL){
            n = 2;
        }
        else{
            n = 4;
        }
        // 创建PKCard对象填充纸牌数组
        for (int i = 1; i <= 8; i++){
            for (int j = 1; j <= 13; j++){
                cards[(i - 1) * 13 + j - 1] = new PKCard((i % n + 1) + "-" + j,
                        this);
            }
        }

        // 打乱纸牌顺序
        this.randomCards();
    }

    // 打乱纸牌顺序的方法
    public void randomCards(){
        PKCard temp = null;
        // 通过随机交换纸牌数组中的元素来打乱纸牌顺序
        for (int i = 0; i < 52; i++){
            int a = (int) (Math.random() * 104);
            int b = (int) (Math.random() * 104);
            temp = cards[a];
            cards[a] = cards[b];
            cards[b] = temp;
        }
    }

    // 重置a和n变量的方法
    public void setNA(){
        a = 0;
        n = 0;
    }

    // 设置纸牌位置的方法
    public void setCardsLocation(){
        table = new Hashtable();
        c = 0;
        finish = 0;
        n = 0;
        a = 0;
        int x = 883;
        int y = 580;
        // 初始化未显示的纸牌（在初始布局中的下方部分纸牌）
        for (int i = 0; i < 6; i++){
            for (int j = 0; j < 10; j++){
                int n = i * 10 + j;
                pane.add(cards[n]);
                // 将纸牌翻面（背面朝上）
                cards[n].turnRear();
                // 移动纸牌到指定位置
                cards[n].moveto(new Point(x, y));
                // 将纸牌位置信息存入哈希表
                table.put(new Point(x, y), cards[n]);
            }
            x += 10;
        }

        x = 20;
        y = 45;
        // 初始化显示的纸牌（在初始布局中的上方部分纸牌）
        for (int i = 10; i > 5; i--){
            for (int j = 0; j < 10; j++){
                int n = i * 10 + j;
                if (n >= 104) continue;
                pane.add(cards[n]);
                cards[n].turnRear();
                cards[n].moveto(new Point(x, y));
                table.put(new Point(x, y), cards[n]);
                x += 101;
            }
            x = 20;
            y -= 5;
        }
    }

    // 显示可操作纸牌的方法
    public void showEnableOperator(){
        //初始化变量
        int x = 0;
        out: while (true){
            Point point = null;
            PKCard card = null;
            //找到可移动的卡片
            do{
                if (point != null){
                    n++;
                }
                point = this.getLastCardLocation(n);
                while (point == null){
                    point = this.getLastCardLocation(++n);
                    if (n == 10) n = 0;
                    x++;
                    if (x == 10) break out;
                }
                card = (PKCard) this.table.get(point);
            }
            while (!card.isCardCanMove());
            //找到最前面的可移动卡片
            while (this.getPreviousCard(card) != null
                    && this.getPreviousCard(card).isCardCanMove()){
                card = this.getPreviousCard(card);
            }
            //寻找其他位置符合条件的卡片
            if (a == 10){
                a = 0;
            }
            for (; a < 10; a++){
                if (a != n){
                    Point p = null;
                    PKCard c = null;
                    do{
                        if (p != null){
                            a++;
                        }
                        p = this.getLastCardLocation(a);
                        int z = 0;
                        while (p == null){
                            p = this.getLastCardLocation(++a);
                            if (a == 10) a = 0;
                            if (a == n) a++;
                            z++;
                            if (z == 10) break out;
                        }
                        c = (PKCard) this.table.get(p);
                    }
                    while (!c.isCardCanMove());
                    if (c.getCardValue() == card.getCardValue() + 1 && c.getCardType() == card.getCardType()){
                        card.flashCard(card);
                        try{
                            Thread.sleep(800);
                        }
                        catch (InterruptedException e){
                            e.printStackTrace();
                        }
                        c.flashCard(c);

                        a++;
                        if (a == 10){
                            n++;
                        }
                        break out;
                    }
                }
            }
            n++;
            if (n == 10){
                n = 0;
            }
            x++;
            if (x == 10){
                break out;
            }
        }
    }

    // 发牌操作的方法
    public void deal()
    {
        this.setNA();
        // 检查10个牌堆是否有空位
        for (int i = 0; i < 10; i++){
            if (this.getLastCardLocation(i) == null){
                JOptionPane.showMessageDialog(this, "该位置没有牌，无法发牌", "提示",
                        JOptionPane.WARNING_MESSAGE);
                this.score -=5;//扣分
                this.updateScoreLabel();//更新分数标签
                return;
            }
        }
        // 发牌
        int x = 20;
        for (int i = 0; i < 10; i++){
            Point lastPoint = this.getLastCardLocation(i);
            // 根据发牌次数调整纸牌的目标位置
            if (c == 0){
                lastPoint.y += 5;
            }
            else{
                lastPoint.y += 20;
            }

            table.remove(cards[c + i].getLocation());
            cards[c + i].moveto(lastPoint);
            table.put(new Point(lastPoint), cards[c + i]);
            cards[c + i].turnFront();
            cards[c + i].setCanMove(true);

            // 调整纸牌的显示顺序（Z - Order）
            this.pane.setComponentZOrder(cards[c + i], 1);

            Point point = new Point(lastPoint);
            if (cards[c + i].getCardValue() == 1){
                int n = cards[c + i].whichColumnAvailable(point);
                point.y -= 240;
                PKCard card = (PKCard) this.table.get(point);
                if (card != null && card.isCardCanMove()){
                    this.haveFinish(n);
                }
            }
            x += 101;
        }
        c += 10;
    }

    //找到当前牌堆的最小一张牌的位置
    public PKCard getMinimumCard(int column){
        Point point = this.getLastCardLocation(column);//获取位置信息

        PKCard minCard = null;
        //遍历牌堆，找到最小的牌
        while (point != null) {
            PKCard card = (PKCard) this.table.get(point);
            if (minCard == null || (card != null && card.getCardValue() < minCard.getCardValue())) {
                minCard = card; // 更新最小值
            }
            point = this.getPreviousCard(card).getLocation(); // 获取上一张牌的位置
        }
        return minCard; // 返回最小值的牌
    }

    // 获取指定纸牌的前一张纸牌的方法
    public PKCard getPreviousCard(PKCard card){
        Point point = new Point(card.getLocation());
        point.y -= 5;
        card = (PKCard) table.get(point);
        if (card != null){
            return card;
        }
        point.y -= 15;
        card = (PKCard) table.get(point);
        return card;
    }

    // 获取指定纸牌的下一张纸牌的方法
    public PKCard getNextCard(PKCard card){
        Point point = new Point(card.getLocation());
        point.y += 5;
        card = (PKCard) table.get(point);
        if (card != null) {
            return card;
        }
        point.y += 15;
        card = (PKCard) table.get(point);
        return card;
    }

    //撤销的方法
    public void undo() {
        if (OutHistory.isEmpty() || InHistory.isEmpty() || previousCard.isEmpty() || movedCard.isEmpty()) {
            JOptionPane.showMessageDialog(this, "没有可撤销的操作", "提示",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        // 获得最近一次操作
        LinkedList<PKCard> historyOut = OutHistory.pop();
        LinkedList<PKCard> historyIn = InHistory.pop();
        Point beforePoint = previousCard.pop();
        while (beforePoint == null){
            beforePoint = previousCard.pop();
        }
        Point movedPoint = movedCard.pop();

        beforePoint = new Point(beforePoint.x, beforePoint.y + 20); // 调整撤销前的位置
        for (PKCard card :historyIn) {
            Point point = card.getLocation();
            table.remove(point); // 从哈希表中移除
            card.moveto(beforePoint); // 将牌移回原来的位置
            table.put(new Point(beforePoint), card); // 将牌位置信息存入哈希表
            pane.setComponentZOrder(card, 1); // 调整牌的显示顺序

            // 更新 0beforePoint 为当前牌的位置
            beforePoint = new Point(beforePoint.x, beforePoint.y + 20); // 每张牌之间相差20像素
        }
        // 恢复纸牌的移动状态
        PKCard card = historyIn.pop();
            card.setCanMove(true);
            // 重新设置前一张牌的移动状态
            PKCard prevCard = getPreviousCard(card);
            if (prevCard != null) {
                if (card.value - 1 == prevCard.getCardValue()
                        && card.type == prevCard.getCardType()){
                    prevCard.setCanMove(true);
                }
                else{
                    prevCard.setCanMove(false);
                }
            }

        // 更新分数
        this.score -= 5; // 撤销操作时扣除5分
        this.updateScoreLabel();
    }

    // 判断是否可以翻开牌的方法
    public boolean canTurnFront(PKCard card){
        Point point = card.getLocation();
        PKCard previousCard = this.getPreviousCard(card);
        PKCard nextCard = this.getNextCard(card);
        if (previousCard != null && previousCard.isCardCanMove()){
            return false;
        }
        if (nextCard != null && nextCard.isCardCanMove()){
            return false;
        }
        return true;
    }

    // 获取指定列的最后一张纸牌位置的方法
    public Point getLastCardLocation(int column){
        Point point = new Point(20 + column * 101, 25);
        PKCard card = (PKCard) this.table.get(point);
        if (card == null) return null;
        while (card != null){
            point = card.getLocation();
            card = this.getNextCard(card);
        }
        return point;
    }

    public Point getGroundLabelLocation(int column){
        return new Point(groundLabel[column].getLocation());
    }

    // 设置groundLabel显示顺序（Z - Order）的方法
    public void setGroundLabelZOrder(){
        for (int i = 0; i < 10; i++){
            // 设置groundLabel的显示顺序，按照特定的规则确定顺序值
            pane.setComponentZOrder(groundLabel[i], 105 + i);
        }
    }

    // 当一列牌完成时的处理方法
    public void haveFinish(int column){
        Point point = this.getLastCardLocation(column);
        PKCard card = (PKCard) this.table.get(point);
        do{
            this.table.remove(point);
            card.moveto(new Point(20 + finish * 10, 580));
            // 调整纸牌的显示顺序（Z - Order）
            pane.setComponentZOrder(card, 1);
            // 将纸牌的新位置信息存入哈希表
            this.table.put(card.getLocation(), card);
            card.setCanMove(false);
            point = this.getLastCardLocation(column);
            if (point == null)
                card = null;
            else
                card = (PKCard) this.table.get(point);
        }
        while (card != null && card.isCardCanMove());

        // 给予称号或成就
        String[] achievements = {
                "初学者",
                "熟练者",
                "专家",
                "大师",
                "传奇",
                "无敌",
                "神技",
                "完美"
        };

        if (finish < achievements.length) {
            String achievement = achievements[finish];
            this.addAchievement(achievement);
            JOptionPane.showMessageDialog(this, "恭喜完成第 " + (finish + 1) + " 列！称号：" + achievements[finish], "成就",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {// 如果所有列都完成，显示胜利提示框
            JOptionPane.showMessageDialog(this, "游戏胜利，恭喜通关", "成功",
                    JOptionPane.PLAIN_MESSAGE);
        }

        finish++;
        if (card != null&& !card.isCardFront()){
            card.turnFront();
            card.setCanMove(true);
        }
        // 计分
        this.score += 10;
        this.updateScoreLabel();
    }
    // 增加成就的方法
    public void addAchievement(String achievement)
    {
        achievements.add(achievement);
    }

    //查看成就的方法
    public void viewAchievements()
    {
        if (achievements.isEmpty()) {
            JOptionPane.showMessageDialog(this, "还没有获得任何成就称号。", "成就",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            StringBuilder achievementsText = new StringBuilder("已获得的成就称号：\n");
            for (String achievement : achievements) {
                achievementsText.append(achievement).append("\n");
            }

            JOptionPane.showMessageDialog(this, achievementsText.toString(), "成就",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
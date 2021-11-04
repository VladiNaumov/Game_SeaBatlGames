
 
public enum ElementStates {
        enEmpty, enWater, enBorder, enWell, enInjured, enKilled, enMissed
}
code: #java
package ru.semikov.sea.logic;
 
public class Element {
        public ElementStates state;
        public Ship ship;
        public boolean shuted;
        public int x, y;
 
 
        public Element(int x, int y) {
                this.state=ElementStates.enWater;
                this.shuted = false;
                this.x = x;
                this.y = y;
        }
}
code: #java
package ru.semikov.sea.logic;
 
public enum ShipStates {
        enWell, enInjured, enKilled
}
code: #java
package ru.semikov.sea.logic;
 
import java.util.ArrayList;
import java.util.Random;
 
 
public class Ship {
        public int x, y;
        private int dx, dy;
        public int size;
        public int health;
        public ShipStates state;
        public FieldShip field;
        public ArrayList<Element> elements;
 
        /**
         * создание коробля
         * @param size - количество палуб
         */
        public Ship(FieldShip field, int size) {
                this.size = size;
                this.health = size;
                this.field = field;
                this.state = ShipStates.enWell;
 
                do {
                        this.GetPlace();
                } while (!this.CheckPlace());
 
                this.elements = new ArrayList<Element>();
                this.SetShip();
        }
 
        /**
         * Генерирует случайное положение корабля и его направление
         */
        private void GetPlace() {
                Random rand = new Random();
                this.x = rand.nextInt(10);
                this.y = rand.nextInt(10);
                this.dx = 0;
                this.dy = 0;
                if (rand.nextInt(2) == 1) {
                        this.dx = 1;
                } else {
                        this.dy = 1;
                }
        }
 
        /**
         * Функция обхода корабля и его окружения
         * 
         * @return
         */
        private boolean ByPass(TrigerShip tp) {
                int i, m, n;
 
                // корабль
                for(i = 0; i < size; i++) {
                        m = y+i*dy;
                        n = x+i*dx;
                        if (tp.Ship(m,n) == false) {
                                return false;
                        }
                }
                // площадка сверху и снизу корабля
                for(i=0; i < size; i++) {
                        m = y+i*dy-dx;
                        n = x+i*dx-dy;
                        if (tp.Border(m, n) == false) {
                                return false;
                        }
                        m = y+i*dy+dx;
                        n = x+i*dx+dy;
                        if (tp.Border(m, n) == false) {
                                return false;
                        }
                }
                // площадка слева и справа корабля
                for(i = -1; i < 2; i++) {
                        m = y+i*dx-dy;
                        n = x+i*dy-dx;
                        if (tp.Border(m, n) == false) {
                                return false;
                        }
                        m = y+i*dx+(dy*size);
                        n = x+i*dy+(dx*size);
                        if (tp.Border(m, n) == false) {
                                return false;
                        }
                }
                return true;
        }
 
        /**
         * Проверка корректности нахождения корабля на поле
         * @return
         */
        private boolean CheckPlace() {
                return ByPass(new TrigerShipCheck(this));
        }
 
        /**
         * Установка на поле корабля и его окружения 
         */
        private void SetShip() {
                ByPass(new TrigerShipSet(this));
        }
}
code: #java
package ru.semikov.sea.logic;
 
public abstract class TrigerShip {
        public FieldShip field;
 
        public TrigerShip(Ship ship) {
                this.field = ship.field;
        }
 
        abstract public boolean Ship(int m, int n);
        abstract public boolean Border(int m, int n);
}
code: #java
package ru.semikov.sea.logic;
 
public class TrigerShipCheck extends TrigerShip {
 
        public TrigerShipCheck(Ship ship) {
                super(ship);
        }
 
        @Override
        public boolean Ship(int m, int n) {
                ElementStates state = field.GetElement(m, n);
                return (state == ElementStates.enWater);
        }
 
        @Override
        public boolean Border(int m, int n) {
                ElementStates state = field.GetElement(m, n);
                return (state == ElementStates.enBorder) || 
                                (state == ElementStates.enWater) || 
                                        (state == ElementStates.enEmpty);
        }
}
code: #java
package ru.semikov.sea.logic;
 
public class TrigerShipSet extends TrigerShip {
 
        private Ship ship;
 
        public TrigerShipSet(Ship ship) {
                super(ship);
                this.ship = ship;
        }
 
        @Override
        public boolean Ship(int m, int n) {
                field.SetElement(m, n, ElementStates.enWell);
                ship.elements.add(field.elements[m][n]);
                field.elements[m][n].ship = ship;
                return true;
        }
 
        @Override
        public boolean Border(int m, int n) {
                field.SetElement(m, n, ElementStates.enBorder);
                return true;
        }
}
code: #java
package ru.semikov.sea.logic;
 
import java.util.ArrayList;
 
public class FieldShip {
        public Element[][] elements;
        public ArrayList<Ship> ships;
 
        /**
         * создание поля с кораблями
         */
        public FieldShip() {
                // заполняем поле элементами воды
                elements = new Element[10][10]; 
                for(int j=0; j<10; j++) {
                        for(int i=0; i<10; i++) {
                                elements[i][j]=new Element(i, j);
                        }
                }
                this.PutShip();
        }
 
        /**
         * Заполняем поле водой и расставляем корабли
         */
        public void PutShip() {
 
                // заполняем поле водой
                for(int j=0; j<10; j++) {
                        for(int i=0; i<10; i++) {
                                Element element = elements[i][j];
                                element.state = ElementStates.enWater;
                                element.shuted = false;
                        }
                }
 
                // заполняем поле короблями
                ships = new ArrayList<Ship>(); 
                for(int i=4; i>0; i--) {
                        for(int j=(5-i); j>0; j--) {
                                Ship ship=new Ship(this,i);
                                ships.add(ship);
                        }
                }
 
                // удаляем окружение коробля
                for(int j=0; j<10; j++) {
                        for(int i=0; i<10; i++) {
                                Element element = elements[i][j];
                                if (element.state == ElementStates.enBorder) {
                                        element.state = ElementStates.enWater;
                                }
                        }
                }
 
        }
 
        /**
         * Сделать выстрел
         * @param x
         * @param y
         * @return
         * Возвращает результат выстрела
         */
        public boolean doShot(int x, int y) {
                boolean shot = false;
 
                ElementStates state = this.GetElement(x, y);
                elements[x][y].shuted = true;
                if (state == ElementStates.enWell) {
                        shot = true;
                        Ship ship = elements[x][y].ship; 
                        if (ship.health != 0) {
                                ship.health--;
                                if (ship.health == 0) {
                                        ship.state = ShipStates.enKilled;
                                        for(Element e : ship.elements) {
                                                e.state = ElementStates.enKilled;
                                        }
                                } else {
                                        ship.state = ShipStates.enInjured;
                                        elements[x][y].state = ElementStates.enInjured;
                                }
                        }
                } else {
                        if ( (state == ElementStates.enBorder) || 
                                                (state == ElementStates.enWater)) {
                                this.SetElement(x, y, ElementStates.enMissed);
                        }
                }
                return shot;
        }
 
        /**
         * Проверка координат в пределах поля 
         * @param x
         * @param y
         * @return - boolean
         */
        public boolean isBound(int x, int y) {
                return !( (x<0) || (x>9) || (y<0) || (y>9) );
        }
 
        /**
         * Получить по координатам тип элемента 
         * 
         * @param x
         * @param y
         * @return
         */
        public ElementStates GetElement(int x, int y) {
                if (isBound(x,y)) {
                        return elements[x][y].state;
                } else {
                        return ElementStates.enEmpty;
                }
        }
 
        /**
         * Установить по координатам тип элемента
         * @param x
         * @param y
         * @param state
         */
        public boolean SetElement(int x, int y, ElementStates state) {
                if (isBound(x,y)) {
                        elements[x][y].state = state;
                }
                return true;
        }
 
        /**
         * отрисовка поля
         */
        public void Draw() {
                for(int j=0; j<10; j++) {
                        for(int i=0; i<10; i++) {
                                System.out.print(elements[i][j].toString());
                        }
                        System.out.println();
                }
        }
 
}
code: #java
package ru.semikov.sea.logic;
 
import java.util.ArrayList;
import java.util.Random;
 
/**
 * Исскуственный интелект  
 */
public class Robot {
 
        public FieldShip field;
        public int x, y;
        Random rand;
 
        /**
         * Создание робота для выполнения хода
         * создаётся для определённого поля  
         */
        public Robot(FieldShip field) {
                this.field = field;
                this.rand = new Random();
        }
 
 
        /**
         * Попытка сделать выстрел
         * @return
         */
        public boolean tryShot() {
                ArrayList<Element> list = new ArrayList<Element>();
 
                for (int j = 0; j < 10; j++) {
                        for (int i = 0; i < 10; i++) {
                                Element e = field.elements[i][j];
                                if (!e.shuted) { 
                                        list.add(e);
                                }
                        }
                }
                Element e = list.get(rand.nextInt(list.size()));
                return field.doShot(e.x, e.y);
        }
 
        /**
         * сделать ход
         */
        public boolean move() {
                boolean finded = false;
                // ищём раненный элемент корабля
                first: for (int j = 0; j < 10; j++) {
                        for (int i = 0; i < 10; i++) {
                                Element element = field.elements[i][j];
                                if (element.state == ElementStates.enInjured) {
                                        // нашли раненный элемент корабля
                                        finded = true;
                                        x = i;
                                        y = j;
                                        break first;
                                }
                        }
                }
/*              
                if (finded) {
                        //TODO добавить здесь анализатор
                        System.out.printf("finded> %s,%s\n", x, y);
 
                        ArrayList<Element> list = new ArrayList<Element>();
 
                        // пробуем стрелять вокруг раненого корабля
                        // выбрал случайную позицию
                        for(int j = 0; j < 2; j++) {
                                int a = x;
                                int b = y + j*2-1;
                                if ( (b < 0) || (b>9) ) {
                                        continue;
                                }
                                if (!field.elements[a][b].shuted) {
                                        list.add(field.elements[a][b]);
                                        System.out.printf("> %s,%s\n", a, b);
                                }
                        }
                        for(int i = 0; i < 2; i++) {
                                int a = x + i*2-1;
                                int b = y;
                                if ( (a < 0) || (a>9) ) {
                                        continue;
                                }
                                if (!field.elements[a][b].shuted) {
                                        list.add(field.elements[a][b]);
                                        System.out.printf("> %s,%s\n", a, b);
                                }
                        }
                        Element e = list.get(rand.nextInt(list.size()));
                        return field.doShot(e.x, e.y);
                }
*/
                return tryShot();
        }
 
        /**
         * Получить координуту X
         * @return
         */
        public int getX() {
                return this.x;
        }
 
        /**
         * Получить координуту Y
         * @return
         */
        public int getY() {
                return this.y;
        }
 
}
code: #java
package ru.semikov.sea.swing;
 
public interface ISubscriber {
        public void update();
}
отображение

code: #java
package ru.semikov.sea.swing;
 
import javax.swing.UIManager;
 
public class FrameGameLauncher {
 
        /**
         * Launch the application.
         */
        public static void main(String[] args) {
                try {
                        UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel");
                } catch (Throwable e) {
                        e.printStackTrace();
                }
                GameModel model = new GameModel();
                GameView view = new GameView(model);
                view.setVisible(true);          
        }
 
}
code: #java
package ru.semikov.sea.swing;
 
import java.util.ArrayList;
import java.util.Iterator;
 
import ru.semikov.sea.logic.FieldShip;
import ru.semikov.sea.logic.Robot;
 
/**
 * Представление главного окна
 */
public class GameModel {
        private ArrayList<ISubscriber> listeners = new ArrayList<ISubscriber>();
        public FieldShip playerFiledA;
        public FieldShip playerFiledB;
        public Robot robot;
        public int currentPlayer;
 
        /**
         * создание модели основной формы
         */
        public GameModel() {
                this.currentPlayer = 0;
                playerFiledA = new FieldShip();
                playerFiledB = new FieldShip();
                robot = new Robot(playerFiledA);
        }
 
        /**
         * Расставление кораблей заново 
         */
        public void newGame() {
                playerFiledA.PutShip();
                playerFiledB.PutShip();
                updateSubscribers();
        }
 
        /**
         * Выстрел по текущему игроку
         */
        public void doShotByOpponent(int x, int y) {
                // если ходит локальный игрок
                if (currentPlayer == 0) {
                        // если промахнулись
                        if (playerFiledB.doShot(x, y) == false) {
                                currentPlayer = 1;
                        }
                }
 
                // если ходит противник
                if (currentPlayer ==1) {
                        while (robot.move());
                        currentPlayer = 0;
                }
                updateSubscribers();
        }
 
        /**
         * регистрация элементов, которые будут обновлять при перерисовки данных на форме
         */
        public void register(ISubscriber o) {
                listeners.add(o);
                o.update();
        }
 
        /**
         * разрегистрация элеметов отображения
         */
        public void unRegister(ISubscriber o) {
                listeners.remove(o);
        }
 
        /**
         * перерисовка данных в представлении
         */
        public void updateSubscribers() {
                Iterator<ISubscriber> i = listeners.iterator();
                while(i.hasNext()) {
                        ISubscriber o = (ISubscriber)i.next();
                        o.update();
                }
        }
 
}
code: #java
package ru.semikov.sea.swing;
 
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
 
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
 
public class GameView extends JFrame {
        private static final long serialVersionUID = 1L;
 
        private GameModel model;
        private GameController controller;
 
        private JMenuItem mntmNewGame;
        private JMenuItem mntmExit;
        private JMenuItem mntmAbout;
        private PanelFieldA panelPlayerA;
        private PanelFieldB panelPlayerB;
        private ScoreField panelScore;
 
        /**
         * Создание представления главной формы
         * @param model - модель главной формы
         */
        public GameView(GameModel model) {
                this.model = model;
                buildUI();
                this.model.register(panelPlayerA);
                this.model.register(panelPlayerB);
                this.model.register(panelScore);
                this.controller=new GameController(this, model);
                attachController();
        }
 
        /**
         * обновляем данные на форме
         */
        public void update() {
                panelPlayerA.repaint();
                panelPlayerB.repaint();
                panelScore.repaint();
                System.out.println("view update");
        }
 
        /**
         * добавление слушателей формы и направляем их на контроллер 
         */
        public void attachController() {
                mntmAbout.addActionListener(controller);
                mntmNewGame.addActionListener(controller);
                mntmExit.addActionListener(controller);
                panelPlayerB.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mousePressed(MouseEvent arg0) {
                                controller.mousePressed(arg0);
                        }
                });
        }
 
        /**
         * построение интерфейса пользователя
         */
        private void buildUI() {
                this.setTitle("SeaBattle");
                this.setResizable(false);
                this.setBounds(400, 300, 483, 228);
                this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                this.getContentPane().setLayout(null);
 
                panelPlayerA = new PanelFieldA(model);
                panelPlayerA.setBounds(20, 31, 151, 151);
                this.getContentPane().add(panelPlayerA);
 
                panelPlayerB = new PanelFieldB(model);
                panelPlayerB.setBounds(190, 31, 151, 151);
                this.getContentPane().add(panelPlayerB);
 
                panelScore = new ScoreField(model);
 
                panelScore.setBounds(370, 31, 90, 151);
                panelScore.setBackground(new Color(225, 225, 255));
                this.getContentPane().add(panelScore);
 
                JMenuBar menuBar = new JMenuBar();
                menuBar.setBounds(0, 0, 477, 21);
                this.getContentPane().add(menuBar);
 
                JMenu mnGame = new JMenu("Game");
                menuBar.add(mnGame);
 
                mntmNewGame = new JMenuItem("New game");
                mnGame.add(mntmNewGame);
 
                JMenuItem mntmProperties = new JMenuItem("Properties");
                mnGame.add(mntmProperties);
 
                mntmExit = new JMenuItem("Exit");
                mnGame.add(mntmExit);
 
                JMenu mnHelp = new JMenu("Help");
                menuBar.add(mnHelp);
 
                mntmAbout = new JMenuItem("About");
                mnHelp.add(mntmAbout);
        }
 
 
}
code: #java
package ru.semikov.sea.swing;
 
import java.awt.Graphics;
import javax.swing.JPanel;
 
abstract public class PanelField extends JPanel implements ISubscriber {
        private static final long serialVersionUID = 1L;
        public GameModel model;
 
        public PanelField(GameModel model) {
                this.model = model;
        }
 
        @Override
        protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // рисуем решётку
                for(int i = 0; i < 11; i++) {
                        g.drawLine(i*15, 0, i*15,150);
                        g.drawLine(0,i*15, 150, i*15);
                }
 
                // рисуем элементы
                for(int j = 0; j < 10; j++) {
                        for(int i = 0; i < 10; i++) {
                                paintElement(g, i, j);
                        }
                }
        }
 
        /**
         * рисование элементов кораблей
         */
        protected void paintElement(Graphics g, int i, int j) {
        }
 
        @Override
        public void update() {
                this.repaint();
        }
 
}
code: #java
package ru.semikov.sea.swing;
 
import java.awt.Color;
import java.awt.Graphics;
import ru.semikov.sea.logic.*;
 
public class PanelFieldA extends PanelField {
        private static final long serialVersionUID = 553977695177508456L;
 
        public PanelFieldA(GameModel model) {
                super(model);
        }
 
        private Color getColorByStateElement(ElementStates state) {
                switch (state) {
                case enBorder:
                        return new Color(215, 215, 255);
                case enWater:
                        return new Color(225, 225, 255);
                case enWell:
                        return Color.green;
                case enInjured:
                        return Color.red;
                case enKilled:
                        return Color.gray;
                case enMissed:
                        return Color.black;
                }
                return Color.blue;
        }
 
 
        @Override
        protected void paintElement(Graphics g, int i, int j) {
                ElementStates state = model.playerFiledA.GetElement(i, j);
                g.setColor(getColorByStateElement(state));
                if (state == ElementStates.enMissed) {
                        g.fillRect(i*15+6, j*15+6, 4, 4);
                } else {
                        g.fillRect(i*15+1, j*15+1, 14, 14);
                }
        }
 
}
code: #java
import java.awt.Color;
import java.awt.Graphics;
import ru.semikov.sea.logic.*;
 
public class PanelFieldB extends PanelField {
        private static final long serialVersionUID = -1770790832677981438L;
 
        public PanelFieldB(GameModel model) {
                super(model);
        }
 
        private Color getColorByStateElement(ElementStates state) {
                switch (state) {
                case enBorder:
                        return new Color(225, 225, 255);
                case enWater:
                        return new Color(225, 225, 255);
                case enWell:
                        return new Color(225, 225, 255);
                case enInjured:
                        return Color.red;
                case enKilled:
                        return Color.gray;
                case enMissed:
                        return Color.black;
                }
 
                return Color.blue;
 
        }
 
        @Override
        protected void paintElement(Graphics g, int i, int j) {
                ElementStates state = model.playerFiledB.GetElement(i, j);
                g.setColor(getColorByStateElement(state));
 
                if (state == ElementStates.enMissed) {
                        g.fillRect(i*15+6, j*15+6, 4, 4);
                } else {
                        g.fillRect(i*15+1, j*15+1, 14, 14);
                }
        }
 
}
code: #java
package ru.semikov.sea.swing;
 
import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;
import ru.semikov.sea.logic.*;
 
public class ScoreField extends JPanel implements ISubscriber {
        private static final long serialVersionUID = 1L;
        private GameModel model;
        private int ships;
 
        public ScoreField(GameModel model) {
                this.model = model;
        }
 
        @Override
        protected void paintComponent(Graphics g) {
                super.paintComponent(g);
 
                int[] m = new int[4];
                for(int i = 0; i < 0; i++) {
                        m[i] = 0;
                }
 
                ships = 0;
                for(Ship ship : model.playerFiledB.ships) {
                        if (ship.state != ShipStates.enKilled) {
                                m[ship.size-1] ++;
                                ships++;
                        }
                }
 
                for(int i = 0; i < 4; i++) {
                        for(int j = 0; j < (i+1); j++) {
                                g.setColor(Color.gray);
                                g.fillRect(j*15+10, i*15+5, 13, 13);
                        }
                        g.setColor(Color.black);
                        g.drawString(String.valueOf(m[i]), 75, i*15+16);
                }
 
                String st = "Alive: ".concat(String.valueOf(ships));
                g.drawString(st, 25, 100);
        }
 
        @Override
        public void update() {
                this.repaint();
        }
 
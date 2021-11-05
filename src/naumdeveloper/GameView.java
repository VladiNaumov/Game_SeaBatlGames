package naumdeveloper;

import javax.swing.*;

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
     *
     * @param model - модель главной формы
     */
    public GameView(GameModel model) {
        this.model = model;
        buildUI();
        this.model.register(panelPlayerA);
        this.model.register(panelPlayerB);
        this.model.register(panelScore);
        this.controller = new GameController(this, model);
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

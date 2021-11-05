package naumdeveloper;

import javax.swing.*;
import java.awt.*;

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
        for (int i = 0; i < 11; i++) {
            g.drawLine(i * 15, 0, i * 15, 150);
            g.drawLine(0, i * 15, 150, i * 15);
        }

        // рисуем элементы
        for (int j = 0; j < 10; j++) {
            for (int i = 0; i < 10; i++) {
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

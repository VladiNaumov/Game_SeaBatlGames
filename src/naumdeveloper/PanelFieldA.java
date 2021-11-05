package naumdeveloper;

import java.awt.*;

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
            g.fillRect(i * 15 + 6, j * 15 + 6, 4, 4);
        } else {
            g.fillRect(i * 15 + 1, j * 15 + 1, 14, 14);
        }
    }

}

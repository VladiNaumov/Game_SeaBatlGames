package naumdeveloper;

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

package naumdeveloper;

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
     *
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
        for (i = 0; i < size; i++) {
            m = y + i * dy;
            n = x + i * dx;
            if (tp.Ship(m, n) == false) {
                return false;
            }
        }
        // площадка сверху и снизу корабля
        for (i = 0; i < size; i++) {
            m = y + i * dy - dx;
            n = x + i * dx - dy;
            if (tp.Border(m, n) == false) {
                return false;
            }
            m = y + i * dy + dx;
            n = x + i * dx + dy;
            if (tp.Border(m, n) == false) {
                return false;
            }
        }
        // площадка слева и справа корабля
        for (i = -1; i < 2; i++) {
            m = y + i * dx - dy;
            n = x + i * dy - dx;
            if (tp.Border(m, n) == false) {
                return false;
            }
            m = y + i * dx + (dy * size);
            n = x + i * dy + (dx * size);
            if (tp.Border(m, n) == false) {
                return false;
            }
        }
        return true;
    }

    /**
     * Проверка корректности нахождения корабля на поле
     *
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

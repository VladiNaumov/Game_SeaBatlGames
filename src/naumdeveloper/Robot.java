package naumdeveloper;

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
     *
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
        first:
        for (int j = 0; j < 10; j++) {
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

                        ArrayList<naumdeveloper.Element> list = new ArrayList<naumdeveloper.Element>();

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
                        naumdeveloper.Element e = list.get(rand.nextInt(list.size()));
                        return field.doShot(e.x, e.y);
                }
*/
        return tryShot();
    }

    /**
     * Получить координуту X
     *
     * @return
     */
    public int getX() {
        return this.x;
    }

    /**
     * Получить координуту Y
     *
     * @return
     */
    public int getY() {
        return this.y;
    }

}

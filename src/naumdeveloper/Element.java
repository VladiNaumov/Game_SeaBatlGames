package naumdeveloper;

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

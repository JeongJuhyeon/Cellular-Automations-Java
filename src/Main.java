public class Main {

    public static void main(String[] args) {
        // write your code here
        brians_brain();
    }

    private static void langtons_ant() {
        Board board = new Board(20, 20);
        while (true) {
            try {
                Thread.sleep(400);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
            board.print();
            board.next_state_langtons_ant();
        }
    }

    private static void life_glider_gun() {
        Board board = new Board("glidergun.txt");
        while (true) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
            board.print();
            board.next_state_life_moore();
        }
    }

    private static void brians_brain(){
        Board board = new Board(200, 200);
        board.center_focused_random_state(0.70);
        // board.brians_oscillator_state();
        while (true) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
            board.print();
            board.next_state_brians_brain();
        }
    }
}

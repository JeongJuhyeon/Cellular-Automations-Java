import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;

public class Board {
    private int width;
    private int height;
    private int board_state[][];
    private int cur_line;
    private Random random;

    private enum Direction {UP, RIGHT, DOWN, LEFT}

    ;
    private LangtonsAnt ant;

    public Board(int height, int width) {
        this.width = width;
        this.height = height;
        this.board_state = new int[height][width];
        this.random = new Random();
        this.ant = new LangtonsAnt();
    }

    public Board(String filename) {
        Path path = Paths.get("");
        this.random = new Random();
        this.ant = new LangtonsAnt();
        cur_line = 0;

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader("C:/Users/jjh/IdeaProjects/Game-of-Life-Java-Implementation/src/com/company/" + filename))) {
            bufferedReader.mark(1000);
            this.height = (int) bufferedReader.lines().count();
            bufferedReader.reset();
            String line = bufferedReader.readLine();

            this.width = line.length();
            this.board_state = new int[height][width];
            process_input_line(line);
            while (null != (line = bufferedReader.readLine())) {
                process_input_line(line);
            }
        } catch (IOException e) {
            System.out.println("File IO error!");
        }
    }

    private void process_input_line(String s) {
        for (int i = 0; i < width; i++) {
            board_state[cur_line][i] = s.charAt(i) - '0';
        }
        cur_line += 1;
    }

    public void dead_state() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                board_state[i][j] = 0;
            }
        }
    }

    public void random_state(double percentage_alive) {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                board_state[i][j] = random.nextInt(100) + 1 <= percentage_alive * 100 ? 1 : 0;
            }
        }
    }

    public void center_focused_random_state(double percentage_alive) {
        for (int i = height / 5 * 1; i < height / 5 * 4; i++) {
            for (int j = width / 5 * 1; j < width / 5 * 4; j++) {
                board_state[i][j] = random.nextInt(100) + 1 <= percentage_alive * 100 ? 1 : 0;
            }
        }
    }

    public void brians_oscillator_state() {
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                board_state[height / 2 + i][width / 2 + j] = 2;
            }
        }
        board_state[height / 2][width / 2 - 1] = 1;
        board_state[height / 2 - 1][width / 2 + 1] = 1;
        board_state[height / 2 + 1][width / 2 + 2] = 1;
        board_state[height / 2 + 2][width / 2] = 1;
    }

    public void next_state_life_moore() {
        int neighbours[][] = new int[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                neighbours[i][j] = calculate_neighbours_moore(i, j);
            }
        }
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (neighbours[i][j] <= 1 || neighbours[i][j] > 3)
                    board_state[i][j] = 0;
                else if (neighbours[i][j] == 3)
                    board_state[i][j] = 1;
            }
        }
    }

    private int calculate_neighbours_moore(int h, int w) {
        int neighbours = 0;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0)
                    continue;
                if (h + i < 0 || h + i > height - 1 || w + j < 0 || w + j > width - 1)
                    continue;

                if (board_state[h + i][w + j] == 1)
                    neighbours += 1;
            }
        }
        return neighbours;
    }

    public void next_state_life_von_neumann() {
        int neighbours[][] = new int[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                neighbours[i][j] = calculate_neighbours_von_neumann(i, j);
            }
        }
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (neighbours[i][j] <= 1 || neighbours[i][j] > 3)
                    board_state[i][j] = 0;
                else if (neighbours[i][j] == 3)
                    board_state[i][j] = 1;
            }
        }
    }

    private int calculate_neighbours_von_neumann(int h, int w) {
        int neighbours = 0;
        for (int i = -2; i <= 2; i++) {
            if (i == 0)
                continue;
            if (h + i >= 0 && h + i <= height - 1)
                neighbours += board_state[h + i][w];
            if (w + i >= 0 && w + i <= width - 1)
                neighbours += board_state[h][w + i];
        }
        return neighbours;
    }

    private class LangtonsAnt {
        public int x;
        public int y;
        public Direction direction;

        public LangtonsAnt() {
            x = width / 2;
            y = height / 2;
            direction = Direction.LEFT;
        }

        public void move_forward() {
            if (direction == Direction.LEFT)
                x -= 1;
            else if (direction == Direction.UP)
                y -= 1;
            else if (direction == Direction.RIGHT)
                x += 1;
            else
                y += 1;
        }
    }

    public void next_state_langtons_ant() {
        if (board_state[ant.y][ant.x] == 0) {
            ant.direction = Direction.values()[(ant.direction.ordinal() + 1) % 4];
            board_state[ant.y][ant.x] = 1;
        } else if (board_state[ant.y][ant.x] == 1) {
            ant.direction = Direction.values()[((ant.direction.ordinal() - 1) % 4 + 4) % 4];
            board_state[ant.y][ant.x] = 0;
        }
        ant.move_forward();
    }

    public void next_state_brians_brain(){
        int neighbours[][] = new int[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                neighbours[i][j] = calculate_neighbours_moore(i, j);
            }
        }

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (board_state[i][j] == 0 && neighbours[i][j] == 2)
                    board_state[i][j] = 1;
                else if (board_state[i][j] == 1)
                    board_state[i][j] = 2;
                else if (board_state[i][j] == 2)
                    board_state[i][j] = 0;

            }
        }

        die_on_border();
    }


    private void die_on_border(){
        for (int i = 0; i < height; i++) {
            board_state[i][0] = 0;
            board_state[i][width - 1] = 0;
        }
        for (int i = 0; i < width; i++) {
            board_state[0][i] = 0;
            board_state[height - 1][i] = 0;
        }
    }

    public void print() {
        for (int i = 0; i < width + 2; i++) {
            System.out.print("-");
        }
        System.out.println();

        for (int i = 0; i < height; i++) {
            System.out.print("|");
            for (int j = 0; j < width; j++) {
                if (board_state[i][j] == 1) // On
                    System.out.print("#");
                else if (board_state[i][j] == 0) // Off
                    System.out.print(" ");
                else if (board_state[i][j] == 2)  // Dying
                    System.out.print("-");
            }
            System.out.println("|");
        }

        for (int i = 0; i < width + 2; i++) {
            System.out.print("-");
        }
        System.out.println();
    }

}

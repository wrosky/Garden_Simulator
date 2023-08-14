import java.util.Scanner;

public class Game {

    private static int rows;
    private static int cols;
    private static int gardenerAmount;
    private static int plantAmount;
    private static int pestAmount;
    private static int plantHp;
    private static Board board;

    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);

            System.out.print("Amount of Rows: ");
            rows = Integer.parseInt(scanner.next());

            System.out.print("Amount of Cols: ");
            cols = Integer.parseInt(scanner.next());

            System.out.print("Amount of Gardeners: ");
            gardenerAmount = Integer.parseInt(scanner.next());

            System.out.print("Amount of Plants: ");
            plantAmount = Integer.parseInt(scanner.next());

            System.out.print("Amount of Pests: ");
            pestAmount = Integer.parseInt(scanner.next());

            System.out.print("Plant HP: ");
            plantHp = Integer.parseInt(scanner.next());

            /* Dla debugowania
            rows = 10;
            cols = 10;
            plantAmount = 5;
            pestAmount = 5;
            plantHp = 3;*/

            if (plantAmount + pestAmount + gardenerAmount > rows * cols) { // Warunek dla, którego można daną ilość obiektów dać do programu
                System.out.println("More Entities than Tiles. Program is shutting down.");
                System.exit(-1);
            }

            board = new Board(rows, cols);

            spawnPlants();
            spawnGardener();
            spawnPests();

            while (board.isVisible()) {

                Thread.sleep(500);

                board.round();
            }

            board.dispose();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    private static void spawnPlants() { // Spawnowanie postaci
        for (int i = 0; i < plantAmount; i++) {
            int row = getRandomRow();
            int col = getRandomCol();

            while (!board.spawn(row, col, new Plant(plantHp))) {
                row = getRandomRow();
                col = getRandomCol();
            }
        }
    }

    private static void spawnGardener() { // Spawnowanie postaci
        for (int i = 0; i < gardenerAmount; i++) {

            int row = getRandomRow();
            int col = getRandomCol();

            while (!board.spawn(row, col, new Gardener())) {
                row = getRandomRow();
                col = getRandomCol();
            }
        }
    }

    private static void spawnPests() { // Spawnowanie postaci
        for (int i = 0; i < pestAmount; i++) {
            int row = getRandomRow();
            int col = getRandomCol();

            while (!board.spawn(row, col, getRandomPest())) {
                row = getRandomRow();
                col = getRandomCol();
            }
        }
    }

    // Losowanie zmiennych kolumny i wiersza
    private static int getRandomRow() {
        return (int) (Math.random()*(rows - 1));
    }

    private static int getRandomCol() {
        return (int) (Math.random()*(cols - 1));
    }

    // Losowanie szkodnika
    private static Pest getRandomPest() {
        int randomNumber = (int) (Math.random()*(4 - 1) + 1);

        if (randomNumber == 1) {
            return new Worm();
        } else {
            return new Mouse();
        }
    }
}
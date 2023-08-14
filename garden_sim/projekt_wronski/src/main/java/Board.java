import javax.swing.*;
import java.awt.*;

public class Board extends JFrame {

    public JPanel panel;
    public Tile[][] tileArray;
    private final int rows;
    private final int cols;

    public Board(int rows, int cols) { // Funkcja, która tworzy plansze
        this.rows = rows;
        this.cols = cols;

        panel = new JPanel();
        panel.setSize(new Dimension(500, 500));
        tileArray = new Tile[rows][cols];

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                Tile tile = new Tile();
                panel.add(tile);
                tileArray[row][col] = tile;
            }
        }

        // Ustawienia dot. planszy
        setContentPane(panel);
        setLayout(new GridLayout(rows, cols));
        panel.setLayout(new GridLayout(rows, cols));
        setSize(new Dimension(cols*60, rows*60));
        setLocationRelativeTo(null);
        setTitle("Garden Game");
        setResizable(false);
        setVisible(true);
    }

    public void round() {
        Tile[][] newTileArray = new Tile[rows][cols];

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                Tile tile = tileArray[row][col];

                if (tile.getEntity() != null && tile.getEntity() instanceof Plant) { // Ustawianie pola dla rośliny
                    prepareTile(newTileArray, row, col, tile.getEntity());
                }
            }
        }

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                Tile tile = tileArray[row][col];

                if (tile.getEntity() != null && tile.getEntity() instanceof Pest) { // Ustawianie pola dla szkodnika
                    String direction = getRandomDirection();

                    int newCol = col;
                    int newRow = row;

                    switch (direction) {
                        case "left" -> newCol = col - 1;
                        case "right" -> newCol = col + 1;
                        case "up" -> newRow = row - 1;
                        case "down" -> newRow = row + 1;
                    }

                    if (isValidPositioning(newTileArray, newRow, newCol)) {
                        prepareTile(newTileArray, newRow, newCol, tile.getEntity());
                    } else {
                        prepareTile(newTileArray, row, col, tile.getEntity());
                    }
                }
            }
        }

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                Tile tile = tileArray[row][col];

                if (tile.getEntity() != null && tile.getEntity() instanceof Gardener) { // Ustawianie pola dla ogrodnika
                    String direction = getRandomDirection();

                    int newCol = col;
                    int newRow = row;

                    switch (direction) {
                        case "left" -> newCol = col - 1;
                        case "right" -> newCol = col + 1;
                        case "up" -> newRow = row - 1;
                        case "down" -> newRow = row + 1;
                    }

                    if (isValidPositioning(newTileArray, newRow, newCol)) {
                        prepareTile(newTileArray, newRow, newCol, tile.getEntity());
                    } else {
                        prepareTile(newTileArray, row, col, tile.getEntity());
                    }
                }
            }
        }

        for (int row = 0; row < rows; row++) { // Ustawienie pustych pól
            for (int col = 0; col < cols; col++) {
                if (newTileArray[row][col] == null) {
                    newTileArray[row][col] = new Tile();
                }
            }
        }

        tileArray = newTileArray;
        panel = new JPanel();

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                panel.add(tileArray[row][col]);
            }
        }

        setContentPane(panel);

        update();
        waitShort();
        checkAfterDraw();
        update();
    }

    private boolean isValidPositioning(Tile[][]newTileArray, int row, int col) {
        if (col < 0 || col >= cols || row < 0 || row >= rows) { // Granice planszy
            return false;
        }
        if (tileArray[row][col].getEntity() != null) { // Zapobieganie przeciwko przechodzenia przez przedmiot
            return false;
        }
        // Uniemożliwienie ingerencji po turze
        return newTileArray[row][col] == null || newTileArray[row][col].getEntity() == null;
    }

    private void prepareTile(Tile[][] tileArray, int row, int col, Entity entity) {
        Tile newTile = new Tile();
        newTile.setEntity(entity);
        tileArray[row][col] = newTile;
    }

    private String getRandomDirection() { // Losowanie kierunku ruchu
        int randomNumber = (int) (Math.random()*(5 - 1) + 1);

        return switch (randomNumber) {
            case 1 -> "left";
            case 2 -> "right";
            case 3 -> "up";
            case 4 -> "down";
            default -> "none";
        };

    }

    public boolean spawn(int row, int col, Entity entity) { // Sprawdzanie miejsca do spawnowania obiektów
        Tile tile = tileArray[row][col];
        if (tile.getEntity() != null) {
            return false;
        }
        tile.setEntity(entity);
        return true;
    }

    public void update() { // Wprowadzenie następnej rundy
        for (Tile[] tileArrayDimension : tileArray) {
            for (Tile tile : tileArrayDimension) {
                tile.update();
            }
        }
        panel.revalidate();
        panel.repaint();

        revalidate();
        repaint();
    }

    public void waitShort() {
        try {
            Thread.sleep(750);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void checkAfterDraw() { // Warunki dla obiektów, gdy zachodzi interakcja
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                Tile tile = tileArray[row][col];
                Entity tileEntity = tile.getEntity();
                if (tileEntity == null) {
                    continue;
                }

                if (tileEntity instanceof Plant) {
                    boolean hasGardener = false;

                    // Przypadki interakcji rośliny z ogrodnikiem
                    if (row != 0 && tileArray[row-1][col].getEntity() instanceof Gardener) {
                        hasGardener = true;
                        ((Plant) tileEntity).heal();
                    }
                    if (row != rows-1 && tileArray[row+1][col].getEntity() instanceof Gardener) {
                        hasGardener = true;
                        ((Plant) tileEntity).heal();
                    }
                    if (col != 0 && tileArray[row][col-1].getEntity() instanceof Gardener) {
                        hasGardener = true;
                        ((Plant) tileEntity).heal();
                    }
                    if (col != cols-1 && tileArray[row][col+1].getEntity() instanceof Gardener) {
                        hasGardener = true;
                        ((Plant) tileEntity).heal();
                    }

                    Tile neighborTile;

                    // Przypadki interakcji rośliny ze szkodnikiem
                    if (row != 0 && (neighborTile = tileArray[row-1][col]).getEntity() instanceof Pest) {
                        if (hasGardener) {
                            neighborTile.setEntity(null);
                        } else {
                            ((Plant) tileEntity).damage();
                        }
                    }
                    if (row != rows-1 && (neighborTile = tileArray[row+1][col]).getEntity() instanceof Pest) {
                        if (hasGardener) {
                            neighborTile.setEntity(null);
                        } else {
                            ((Plant) tileEntity).damage();
                        }
                    }
                    if (col != 0 && (neighborTile = tileArray[row][col-1]).getEntity() instanceof Pest) {
                        if (hasGardener) {
                            neighborTile.setEntity(null);
                        } else {
                            ((Plant) tileEntity).damage();
                        }
                    }
                    if (col != cols-1 && (neighborTile = tileArray[row][col+1]).getEntity() instanceof Pest) {
                        if (hasGardener) {
                            neighborTile.setEntity(null);
                        } else {
                            ((Plant) tileEntity).damage();
                        }
                    }

                    // Jeśli roślina będzie miała 0 hp to umiera
                    if (((Plant) tileEntity).getHp() == 0) {
                        tile.setEntity(null);
                    }
                    if(((Plant) tileEntity).getHp() == -1){
                        tile.setEntity(null);
                    }
                }

                if (tileEntity instanceof Gardener) {
                    Tile neighborTile;

                    // Przypadki interakcji ogrodnika ze szkodnikiem
                    if (row != 0 && (neighborTile = tileArray[row-1][col]).getEntity() instanceof Pest) {
                        neighborTile.setEntity(null);
                    }
                    if (row != rows-1 && (neighborTile = tileArray[row+1][col]).getEntity() instanceof Pest) {
                        neighborTile.setEntity(null);
                    }
                    if (col != 0 && (neighborTile = tileArray[row][col-1]).getEntity() instanceof Pest) {
                        neighborTile.setEntity(null);
                    }
                    if (col != cols-1 && (neighborTile = tileArray[row][col+1]).getEntity() instanceof Pest) {
                        neighborTile.setEntity(null);
                    }
                }
            }
        }
    }
}
import java.util.*;

public class Board {
    private final int size; // dimension of the board
    private final Cell[][] grid;
    private final Map<String, ColorRegion> regions; // Key: RGB color string
    private final List<Cell> queens; // Track all placed queens

    public Board(int size) {
        this.size = size;
        this.grid = new Cell[size][size];
        this.regions = new HashMap<>();
        this.queens = new ArrayList<>();
    }

    public void setCell(int row, int col, String color) {
        Cell cell = new Cell(row, col, color);
        grid[row][col] = cell;

        // Add cell to its color region
        regions.computeIfAbsent(color, ColorRegion::new).addCell(cell);
    }

    public Cell getCell(int row, int col) {
        return grid[row][col];
    }

    public int getSize() {
        return size;
    }

    public Collection<ColorRegion> getRegions() {
        return regions.values();
    }

    public ColorRegion getRegion(String color) {
        return regions.get(color);
    }

    public List<Cell> getRow(int row) {
        return Arrays.asList(grid[row]);
    }

    public List<Cell> getColumn(int col) {
        List<Cell> column = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            column.add(grid[i][col]);
        }
        return column;
    }

    public List<Cell> getQueens() {
        return Collections.unmodifiableList(queens);
    }

    public boolean placeQueen(Cell cell) {
        if (isValidPlacement(cell)) {
            cell.setState(CellState.QUEEN);
            queens.add(cell);

            // Update the region's queen placement
            String color = cell.getColor();
            regions.get(color).setQueenPlacement(cell);

            return true;
        }
        return false;
    }

    public void removeQueen(Cell cell) {
        cell.setState(CellState.EMPTY);
        queens.remove(cell);

        // Clear the region's queen placement
        String color = cell.getColor();
        regions.get(color).setQueenPlacement(null);
    }

    private boolean isValidPlacement(Cell cell) {
        // Check if there's already a queen in the same row
        for (Cell c : getRow(cell.getRow())) {
            if (c.getState() == CellState.QUEEN){
                return false;
            }
        }

        // Check if there's already a queen in the same column
        for (Cell c : getColumn(cell.getCol())) {
            if (c.getState() == CellState.QUEEN){
                return false;
            }
        }

        // Check if there's already a queen in the same color region
        ColorRegion region = regions.get(cell.getColor());
        if (region.hasQueen()) {
            return false;
        }

        // Check if any existing queen is adjacent
        for (Cell queen : queens) {
            if (cell.isAdjacent(queen)) {
                return false;
            }
        }

        return true;
    }

    public boolean isSolved() {
        // Check if all regions have exactly one queen
        for (ColorRegion region : regions.values()) {
            if (!region.hasQueen()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Board board = (Board) o;
        return size == board.size && Objects.deepEquals(grid, board.grid) && Objects.equals(regions, board.regions) && Objects.equals(queens, board.queens);
    }

    @Override
    public int hashCode() {
        return Objects.hash(size, Arrays.deepHashCode(grid), regions, queens);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                Cell cell = grid[i][j];
                switch (cell.getState()) {
                    case QUEEN -> sb.append("Q ");
                    case BLOCKED -> sb.append("X ");
                    default -> sb.append(". ");
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}

import java.util.Objects;

public class Cell {
    private final int row;
    private final int col;
    private final String color;
    private CellState state;

    public Cell(int row, int col, String color) {
        this.row = row;
        this.col = col;
        this.color = color;
        this.state = CellState.EMPTY;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public String getColor() {
        return color;
    }

    public CellState getState() {
        return state;
    }

    public void setState(CellState state) {
        this.state = state;
    }

    public boolean isAdjacent(Cell other) {
        int rowDiff = Math.abs(this.row - other.row);
        int colDiff = Math.abs(this.col - other.col);
        // Adjacent includes all 8 surrounding cells
        return rowDiff <= 1 && colDiff <= 1 && (rowDiff + colDiff > 0);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cell cell = (Cell) o;
        return row == cell.row && col == cell.col;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }

    @Override
    public String toString() {
        return "(" + row + "," + col + "," + color + ")";
    }
}

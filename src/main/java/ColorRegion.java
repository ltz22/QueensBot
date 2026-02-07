import java.util.*;

public class ColorRegion {
    private final String color;
    private final List<Cell> cells;
    private Cell queenPlacement; // null if no queen placed yet

    public ColorRegion(String color) {
        this.color = color;
        this.cells = new ArrayList<>();
        this.queenPlacement = null;
    }

    public void addCell(Cell cell) {
        cells.add(cell);
    }

    public String getColor() {
        return color;
    }

    public List<Cell> getCells() {
        return Collections.unmodifiableList(cells);
    }

    public List<Cell> getAvailableCells() {
        return cells.stream()
                .filter(cell -> cell.getState() == CellState.EMPTY)
                .toList();
    }

    public boolean hasQueen() {
        return queenPlacement != null;
    }

    public Cell getQueenPlacement() {
        return queenPlacement;
    }

    public void setQueenPlacement(Cell cell) {
        this.queenPlacement = cell;
    }

    public int size() {
        return cells.size();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ColorRegion that = (ColorRegion) o;
        return Objects.equals(color, that.color) && Objects.equals(cells, that.cells) && Objects.equals(queenPlacement, that.queenPlacement);
    }

    @Override
    public int hashCode() {
        return Objects.hash(color, cells, queenPlacement);
    }

    @Override
    public String toString() {
        return "Region[" + color + ", " + cells.size() + " cells]";
    }
}

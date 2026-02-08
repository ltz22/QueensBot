import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solution;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.solver.variables.BoolVar;

import java.util.*;

public class QueensSolver {
    private final Board board;
    private Model model;
    private Map<String, IntVar> regionQueenPosition; // Maps color -> position index in region
    private Map<String, List<Cell>> regionCellsList; // Maps color -> ordered list of cells

    public QueensSolver(Board board) {
        this.board = board;
        this.model = new Model();
        this.regionQueenPosition = new HashMap<>();
        this.regionCellsList = new HashMap<>();
    }

    public Solution solve() {
        setupVariables();
        setupConstraints();

        System.out.println("Solving...");
        Solution solution = model.getSolver().findSolution();

        if (solution != null) {
            System.out.println("Solution found!");
            applySolution(solution);
            return solution;
        } else {
            System.out.println("No solution found!");
            return null;
        }
    }

    private void setupVariables() {
        // For each color region, create a variable representing which cell in that region has the queen
        for (ColorRegion region : board.getRegions()) {
            String color = region.getColor();
            List<Cell> cells = new ArrayList<>(region.getCells());
            regionCellsList.put(color, cells);

            // Variable domain is [0, region.size()-1]
            IntVar queenPos = model.intVar("queen_" + color, 0, cells.size() - 1);
            regionQueenPosition.put(color, queenPos);
        }
    }

    private void setupConstraints() {
        List<ColorRegion> regions = new ArrayList<>(board.getRegions());

        // For each pair of regions, add constraints
        for (int i = 0; i < regions.size(); i++) {
            for (int j = i + 1; j < regions.size(); j++) {
                ColorRegion region1 = regions.get(i);
                ColorRegion region2 = regions.get(j);

                addPairwiseConstraints(region1, region2);
            }
        }
    }

    private void addPairwiseConstraints(ColorRegion region1, ColorRegion region2) {
        String color1 = region1.getColor();
        String color2 = region2.getColor();

        IntVar pos1 = regionQueenPosition.get(color1);
        IntVar pos2 = regionQueenPosition.get(color2);

        List<Cell> cells1 = regionCellsList.get(color1);
        List<Cell> cells2 = regionCellsList.get(color2);

        // For each combination of cells from the two regions,
        // if they conflict, we must exclude that combination
        for (int i = 0; i < cells1.size(); i++) {
            for (int j = 0; j < cells2.size(); j++) {
                Cell cell1 = cells1.get(i);
                Cell cell2 = cells2.get(j);

                if (conflictExists(cell1, cell2)) {
                    // If pos1 == i, then pos2 cannot be j
                    // Equivalently: NOT (pos1 == i AND pos2 == j)
                    BoolVar pos1EqualsI = model.intEqView(pos1, i);
                    BoolVar pos2EqualsJ = model.intEqView(pos2, j);
                    BoolVar bothTrue = model.and(pos1EqualsI, pos2EqualsJ).reify();
                    model.arithm(bothTrue, "=", 0).post(); // Must be false
                }
            }
        }
    }

    private boolean conflictExists(Cell cell1, Cell cell2) {
        // Check if two cells conflict (same row, same column, or adjacent)
        // Same row
        if (cell1.getRow() == cell2.getRow()) {
            return true;
        }
        // Same column
        if (cell1.getCol() == cell2.getCol()) {
            return true;
        }
        // Adjacent
        if (cell1.isAdjacent(cell2)) {
            return true;
        }
        return false;
    }

    private void applySolution(Solution solution) {
        // Apply the solution to the board
        for (ColorRegion region : board.getRegions()) {
            String color = region.getColor();
            IntVar posVar = regionQueenPosition.get(color);
            int position = solution.getIntVal(posVar);

            List<Cell> cells = regionCellsList.get(color);
            Cell queenCell = cells.get(position);

            board.placeQueen(queenCell);
            //System.out.println("Queen placed at (" + queenCell.getRow() + ", " + queenCell.getCol() + ") in region " + color);
        }

        System.out.println("\nFinal board:");
        System.out.println(board);
    }

    public Map<Cell, Boolean> getSolutionMap() {
        // Returns a map of Cell -> true (if queen should be placed)
        Map<Cell, Boolean> solutionMap = new HashMap<>();

        for (ColorRegion region : board.getRegions()) {
            if (region.hasQueen()) {
                solutionMap.put(region.getQueenPlacement(), true);
            }
        }

        return solutionMap;
    }
}

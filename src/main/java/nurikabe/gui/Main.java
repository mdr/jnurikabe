package nurikabe.gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import nurikabe.grid.AutoSolver;
import nurikabe.grid.CellType;
import nurikabe.grid.NurikabeGrid;
import nurikabe.reasoning.Conclusion;
import nurikabe.reasoning.rationale.Rationale;

public class Main implements NurikabeGridCellMousePressedListener {

    
    // Solved: 1, 3, 5, 7, 8, 9, 10, 11, 12, 13, 14
    // Unsolved: 2, 4 (multiple solutions possible), 6
    public static final int NUMBER = 14;
    

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                final JFrame frame = new JFrame("NurikabeGridView test");
                frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.PAGE_AXIS));
                frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                
                final NurikabeGridView nurikabeGridView = makeNurikabeGridView();
                frame.add(nurikabeGridView);


                JButton button = new JButton("Solve a cell");
                button.setAlignmentX(0.5f);
                frame.add(button);

                final JTextPane textPane = new JTextPane();
                textPane.setPreferredSize(new Dimension(300, 200));
                textPane.setEditable(false);
                JScrollPane scrollPane = new JScrollPane(textPane);
                frame.add(scrollPane);

                final AutoSolver autoSolver = AutoSolver.makeStandardSolver();
                button.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        NurikabeGrid startGrid = nurikabeGridView.getGrid();
                        Map<Conclusion, Rationale> conclusions = autoSolver.getFirstConclusion(startGrid);
                        if (startGrid.isComplete()) {
                            if (startGrid.isCompleteAndCorrect()) {
                                textPane.setText("Finished!");
                            } else {
                                textPane.setText("Incorrect grid");
                            }
                            return;
                        }
                        if (conclusions.size() == 0) {
                            textPane.setText("Stumped!");
                            return;
                        }
                        Conclusion conclusion = conclusions.keySet().iterator().next();
                        Rationale rationale = conclusions.get(conclusion);
                        if (conclusion != null) {
                            NurikabeGrid newGrid = conclusion.applyTo(startGrid);
                            nurikabeGridView.setGrid(newGrid);
                            textPane.setText(conclusion + " because " + rationale.getDescription());
                        }

                    }

                });
                frame.pack();
                frame.setVisible(true);
            }

            private NurikabeGridView makeNurikabeGridView() {
                NurikabeGrid grid = getSampleGrid(NUMBER);
                final NurikabeGridView nurikabeGridView = new NurikabeGridView(grid);
                nurikabeGridView.setOpaque(true);
                nurikabeGridView.setCellSelectionHighlightingEnabled(true);
                nurikabeGridView.setDrawCoordinateAxes(true);
                nurikabeGridView.addNurikabeGridCellMousePressedListener(new Main());
                return nurikabeGridView;
            }

        });
    }

    public void mousePressed(NurikabeGridCellMousePressedEvent event) {
        NurikabeGrid grid = event.getGrid();
        int column = event.getColumn();
        int row = event.getRow();
        if (grid.cellAt(row, column).isIslandNumber())
            return;
        MouseEvent mouseEvent = event.getMouseEvent();
        NurikabeGrid newGrid;
        if (grid.getCellType(row, column) == CellType.UNDETERMINED) {
            if (SwingUtilities.isLeftMouseButton(mouseEvent))
                newGrid = grid.concludeCellIsLand(row, column);
            else
                newGrid = grid.concludeCellIsWater(row, column);
        } else if (grid.getCellType(row, column) == CellType.LAND) {
            if (SwingUtilities.isLeftMouseButton(mouseEvent))
                newGrid = grid.concludeCellIsWater(row, column);
            else
                newGrid = grid.removeConclusionFromCell(row, column);
        } else {
            if (SwingUtilities.isLeftMouseButton(mouseEvent))
                newGrid = grid.removeConclusionFromCell(row, column);
            else
                newGrid = grid.concludeCellIsLand(row, column);
        }
        NurikabeGridView gridView = (NurikabeGridView) event.getSource();
        gridView.setGrid(newGrid);

//        gridView.clearHighlightingLayers();
//        HighlightingLayer layer1 = new NurikabeGridView.HighlightingLayer(newGrid.cellAt(2, 8).getPolyomino()
//                .getCells(), Color.RED);
//        HighlightingLayer layer2 = new NurikabeGridView.HighlightingLayer(newGrid.cellAt(7, 3).getPolyomino()
//                .getCells(), Color.BLUE);
//        HighlightingLayer layer3 = new NurikabeGridView.HighlightingLayer(newGrid.cellAt(2, 3).getPolyomino()
//                .getCells(), Color.WHITE);
//        gridView.addHighlightingLayer(layer1);
//        gridView.addHighlightingLayer(layer2);
//        gridView.addHighlightingLayer(layer3);

    }

    private static NurikabeGrid getSampleGrid(int number) {
        StringBuilder builder;

        builder = new StringBuilder();
        switch (number) {
            case 0:

                // ............ 0123456
                builder.append("#3..#1#:"); // 0
                builder.append("### ## :"); // 1
                builder.append("2.#1#..:"); // 2
                builder.append("## ###.:"); // 3
                builder.append("#1#.2#.:"); // 4
                builder.append("##2### :"); // 5
                builder.append("1#.#1#6:"); // 6
                break;
            case 1:
                builder.append(" 3   1 :"); // 0
                builder.append("       :"); // 1
                builder.append("2  1   :"); // 2
                builder.append("       :"); // 3
                builder.append(" 1  2  :"); // 4
                builder.append("  2    :"); // 5
                builder.append("1   1 6:"); // 6
                break;
            case 2:
                builder.append("2        2:");
                builder.append("      2   :");
                builder.append(" 2  7     :");
                builder.append("          :");
                builder.append("      3 3 :");
                builder.append("  2    3  :");
                builder.append("2  4      :");
                builder.append("          :");
                builder.append(" 1    2 4 :");
                break;

            case 3:
                builder.append("    2:");
                builder.append("     :");
                builder.append("7   2:");
                builder.append("     :");
                builder.append("3    :");
                break;

            case 4:
                // ............ 0123456789
                builder.append("  2 1  2  :"); // 0
                builder.append("     1   1:"); // 1
                builder.append("1 3       :"); // 2
                builder.append("        3 :"); // 3
                builder.append("  2 7    1:"); // 4
                builder.append("2         :"); // 5
                builder.append("    2    1:"); // 6
                builder.append(" 3        :"); // 7
                builder.append("   1 4   1:"); // 8
                builder.append("          :"); // 9
                break;

            case 5:
                // ............ 0123456789
                builder.append("4    3 4 :"); // 0
                builder.append("         :"); // 1
                builder.append("         :"); // 2
                builder.append("5        :"); // 3
                builder.append(" 1     7 :"); // 4
                builder.append("        2:"); // 5
                builder.append("         :"); // 6
                builder.append("         :"); // 7
                builder.append(" 6 4    1:"); // 8
                break;

            case 6:
                // ............ 012345678
                builder.append("  3      :"); // 0
                builder.append(" 4      7:"); // 1
                builder.append("         :"); // 2
                builder.append("        4:"); // 3
                builder.append("         :"); // 4
                builder.append("2        :"); // 5
                builder.append("         :"); // 6
                builder.append("4      2 :"); // 7
                builder.append("      7  :"); // 8
                break;
            case 7:
                // ............ 012345678
                builder.append("    3    :"); // 0
                builder.append(" 6 4     :"); // 1
                builder.append("         :"); // 2
                builder.append("         :"); // 3
                builder.append("    5    :"); // 4
                builder.append("         :"); // 5
                builder.append("         :"); // 6
                builder.append("     2 7 :"); // 7
                builder.append("    4    :"); // 8
                break;
            case 8:
                // ............ 012345678
                builder.append("       8 :"); // 0
                builder.append("      4  :"); // 1
                builder.append("3        :"); // 2
                builder.append("     3   :"); // 3
                builder.append("         :"); // 4
                builder.append("   3     :"); // 5
                builder.append("        3:"); // 6
                builder.append("  6      :"); // 7
                builder.append(" 6       :"); // 8
                break;

            case 9:
                // ............ 012345678
                builder.append("  5    3 :"); // 0
                builder.append("   3     :"); // 1
                builder.append("  1      :"); // 2
                builder.append("       2 :"); // 3
                builder.append("         :"); // 4
                builder.append(" 7       :"); // 5
                builder.append("      1  :"); // 6
                builder.append("     4   :"); // 7
                builder.append(" 5    5  :"); // 8
                break;
            case 10:
                // ............ 012345678
                builder.append("5        :"); // 0
                builder.append("      6  :"); // 1
                builder.append("2        :"); // 2
                builder.append("      1  :"); // 3
                builder.append("7       4:"); // 4
                builder.append("  6      :"); // 5
                builder.append("        1:"); // 6
                builder.append("  3      :"); // 7
                builder.append("        3:"); // 8
                break;
            case 11:
                // ............ 012345678
                builder.append("     2   :"); // 0
                builder.append("         :"); // 1
                builder.append("   4     :"); // 2
                builder.append("1 6      :"); // 3
                builder.append("         :"); // 4
                builder.append("      3 6:"); // 5
                builder.append("     6   :"); // 6
                builder.append("         :"); // 7
                builder.append("   6     :"); // 8
                break;
            case 12:
                // ............ 012345678
                builder.append("    4    :"); // 0
                builder.append("         :"); // 1
                builder.append(" 2 5  2  :"); // 2
                builder.append("5        :"); // 3
                builder.append("         :"); // 4
                builder.append("        4:"); // 5
                builder.append("  2  1 3 :"); // 6
                builder.append("         :"); // 7
                builder.append("    5    :"); // 8
                break;

            case 13:
                // ............ 012345678
                builder.append("     4   :"); // 0
                builder.append("   3     :"); // 1
                builder.append("         :"); // 2
                builder.append("3        :"); // 3
                builder.append("   3 6   :"); // 4
                builder.append("        6:"); // 5
                builder.append("         :"); // 6
                builder.append("     3   :"); // 7
                builder.append("   4     :"); // 8
                break;

            case 14:
                // ............ 012345678
                builder.append("         :"); // 0
                builder.append("         :"); // 1
                builder.append("      5 2:"); // 2
                builder.append("     7   :"); // 3
                builder.append(" 4     2 :"); // 4
                builder.append("   3     :"); // 5
                builder.append("5 4      :"); // 6
                builder.append("         :"); // 7
                builder.append("         :"); // 8
                break;
                
            case 15:
                // ............ 012345678
                builder.append("         :"); // 0
                builder.append("         :"); // 1
                builder.append("         :"); // 2
                builder.append("         :"); // 3
                builder.append("         :"); // 4
                builder.append("         :"); // 5
                builder.append("         :"); // 6
                builder.append("         :"); // 7
                builder.append("         :"); // 8
                break;

        }
        return NurikabeGrid.createFromAsciiGrid(builder.toString());
    }

}

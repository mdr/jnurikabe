package nurikabe.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.swing.JComponent;
import javax.swing.event.EventListenerList;

import nurikabe.grid.Cell;
import nurikabe.grid.CellType;
import nurikabe.grid.NurikabeGrid;
import utils.GuiUtils;

public class NurikabeGridView extends JComponent implements MouseListener, MouseMotionListener {

    private NurikabeGrid grid;

    private boolean cellSelectionHighlightingEnabled = false;

    private boolean drawCoordinateAxes = false;

    private EventListenerList cellMousePressListenerList = new EventListenerList();

    private List<HighlightingLayer> highlightingLayers = new ArrayList<HighlightingLayer>();
    
    private HashMap<Integer, BufferedImage> oceanTiles;

    private HashMap<Integer, BufferedImage> landTiles;

    private int squareSize;

    private int numberOfColumns;

    private int numberOfRows;

    private int selectedRow;

    private int selectedColumn;

    private boolean mouseIsOverCell;

    // Constants:

    private static final Color TRANSPARENT_BLACK = new Color(0.0f, 0.0f, 0.0f, 0.0f);

    private static final BasicStroke THICK_STROKE = new BasicStroke(2.0f);

    private static final Font NUMBER_CELL_FONT = new Font("SansSerif", Font.PLAIN, 30);

    public NurikabeGridView(NurikabeGrid grid) {
        this.grid = grid;
        this.numberOfColumns = grid.numberOfColumns();
        this.numberOfRows = grid.numberOfRows();
        SpriteSheet spriteSheet = SpriteSheet.getInstance();
        squareSize = SpriteSheet.getSquareSize();
        landTiles = new HashMap<Integer, BufferedImage>();
        for (int i = 0; i < 16; i++)
            landTiles.put(i, spriteSheet.getSprite(1, i));

        oceanTiles = new HashMap<Integer, BufferedImage>();
        for (int i = 0; i < 16; i++)
            oceanTiles.put(i, spriteSheet.getSprite(9, i)); // 8, 15 - i
        this.addMouseListener(this);
        this.addMouseMotionListener(this);

    }

    private int tileNumber(boolean north, boolean east, boolean south, boolean west) {
        int number = 0;
        number += north ? 0 : 1;
        number += east ? 0 : 2;
        number += south ? 0 : 4;
        number += west ? 0 : 8;
        return number;
    }

    private BufferedImage getLandTile(int row, int column) {
        boolean north = false;
        boolean east = false;
        boolean south = false;
        boolean west = false;

        if (row > 0 && grid.cellAt(row - 1, column).isLand())
            north = true;
        if (row < numberOfRows - 1 && grid.cellAt(row + 1, column).isLand())
            south = true;
        if (column > 0 && grid.cellAt(row, column - 1).isLand())
            west = true;
        if (column < numberOfColumns - 1 && grid.cellAt(row, column + 1).isLand())
            east = true;

        return landTiles.get(tileNumber(north, east, south, west));
    }

    private BufferedImage getOceanTile(int row, int column) {
        boolean north = true;
        boolean east = true;
        boolean south = true;
        boolean west = true;

        if (row > 0 && grid.cellAt(row - 1, column).isLand())
            north = false;
        if (row < numberOfRows - 1 && grid.cellAt(row + 1, column).isLand())
            south = false;
        if (column > 0 && grid.cellAt(row, column - 1).isLand())
            west = false;
        if (column < numberOfColumns - 1 && grid.cellAt(row, column + 1).isLand())
            east = false;

        return oceanTiles.get(tileNumber(north, east, south, west));
    }

    private void paintBackgroundIfRequired(Graphics2D graphics) {
        if (isOpaque()) {
            graphics.setColor(Color.BLACK);
            graphics.fill(graphics.getClipBounds());
        }
    }

    private void drawSelectedCellMarker(Graphics2D graphics) {
        graphics.setColor(Color.YELLOW);
        Stroke previousStroke = graphics.getStroke();
        graphics.setStroke(THICK_STROKE);
        graphics.drawRect(selectedColumn * squareSize, selectedRow * squareSize, squareSize, squareSize);
        graphics.setStroke(previousStroke);
    }

    private void drawBorderTiles(Graphics2D graphics) {

        for (int column = 0; column < numberOfColumns; column++) {
            boolean south = !grid.cellAt(0, column).isLand();
            BufferedImage northBorderTile = oceanTiles.get(tileNumber(true, true, south, true));
            int xPos = (column + 1) * squareSize;
            graphics.drawImage(northBorderTile, xPos, 0, null, null);

            boolean north = !grid.cellAt(numberOfRows - 1, column).isLand();
            BufferedImage southBorderTile = oceanTiles.get(tileNumber(north, true, true, true));
            int yPos = (numberOfRows + 1) * squareSize;
            graphics.drawImage(southBorderTile, xPos, yPos, null, null);
        }
        for (int row = 0; row < numberOfRows; row++) {
            boolean east = !grid.cellAt(row, 0).isLand();
            BufferedImage westBorderTile = oceanTiles.get(tileNumber(true, east, true, true));
            graphics.drawImage(westBorderTile, 0, (row + 1) * squareSize, null, null);

            boolean west = !grid.cellAt(row, numberOfColumns - 1).isLand();
            BufferedImage eastBorderTile = oceanTiles.get(tileNumber(true, true, true, west));
            graphics.drawImage(eastBorderTile, (numberOfColumns + 1) * squareSize, (row + 1) * squareSize, null, null);
        }

        // Corner tiles
        BufferedImage allWater = oceanTiles.get(tileNumber(true, true, true, true));
        graphics.drawImage(allWater, 0, 0, null, null);
        graphics.drawImage(allWater, 0, (numberOfRows + 1) * squareSize, null, null);
        graphics.drawImage(allWater, (numberOfColumns + 1) * squareSize, 0, null, null);
        graphics.drawImage(allWater, (numberOfColumns + 1) * squareSize, (numberOfRows + 1) * squareSize, null, null);

        paintBorderGradients(graphics);

        if (drawCoordinateAxes)
            drawCoordinateAxes(graphics);
    }

    private void hatchShade(Graphics2D graphics, int row, int column, Color colour) {
        GuiUtils.drawHatchedRectangle(graphics, new Rectangle2D.Double(column * squareSize, row * squareSize,
                squareSize, squareSize), colour);
    }

    private void hatchShadeCells(Graphics2D graphics, Set<Cell> cells, Color colour) {
        Stroke previousStroke = graphics.getStroke();
        Color previousColour = graphics.getColor();
        graphics.setColor(colour);
        graphics.setStroke(new BasicStroke(3.0f));
        for (Cell cell : cells) {
            int column = cell.getColumn();
            int row = cell.getRow();
            hatchShade(graphics, row, column, colour);
            if (column == 0 || !cells.contains(cell.getWestNeighbour())) {
                graphics.drawLine(column * squareSize, row * squareSize, column * squareSize, (row + 1) * squareSize);
            }
            if (column == numberOfColumns - 1 || !cells.contains(cell.getEastNeighbour())) {
                graphics.drawLine((column + 1) * squareSize, row * squareSize, (column + 1) * squareSize, (row + 1)
                        * squareSize);
            }
            if (row == 0 || !cells.contains(cell.getNorthNeighbour())) {
                graphics.drawLine(column * squareSize, row * squareSize, (column + 1) * squareSize, row * squareSize);
            }
            if (row == numberOfRows - 1 || !cells.contains(cell.getSouthNeighbour())) {
                graphics.drawLine(column * squareSize, (row + 1) * squareSize, (column + 1) * squareSize, (row + 1)
                        * squareSize);
            }
        }
        graphics.setColor(previousColour);
        graphics.setStroke(previousStroke);
    }

    private void drawCoordinateAxes(Graphics2D graphics) {

        setFont(NUMBER_CELL_FONT);
        graphics.setColor(new Color(1f, 1f, 1f, 0.3f));
        for (int row = 0; row < numberOfRows; row++) {
            String numberString = row + "";
            Rectangle2D bounds = NUMBER_CELL_FONT.getStringBounds(numberString, graphics.getFontRenderContext());
            int width = (int) bounds.getWidth();
            int height = (int) bounds.getHeight();
            int x = squareSize / 2 - width / 2;
            int y = (row + 1) * squareSize + squareSize / 2 + height / 3;
            graphics.drawString(numberString, x, y);
        }
        for (int column = 0; column < numberOfColumns; column++) {
            String numberString = column + "";
            Rectangle2D bounds = NUMBER_CELL_FONT.getStringBounds(numberString, graphics.getFontRenderContext());
            int width = (int) bounds.getWidth();
            int height = (int) bounds.getHeight();
            int x = (column + 1) * squareSize + squareSize / 2 - width / 2;
            int y = squareSize / 2 + height / 3;
            graphics.drawString(numberString, x, y);
        }

    }

    private void paintBorderGradients(Graphics2D graphics) {
        int maxX = (numberOfColumns + 2) * squareSize;
        int maxY = (numberOfRows + 2) * squareSize;
        Rectangle clipBounds = graphics.getClipBounds();

        Rectangle2D northRectangle = new Rectangle2D.Double(0, 0, maxX, squareSize);
        if (clipBounds.intersects(northRectangle)) {
            GradientPaint northGradient = new GradientPaint(maxX / 2, squareSize / 5, Color.BLACK, maxX / 2,
                    squareSize, TRANSPARENT_BLACK, false);
            graphics.setPaint(northGradient);
            graphics.fill(northRectangle);
        }

        Rectangle2D southRectangle = new Rectangle2D.Double(0, maxY - squareSize, maxX, squareSize);
        if (clipBounds.intersects(southRectangle)) {
            GradientPaint southGradient = new GradientPaint(maxX / 2, maxY - squareSize / 5, Color.BLACK, maxX / 2,
                    maxY - squareSize, TRANSPARENT_BLACK, false);
            graphics.setPaint(southGradient);
            graphics.fill(southRectangle);
        }

        Rectangle2D westRectangle = new Rectangle2D.Double(0, 0, squareSize, maxY);
        if (clipBounds.intersects(westRectangle)) {
            GradientPaint westGradient = new GradientPaint(squareSize / 5, maxY / 2, Color.BLACK, squareSize, maxY / 2,
                    TRANSPARENT_BLACK, false);
            graphics.setPaint(westGradient);
            graphics.fill(westRectangle);
        }

        Rectangle2D eastRectangle = new Rectangle2D.Double(maxX - squareSize, 0, squareSize, maxY);
        if (clipBounds.intersects(eastRectangle)) {
            GradientPaint eastGradient = new GradientPaint(maxX - squareSize / 5, maxY / 2, Color.BLACK, maxX
                    - squareSize, maxY / 2, TRANSPARENT_BLACK, false);
            graphics.setPaint(eastGradient);
            graphics.fill(eastRectangle);
        }
    }

    private void drawGridLines(Graphics2D graphics) {
        graphics.setColor(new Color(1.0f, 1.0f, 1.0f, 0.4f));
        int maxY = numberOfRows * squareSize;
        int maxX = numberOfColumns * squareSize;
        for (int column = 1; column < numberOfColumns; column++)
            graphics.drawLine(column * squareSize, 0, column * squareSize, maxY);
        for (int row = 1; row < numberOfRows; row++)
            graphics.drawLine(0, row * squareSize, maxX, row * squareSize);
        graphics.setColor(Color.WHITE);
        Stroke oldStroke = graphics.getStroke();
        graphics.setStroke(THICK_STROKE);
        graphics.drawLine(0, 0, 0, maxY);
        graphics.drawLine(0, 0, maxX, 0);
        graphics.drawLine(0, maxY, maxX, maxY);
        graphics.drawLine(maxX, 0, maxX, maxY);

        graphics.setStroke(oldStroke);
    }

    private void drawCell(Graphics2D graphics, int row, int column) {
        Cell cell = grid.cellAt(row, column);
        CellType type = cell.getType();
        int xStart = column * squareSize;
        int yStart = row * squareSize;
        if (type == CellType.LAND)
            graphics.drawImage(getLandTile(row, column), xStart, yStart, null, null);
        else if (type == CellType.WATER)
            graphics.drawImage(getOceanTile(row, column), xStart, yStart, null, null);
        if (grid.hasNumberFor(row, column)) {
            String islandNumber = "" + grid.getIslandNumber(row, column);
            Rectangle2D bounds = NUMBER_CELL_FONT.getStringBounds(islandNumber, graphics.getFontRenderContext());
            int width = (int) bounds.getWidth();
            int height = (int) bounds.getHeight();
            drawShadowString(graphics, islandNumber, xStart + squareSize / 2 - width / 2, yStart + squareSize / 2
                    + height / 3, NUMBER_CELL_FONT);

        }
    }

    private void drawShadowString(Graphics2D graphics, String text, int x, int y, Font font) {
        graphics.setFont(font);
        graphics.setColor(Color.BLACK);
        graphics.drawString(text, x + 2, y + 2);
        graphics.setColor(Color.WHITE);
        graphics.drawString(text, x - 2, y - 2);
    }

    private Rectangle getRepaintRectangleForCell(int row, int column) {
        // Not just the cell bounds, but also the overlap for any thick border lines
        int lineWidth = (int) Math.ceil(THICK_STROKE.getLineWidth());
        int x = (column + 1) * squareSize - lineWidth;
        int y = (row + 1) * squareSize - lineWidth;
        return new Rectangle(x, y, squareSize + lineWidth * 2, squareSize + lineWidth * 2);
    }

    private int yToRow(int y) {
        return y / squareSize - 1;
    }

    private int xToColumn(int x) {
        return x / squareSize - 1;
    }

    private boolean isMouseOverCell() {
        return selectedRow >= 0 && selectedRow < numberOfRows && selectedColumn >= 0
                && selectedColumn < numberOfColumns;
    }

    // JComponent overrides
    // ====================

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D graphics = (Graphics2D) (g.create());

        // Setup anti-aliasing
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        paintBackgroundIfRequired(graphics);

        drawBorderTiles(graphics);

        // Relocate coordinates to top-left of grid for convenience
        graphics.transform(AffineTransform.getTranslateInstance(squareSize, squareSize));

        for (int row = 0; row < numberOfRows; row++)
            for (int column = 0; column < numberOfColumns; column++)
                drawCell(graphics, row, column);

        drawGridLines(graphics);

        for (HighlightingLayer layer : highlightingLayers) 
            hatchShadeCells(graphics, layer.getCells(), layer.getColour());

        if (cellSelectionHighlightingEnabled && mouseIsOverCell)
            drawSelectedCellMarker(graphics);

        graphics.dispose();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension((numberOfColumns + 2) * squareSize, (numberOfRows + 2) * squareSize);
    }

    @Override
    public Dimension getMinimumSize() {
        return getPreferredSize();
    }

    // JavaBeans properties
    // ====================

    public NurikabeGrid getGrid() {
        return grid;
    }

    public void setGrid(NurikabeGrid grid) {
        this.grid = grid;
        // TODO: potential optimisation: redraw only what's changed, which will typically be only one cell
        // Rectangle rectangleNorthWest = getRepaintRectangleForCell(row - 1, column - 1);
        // Rectangle rectangleSouthEast = getRepaintRectangleForCell(row + 1, column + 1);
        // repaint(rectangleNorthWest.union(rectangleSouthEast));
        repaint();
    }

    public boolean isCellSelectionHighlightingEnabled() {
        return cellSelectionHighlightingEnabled;
    }

    public void setCellSelectionHighlightingEnabled(boolean cellSelectionHighlightingEnabled) {
        this.cellSelectionHighlightingEnabled = cellSelectionHighlightingEnabled;
    }

    public boolean getDrawCoordinateAxes() {
        return drawCoordinateAxes;
    }

    public void setDrawCoordinateAxes(boolean drawCoordinateAxes) {
        this.drawCoordinateAxes = drawCoordinateAxes;
    }

    // NurikabeGridCellMousePressed event handling
    // ===========================================

    public void addNurikabeGridCellMousePressedListener(NurikabeGridCellMousePressedListener listener) {
        cellMousePressListenerList.add(NurikabeGridCellMousePressedListener.class, listener);
    }

    public void removeNurikabeGridCellMousePressedListener(NurikabeGridCellMousePressedListener listener) {
        cellMousePressListenerList.remove(NurikabeGridCellMousePressedListener.class, listener);
    }

    private void fireNurikabeGridCellMousePressedEvent(int row, int column, MouseEvent mouseEvent) {
        Object[] listeners = cellMousePressListenerList.getListenerList();
        NurikabeGridCellMousePressedEvent event = new NurikabeGridCellMousePressedEvent(this, grid, row, column,
                mouseEvent);
        for (int i = 0; i < listeners.length; i += 2) {
            if (!(listeners[i] instanceof Class))
                continue;
            Class<?> listenerClass = (Class<?>) listeners[i];
            if (NurikabeGridCellMousePressedListener.class.isAssignableFrom(listenerClass))
                ((NurikabeGridCellMousePressedListener) listeners[i + 1]).mousePressed(event);
        }
    }

    // Mouse listeners
    // ===============

    public void mousePressed(MouseEvent e) {
        int column = xToColumn(e.getX());
        int row = yToRow(e.getY());
        if (column < 0 || row < 0 || row >= numberOfRows || column >= numberOfColumns)
            return;
        fireNurikabeGridCellMousePressedEvent(row, column, e);
    }

    public void mouseEntered(MouseEvent e) {
        mouseIsOverCell = isMouseOverCell();
        repaint(getRepaintRectangleForCell(selectedRow, selectedColumn));
    }

    public void mouseExited(MouseEvent e) {// Don't care
        mouseIsOverCell = false;
        repaint(getRepaintRectangleForCell(selectedRow, selectedColumn));
    }

    public void mouseReleased(MouseEvent e) {
    // Don't care
    }

    public void mouseClicked(MouseEvent e) {
    // Don't care
    }

    public void mouseDragged(MouseEvent e) {
    // Don't care
    }

    public void mouseMoved(MouseEvent e) {
        int row = yToRow(e.getY());
        int column = xToColumn(e.getX());
        if (row != selectedRow || column != selectedColumn) {
            Rectangle previousRepaintRectangle = getRepaintRectangleForCell(selectedRow, selectedColumn);
            selectedRow = row;
            selectedColumn = column;
            mouseIsOverCell = isMouseOverCell();
            Rectangle newRepaintRectangle = getRepaintRectangleForCell(row, column);
            Rectangle repaintRegion = newRepaintRectangle.union(previousRepaintRectangle);
            repaint(repaintRegion);
        }
    }

    public void addHighlightingLayer(HighlightingLayer layer) {
        highlightingLayers.add(layer);
        repaint();
    }

    public void clearHighlightingLayers() {
        highlightingLayers.clear();
        repaint();
    }
    
    public static class HighlightingLayer {
        private Set<Cell> cells;
        private Color colour;

        public HighlightingLayer(Set<Cell> cells, Color colour) {
            this.cells = cells;
            this.colour = colour;
        }

        public Set<Cell> getCells() {
            return cells;
        }

        public Color getColour() {
            return colour;
        }
    }
    
    
    
}

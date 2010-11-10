package utils;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Rectangle2D;

public class GuiUtils {

    public static void drawHatchedRectangle(Graphics2D graphics, Rectangle2D rectangle, Color colour) {
        Color previousColour = graphics.getColor();
        Stroke previousStroke = graphics.getStroke();
        graphics.setColor(colour);
        graphics.setStroke(new BasicStroke(1f));

        double width = rectangle.getWidth();
        double height = rectangle.getHeight();
        double total = width + height;
        double increment = 8.0;
        double sum = 0.0;
        while (sum < total) {
            sum += increment;
            double x1 = rectangle.getX();
            double y1 = rectangle.getY();
            if (sum > width) {
                x1 += width;
                y1 += sum - width;
            } else {
                x1 += sum;
                y1 += 0;
            }
            double x2 = rectangle.getX();
            double y2 = rectangle.getY();
            if (sum > height) {
                x2 += sum - height;
                y2 += height;
            } else {
                x2 += 0;
                y2 += sum;
            }
            graphics.drawLine((int) x1, (int) y1, (int) x2, (int) y2);
        }
        graphics.setColor(previousColour);
        graphics.setStroke(previousStroke);
    }
}

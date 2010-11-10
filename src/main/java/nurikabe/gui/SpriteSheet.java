package nurikabe.gui;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

public class SpriteSheet {

    //    private static final SpriteSheet INSTANCE = new SpriteSheet( "/neotrident-tiles.png", 30 );
    private static final SpriteSheet INSTANCE = new SpriteSheet( "/bigtrident-tiles.png", 60 );
    //    private static final SpriteSheet INSTANCE = new SpriteSheet( "/engels-tiles.png", 45 );

    private final int squareSize;

    private BufferedImage image;

    private SpriteSheet( String filename, int squareSize ) {
        this.squareSize = squareSize;

        loadImage( filename );

    }

    public static int getSquareSize() {
        return INSTANCE.squareSize;
    }

    public static SpriteSheet getInstance() {
        return INSTANCE;
    }

    private void loadImage( String filename ) {
        InputStream imageStream = SpriteSheet.class.getResourceAsStream( filename );
        try {
            image = ImageIO.read( imageStream );
        } catch ( IOException e ) {
            e.printStackTrace(); // TODO: Handle exception better
        } finally {
            try {
                if ( imageStream != null )
                    imageStream.close();
            } catch ( IOException e ) {
                e.printStackTrace();
            }

        }
    }

    public BufferedImage getSprite( int row, int column ) {
        return image.getSubimage( column * squareSize, row * squareSize, squareSize, squareSize );
    }

}

package Business.Java;

import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;

public class screenParameter {
    public screenParameter() {
        rec_screen = Screen.getPrimary().getVisualBounds();
    }

    public double getScreenHeight() {
        return rec_screen.getHeight();
    }

    public double getScreenWidth() {
        return rec_screen.getWidth();
    }

    private Rectangle2D rec_screen;
}

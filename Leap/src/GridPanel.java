import java.awt.*;
import javax.swing.*;

public class GridPanel extends JPanel{

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawLine(getWidth() / 2, 0, getWidth() / 2, getHeight());
        g.drawLine(0, getHeight() / 2, getWidth(), getHeight() / 2);
    }
}

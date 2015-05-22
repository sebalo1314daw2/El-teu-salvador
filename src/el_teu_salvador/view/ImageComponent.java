package el_teu_salvador.view;

import java.awt.Dimension;
import java.awt.Graphics;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class ImageComponent extends JPanel {
    // ================================ Attributes =====================================================
    private File file;
    // ================================ Constructors =====================================================
    public ImageComponent(File file) {
        this.setPreferredSize(new Dimension(96, 96) );
        this.file = file;
    }
    // ================================ Methods =====================================================
    @Override
    public void paint(Graphics grphcs) {
        try {
            Dimension dimension = new Dimension(96, 96);
            ImageIcon imgIcon = new ImageIcon( ImageIO.read(file) );
            grphcs.drawImage(imgIcon.getImage(), 0, 0, dimension.width, dimension.height, null);
            this.setOpaque(false);
            super.paintComponent(grphcs);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}

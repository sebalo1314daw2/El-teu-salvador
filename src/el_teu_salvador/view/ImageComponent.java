package el_teu_salvador.view;

import el_teu_salvador.model.Photo;
import java.awt.Dimension;
import java.awt.Graphics;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class ImageComponent extends JPanel {
    // ================================ Attributes =====================================================
    private Photo photo;
    // ================================ Constructors =====================================================
    public ImageComponent(Photo photo) {
        this.setPreferredSize(new Dimension(96, 96) );
        this.photo = photo;
    }
    // ================================ Accessors =====================================================
    public void setPhoto(Photo photo) {
        this.photo = photo;
        this.repaint();
    }
    // ================================ Methods =====================================================
    @Override
    public void paint(Graphics grphcs) {
        try {
            Dimension dimension = new Dimension(96, 96);
            ImageIcon imgIcon = new ImageIcon( ImageIO.read( photo.getSource() ) );
            grphcs.drawImage(imgIcon.getImage(), 0, 0, dimension.width, dimension.height, null);
            this.setOpaque(false);
            super.paintComponent(grphcs);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}

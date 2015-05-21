package el_teu_salvador;

import el_teu_salvador.control.Controller;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String [] args) {
        SwingUtilities.invokeLater (
            new Runnable() {
                public void run() {
                    new Controller();
                }
            }
        );
    }
}
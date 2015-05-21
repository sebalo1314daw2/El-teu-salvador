package el_teu_salvador.view;

import el_teu_salvador.control.Controller;
import el_teu_salvador.model.exceptions.VCFNotSelectedException;
import el_teu_salvador.model.persistence.VCF;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

public class MainView extends JFrame {
    // =============================== Attributes ======================================================
    private Controller controller;
    public static final String INVALID_VCF = "L'arxiu seleccionat no és del tipus VCF";
    // ================================ Constructors =====================================================
    public MainView(Controller controller) {
        initComponents();
        this.controller = controller;
    }
    // ================================ Methods =====================================================
    /**
     * initComponents()
     * This procedure initializes all the components of the main view
     * @author Sergio Baena Lopez
     * @version 1.0
     */
    private void initComponents() {
        this.setTitle("El teu salvador");
        
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(400, 200);
        
        JMenuBar menuBar = buildMainMenu();
        this.setJMenuBar(menuBar);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
    /**
     * buildMainMenu()
     * This procedure builds the main menu of the application
     * @author Sergio Baena Lopez
     * @version 1.0
     * @return JMenuBar the main menu of the application 
     */
    private JMenuBar buildMainMenu() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menu;
        JMenuItem menuItem;
        // Create Fitxer
        menu = new JMenu("Fitxer");
        
        menuItem = new JMenuItem("Importar contactes");
        menuItem.addActionListener(new ActionListener() {
             public void actionPerformed(ActionEvent event) { controller.importContacts(); }
        });
        menu.add(menuItem);
        
        menuItem = new JMenuItem("Exportar contactes");
        menu.add(menuItem);
        
        menuItem = new JMenuItem("Sortir");
        menu.add(menuItem);
        
        menuBar.add(menu);
        // Create Contactes
        menu = new JMenu("Contactes");
        
        menuItem = new JMenuItem("Afegir");
        menu.add(menuItem);
        
        menuItem = new JMenuItem("Llistar");
        menu.add(menuItem);
        
        menuItem = new JMenuItem("Buscar");
        menu.add(menuItem);
        
        menuItem = new JMenuItem("Editar");
        menu.add(menuItem);
        
        menuItem = new JMenuItem("Eliminar");
        menu.add(menuItem);
        
        menuBar.add(menu);
        // Create Usuaris
        menu = new JMenu("Usuaris");
        
        menuItem = new JMenuItem("Canviar l'actiu");
        menu.add(menuItem);
        
        menuItem = new JMenuItem("Afegir");
        menu.add(menuItem);
        
        menuItem = new JMenuItem("Llistar");
        menu.add(menuItem);
        
        menuItem = new JMenuItem("Buscar");
        menu.add(menuItem);
        
        menuItem = new JMenuItem("Editar");
        menu.add(menuItem);
        
        menuItem = new JMenuItem("Eliminar");
        menu.add(menuItem);
        
        menuBar.add(menu);
        // Create Ajuda
        menu = new JMenu("Ajuda");
        
        menuItem = new JMenuItem("Tutorial de com importar els contactes del teu móvil");
        menu.add(menuItem);

        menuItem = new JMenuItem("Respecte de El teu salvador");
        menu.add(menuItem);
        
        menuBar.add(menu);
        
        return menuBar;
    }
    /**
     * selectVCF()
     * This function shows the file explorer and then the user selects a VCF file
     * @author Sergio Baena Lopez
     * @version 1.0
     * @throws VCFNotSelectedException if the user didn't select any VCF file
     * @return VCF the VCF file which the user selected
     */
    public VCF selectVCF() throws VCFNotSelectedException {
        // http://javiergarbedo.es/15-apuntes-java/ficheros/48-cuadro-de-dialogo-de-seleccion-de-archivos-o-directorios
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("VCF", "vcf");
        fileChooser.setFileFilter(filter);
        int selection = fileChooser.showDialog(this, "Importar contactes");
        if(selection != JFileChooser.APPROVE_OPTION) {
            throw new VCFNotSelectedException("The user doesn't select any VCF file");
        }
        return new VCF( fileChooser.getSelectedFile() ); 
    }
    /**
     * showErrorMsg()
     * This procedure shows the specified error message
     * @author Sergio Baena Lopez
     * @version 1.0
     * @param String msg the error message to show
     */
    public void showErrorMsg(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }
    /**
     * changeView()
     * This procedure changes its current view (ContentPane)
     * @author Sergio Baena Lopez
     * @version 1.0
     * @param JPanel view the view to set
     */
    public void changeView(JPanel view) {
        this.setContentPane(view);
        this.validate();
    }
}

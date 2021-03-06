package el_teu_salvador.view;

import el_teu_salvador.control.Controller;
import el_teu_salvador.model.Contact;
import el_teu_salvador.model.Photo;
import el_teu_salvador.model.exceptions.DirectoryNotSelectedException;
import el_teu_salvador.model.exceptions.VCFNotSelectedException;
import el_teu_salvador.model.persistence.ImageFile;
import el_teu_salvador.model.persistence.VCF;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

public class MainView extends JFrame {
    // =============================== Attributes ======================================================
    private Controller controller;
    
    public static final String INVALID_VCF_MSG = "L'arxiu seleccionat no és del tipus VCF";
    public static final String DIRECTORY_NOT_EXISTED_MSG = "La carpeta seleccionada no existeix";
    public static final String DIRECTORY_NO_EDITABLE_MSG = "Accés denegat a la carpeta";
    
    public static final String SUCCESSFUL_EXPORTATION_MSG = "S'ha exportat tots els contactes satisfactòriament";
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
     * @version 7.0
     */
    private void initComponents() {
        this.setTitle("El teu salvador");
        
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {public void windowClosing(WindowEvent we) {
            controller.exit();
        }});
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
     * @version 9.0
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
        menuItem.addActionListener(new ActionListener() {public void actionPerformed(ActionEvent event) { 
            controller.exportContacts();
        }});
        menu.add(menuItem);
        
        menuItem = new JMenuItem("Sortir");
        menuItem.addActionListener(new ActionListener() {public void actionPerformed(ActionEvent event) { 
            controller.exit();
        }});
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
     * showSuccessMsg()
     * This procedure shows the specified successful message
     * @author Sergio Baena Lopez
     * @version 4.0
     * @param String msg the successful message to show
     */
    public void showSuccessMsg(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Èxit", JOptionPane.INFORMATION_MESSAGE);
    }
    /**
     * obtainImageComponent()
     * This function obtains the image component from the specified contact
     * @author Sergio Baena Lopez
     * @version 6.2
     * @throws FileNotFoundException if the image file wasn't found
     * @param Contact contact the contact whose image component we want to obtain
     * @return ImageComponent the image component
     */
    public static ImageComponent obtainImageComponent(Contact contact) throws FileNotFoundException {
        Photo photo;
        if(contact.getPhoto() == null) { // This contact doesn't have photo
            photo = new Photo (
                new File (
                    ImageFile.CONTAINER_DIRECTORY_PATH + ImageFile.DEFAULT_IMAGE_PATH
                )
            );
        } else { // This contact has photo
            photo = new Photo ( 
                new File (
                    ImageFile.CONTAINER_DIRECTORY_PATH  +
                    contact.getId()                     +
                    "."                                 + 
                    contact.getPhoto().getType()
                )
            );
        }
        File source = photo.getSource();
        if( !source.exists() ) {
            throw new FileNotFoundException("The file image " + source + " wasn't found");
        } // The file image was found
        return new ImageComponent(photo);
    }
    /**
     * selectDirectory()
     * This function shows the file explorer and then the user selects a directory
     * @author Sergio Baena Lopez
     * @version 9.0
     * @return File the selected directory
     * @throws DirectoryNotSelectedException if the user didn't selected any directory
     */
    public File selectDirectory() throws DirectoryNotSelectedException {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int selection = fileChooser.showDialog(this, "Exportar contactes");
        if(selection != JFileChooser.APPROVE_OPTION) {
            throw new DirectoryNotSelectedException("The user doesn't select any directory");
        }
        return fileChooser.getSelectedFile();
    }
}
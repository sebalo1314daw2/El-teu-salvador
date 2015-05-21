package el_teu_salvador.view;

import el_teu_salvador.control.Controller;
import el_teu_salvador.model.Contact;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class ContactAdminPanel extends JPanel {
    // =============================== Attributes ======================================================
    private Controller controller;
    private JPanel table;
    // ================================ Constructors =====================================================
    public ContactAdminPanel(Controller controller, List<Contact> contactList) {
        initComponents(contactList);
        this.controller = controller;
    }
    // ================================ Methods =====================================================
    /**
     * initComponents()
     * This procedure initializes all the components of the contacts' administration
     * @author Sergio Baena Lopez
     * @version 1.0
     * @param List<Contact> contactList the list of contact to manage
     */
    private void initComponents(List<Contact> contactList) {
        // Setting the layout 
        this.setLayout( new BoxLayout(this, BoxLayout.Y_AXIS) );
        // Creating the container of the title
        JPanel titleContainer = new JPanel();
        titleContainer.setLayout( new FlowLayout(FlowLayout.LEFT) );
        JLabel title = new JLabel("Els teus contactes");
        titleContainer.add(title);
        this.add(titleContainer);
        // Creating the container of searchº engine
        JPanel searchEngineContainer = new JPanel();
        searchEngineContainer.setLayout( new FlowLayout(FlowLayout.LEFT) );
        JTextField searchEngine = new JTextField("Cerca pel nom");
        JButton searchButton = new JButton("Cercar");
        searchEngineContainer.add(searchEngine);
        searchEngineContainer.add(searchButton);
        this.add(searchEngineContainer);
        // Creating the container of the group of buttons
        JPanel buttonGroupContainer = new JPanel();
        buttonGroupContainer.setLayout( new FlowLayout(FlowLayout.LEFT) );
        JButton addButton = new JButton("Afegir");
        JButton editButton = new JButton("Editar");
        JButton deleteButton = new JButton("Eliminar");
        buttonGroupContainer.add(addButton);
        buttonGroupContainer.add(editButton);
        buttonGroupContainer.add(deleteButton);
        this.add(buttonGroupContainer);
        // Creating the table of contacts
        
    }
    /**
     * generateTable()
     * This procedure generates the table of contacts
     * @author Sergio Baena Lopez
     * @version 1.0
     * @param List<Contact> contactList the list of contact of the table to generate
     */
    public void generateTable(List<Contact> contactList) {
        table = new JPanel();
        // Setting layout
        table.setLayout( new GridLayout(0, 4) );
        // Setting the headers
        table.add( new JLabel("Seleccionar") );
        table.add( new JLabel("Foto") );
        table.add( new JLabel("Nom") );
        table.add( new JLabel("Telèfons") );
        // Setting the content
        for(int i = 0; i < contactList.size(); i++) {
            // Getting the data (a contact)
            final Contact aContact = contactList.get(i);
            // Setting a checkbox
            final JCheckBox aCheckbox = new JCheckBox();
            aCheckbox.addChangeListener (
                new ChangeListener() {
                    @Override
                    public void stateChanged(ChangeEvent ce) {
                        controller.modifySelectedContactList(aCheckbox, aContact);
                    }                
                }
            );
            table.add(aCheckbox);
            // Setting the photo
            
            
            
            
            
            
            
        }
        
        
        
        
        
        
        
        
    }
}
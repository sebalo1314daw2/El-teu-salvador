package el_teu_salvador.view;

import el_teu_salvador.control.Controller;
import el_teu_salvador.model.Contact;
import el_teu_salvador.model.ContactList;
import el_teu_salvador.model.Phone;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class ContactAdminPanel extends JPanel {
    // =============================== Attributes ======================================================
    private Controller controller;
    private JScrollPane scrollTable;
    private List<JCheckBox> checkboxesList;
    private JTextField searchEngine;
    private boolean isFirstTime;
    
    private static final String PLACEHOLDER_MSG = "Cerca pel nom";
    public static final String CONTACT_NOT_FOUND_MSG = "No s'ha trobat cap contacte";
    public static final String NO_CONTACT_SELECTED_MSG = "No has seleccionat cap contacte";
    public static final String TOO_MANY_SELECTED_CONTACTS_MSG = "Has de seleccionar solament un contacte";
    // ================================ Constructors =====================================================
    public ContactAdminPanel(Controller controller, ContactList contactList) throws IOException {
        checkboxesList = new ArrayList<JCheckBox>();
        isFirstTime = true;
        initComponents(contactList);
        this.controller = controller;
    }
    // ================================ Methods =====================================================
    /**
     * initComponents()
     * This procedure initializes all the components of the contacts' administration
     * @author Sergio Baena Lopez
     * @version 1.0
     * @param ContactList contactList the list of contact to manage
     */
    private void initComponents(ContactList contactList) throws IOException {
        // Setting the layout 
        this.setLayout( new BoxLayout(this, BoxLayout.Y_AXIS) );
        // Creating the container of the title
        JPanel titleContainer = new JPanel();
        titleContainer.setLayout( new FlowLayout(FlowLayout.CENTER) );
        JLabel title = new JLabel("<html><span style='font-size:40px'>Els teus contactes</span></html>");
        titleContainer.add(title);
        titleContainer.setBackground( new Color(199, 199, 193) );
        this.add(titleContainer);
        // Creating the container of search engine
        JPanel searchEngineContainer = new JPanel();
        searchEngineContainer.setLayout( new FlowLayout(FlowLayout.CENTER) );
        searchEngine = new JTextField(30);
        searchEngine.setHorizontalAlignment(SwingConstants.CENTER);
        this.putPlaceholderIfIsNecessary();
        searchEngine.addFocusListener(new FocusListener() {public void focusGained(FocusEvent fe) {
            controller.quitPlaceholderIfIsNecessary();
        } public void focusLost(FocusEvent fe) {
            controller.putPlaceholderIfIsNecessary();
        }});
        JButton searchButton = new JButton("Cercar");
        searchButton.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent ae) {
            controller.findContacts();
        }});
        searchEngineContainer.add(searchEngine);
        searchEngineContainer.add(searchButton);
        searchEngineContainer.setBackground( new Color(199, 199, 193) );
        this.add(searchEngineContainer);
        // Creating the container of the group of buttons
        JPanel buttonGroupContainer = new JPanel();
        buttonGroupContainer.setLayout( new FlowLayout(FlowLayout.CENTER) );
        JButton addButton = new JButton("Afegir");
        addButton.addActionListener(new ActionListener() {public void actionPerformed(ActionEvent ae) {
            controller.showContactRegister();
        }});
        JButton listAllButton = new JButton("Llistar tots");
        listAllButton.addActionListener(new ActionListener() {public void actionPerformed(ActionEvent ae) {
            controller.listAllContacts();
        }});
        JButton editButton = new JButton("Editar");
        editButton.addActionListener(new ActionListener() {public void actionPerformed(ActionEvent ae) {
            controller.showFormContactToModification();
        }});
        JButton deleteButton = new JButton("Eliminar");
        deleteButton.addActionListener(new ActionListener() {public void actionPerformed(ActionEvent ae) {
            controller.removeContacts();
        }});
        JButton selectAllButton = new JButton("Seleccionar tot");
        selectAllButton.addActionListener(new ActionListener() {public void actionPerformed(ActionEvent ae) {
            controller.selectAllContacts();
        }});
        JButton clearButton = new JButton("Netejar selecció");
        clearButton.addActionListener (new ActionListener() {public void actionPerformed(ActionEvent ae) {
            controller.deselectAllContacts();
        }});
        buttonGroupContainer.add(addButton);
        buttonGroupContainer.add(listAllButton);
        buttonGroupContainer.add(editButton);
        buttonGroupContainer.add(deleteButton);
        buttonGroupContainer.add(selectAllButton);
        buttonGroupContainer.add(clearButton);
        buttonGroupContainer.setBackground( new Color(199, 199, 193) );
        this.add(buttonGroupContainer);
        // Creating the table of contacts
        generateTable(contactList);
    }
    /**
     * generateTable()
     * This procedure generates the table of contacts
     * @author Sergio Baena Lopez
     * @version 3.0
     * @param ContactList contactList the list of contact of the table to generate
     */
    public void generateTable(ContactList contactList) throws IOException {
        JPanel table = new JPanel();
        // Setting layout
        table.setLayout( new GridLayout(0, 4, 8, 11) );
        // Setting the headers
        table.add( new JLabel("<html><span style='font-size:15px'>Seleccionar</span></html>") );
        table.add( new JLabel("<html><span style='font-size:15px'>Foto</span></html>") );
        table.add( new JLabel("<html><span style='font-size:15px'>Nom</span></html>") );
        table.add( new JLabel("<html><span style='font-size:15px'>Telèfons</span></html>") );
        // Setting the content
        for(int i = 0; i < contactList.size(); i++) {
            // Getting the data (a contact)
            final Contact aContact = contactList.get(i);
            // Setting a checkbox
            final JCheckBox aCheckbox = new JCheckBox();
            aCheckbox.setPreferredSize( new Dimension(11, 11) );
            aCheckbox.addActionListener(new ActionListener() {public void actionPerformed(ActionEvent ae) {
                controller.modifySelectedContactList(aCheckbox, aContact);
            }});
            checkboxesList.add(aCheckbox);        
            table.add(aCheckbox);
            // Setting the photo
            table.add( MainView.obtainImageComponent(aContact) );
            // Setting the name of the contact
            JLabel nameLabel = new JLabel( aContact.getName() );
            nameLabel.setPreferredSize( new Dimension(125, 96) );
            table.add(nameLabel);
            // Setting the phones of the contact
            JLabel listComponent = obtainPhoneListComponent( aContact.getPhoneList() );
            listComponent.setPreferredSize( new Dimension(140, 96) );
            table.add(listComponent);
        }
        // We create a scroll pane y add the table
        if(!isFirstTime) {
            this.remove(scrollTable);
        } else {
            isFirstTime = false;
        }
        scrollTable = new JScrollPane(table);
        this.add(scrollTable);
        this.validate();
    }
        /**
         * obtainPhoneListComponent()
         * This function obtains the component list of phones (JLabel) with the specified list of 
         * phones
         * @author Sergio Baena Lopez
         * @version 1.0
         * @param List<Phone> phoneList the list of phones whose component list we want to obtain
         * @return JLabel the component list of phones
         */
       private JLabel obtainPhoneListComponent(List<Phone> phoneList) {
           StringBuilder builder = new StringBuilder();
           builder.append("<html>");
           for(int i = 0; i < phoneList.size(); i++) {
               Phone aPhone = phoneList.get(i);
               switch( aPhone.getType() ) {
                   case Phone.MOBILE:
                       builder.append("Mòbil: ");
                       builder.append( aPhone.getNumber() );
                       break;
                   case Phone.LANDLINE:
                       builder.append("Fix: ");
                       builder.append( aPhone.getNumber() );
                       break;
               }
               builder.append("<br />");
           } // We've already added all the phones to the builder
           builder.append("</html>");
           return new JLabel( builder.toString() );   
       }
       /**
        * checkAll()
        * This procedure checks all the checkboxes
        * @author Sergio Baena Lopez
        * @version 1.0
        */
       public void checkAll() {
           for(int i = 0; i < checkboxesList.size(); i++) {
                checkboxesList.get(i).setSelected(true);
           }           
       }
       /**
        * uncheckAll()
        * This procedure unchecks all the checkboxes
        * @author Sergio Baena Lopez
        * @version 2.2
        */
       public void uncheckAll() {
           for(int i = 0; i < checkboxesList.size(); i++) {
                checkboxesList.get(i).setSelected(false);
           }           
       }
       /**
        * quitPlaceholderIfIsNecessary()
        * This procedure quits the placeholder if is it necessary
        * @author Sergio Baena Lopez
        * @version 3.0
        */
       public void quitPlaceholderIfIsNecessary() {
           if( searchEngine.getText().equals(PLACEHOLDER_MSG) ) { // We've to quit the placeholder
                searchEngine.setText("");
                searchEngine.setForeground(Color.BLACK);
           }
       }
       /**
        * putPlaceholderIfIsNecessary()
        * This procedure puts the placeholder if is it necessary
        * @author Sergio Baena Lopez
        * @version 3.0
        */
       public void putPlaceholderIfIsNecessary() {
           if( searchEngine.getText().equals("") ) {
                searchEngine.setText(PLACEHOLDER_MSG);
                searchEngine.setForeground( new Color(138, 138, 138) );
           }
       }
       /**
        * readSearchEngine()
        * This function reads the search engine
        * @author Sergio Baena Lopez
        * @version 3.0
        * @return Contact the search engine as contact
        */
       public Contact readSearchEngine() {
           String readTextbox = searchEngine.getText();
           Contact readContact;
           if( readTextbox.equals(PLACEHOLDER_MSG) ) {
               readContact = new Contact("");
           } else {
               readContact = new Contact(readTextbox);
           }
           return readContact;
       }
    }
       
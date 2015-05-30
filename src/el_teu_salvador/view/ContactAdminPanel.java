package el_teu_salvador.view;

import el_teu_salvador.control.Controller;
import el_teu_salvador.model.Contact;
import el_teu_salvador.model.Phone;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class ContactAdminPanel extends JPanel {
    // =============================== Attributes ======================================================
    private Controller controller;
    private JScrollPane scrollTable;
    private List<JCheckBox> checkboxesList;
    // ================================ Constructors =====================================================
    public ContactAdminPanel(Controller controller, List<Contact> contactList) throws IOException {
        checkboxesList = new ArrayList<JCheckBox>();
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
    private void initComponents(List<Contact> contactList) throws IOException {
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
        JTextField searchEngine = new JTextField("Cerca pel nom", 30);
        searchEngine.setHorizontalAlignment(SwingConstants.CENTER);
        searchEngine.setForeground( new Color(138, 138, 138) );
        JButton searchButton = new JButton("Cercar");
        searchEngineContainer.add(searchEngine);
        searchEngineContainer.add(searchButton);
        searchEngineContainer.setBackground( new Color(199, 199, 193) );
        this.add(searchEngineContainer);
        // Creating the container of the group of buttons
        JPanel buttonGroupContainer = new JPanel();
        buttonGroupContainer.setLayout( new FlowLayout(FlowLayout.CENTER) );
        JButton addButton = new JButton("Afegir");
        JButton editButton = new JButton("Editar");
        JButton deleteButton = new JButton("Eliminar");
        JButton selectAllButton = new JButton("Seleccionar tot");
        selectAllButton.addActionListener (
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent ae) {
                        controller.selectAllContacts();
                    }
                }
        );
        JButton clearButton = new JButton("Netejar selecció");
        buttonGroupContainer.add(addButton);
        buttonGroupContainer.add(editButton);
        buttonGroupContainer.add(deleteButton);
        buttonGroupContainer.add(selectAllButton);
        buttonGroupContainer.add(clearButton);
        buttonGroupContainer.setBackground( new Color(199, 199, 193) );
        this.add(buttonGroupContainer);
        // Creating the table of contacts
        generateTable(contactList);
        this.add(scrollTable);
    }
    /**
     * generateTable()
     * This procedure generates the table of contacts
     * @author Sergio Baena Lopez
     * @version 1.0
     * @param List<Contact> contactList the list of contact of the table to generate
     */
    public void generateTable(List<Contact> contactList) throws IOException {
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
            aCheckbox.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent ae) {
                    controller.modifySelectedContactList(aCheckbox, aContact);
                }
            });
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
        scrollTable = new JScrollPane(table);
        scrollTable.validate();
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
    }
       
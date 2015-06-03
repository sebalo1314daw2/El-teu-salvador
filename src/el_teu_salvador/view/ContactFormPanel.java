package el_teu_salvador.view;

import el_teu_salvador.control.Controller;
import el_teu_salvador.model.Contact;
import el_teu_salvador.model.Phone;
import el_teu_salvador.model.Photo;
import el_teu_salvador.model.exceptions.PhoneFieldNotFoundException;
import el_teu_salvador.model.persistence.ImageFile;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

public class ContactFormPanel extends JPanel {
    // =============================== Attributes ======================================================
    private Controller controller;
    private JPanel form;
    private ImageComponent imageComponent;
    private JTextField nameTextbox;
    private List<JComboBox> selectList;
    private List<JTextField> textboxList;
    // ================================ Constructors =====================================================
    public ContactFormPanel(Controller controller, Contact contact) {
        this.controller = controller;
        selectList = new ArrayList<JComboBox>();
        textboxList = new ArrayList<JTextField>();
        initComponents(contact);
    }
    // ================================ Methods =====================================================
     /**
     * initComponents()
     * This procedure initializes all the components of the contact's form
     * @author Sergio Baena Lopez
     * @version 5.0
     * @param Contact contact the contact to modify or null (if the form is a register)
     */
    private void initComponents(Contact contact) {
        // Setting the layout 
        this.setLayout( new BoxLayout(this, BoxLayout.Y_AXIS) );
        // Creating the container of the title
        JPanel titleContainer = new JPanel();
        titleContainer.setLayout( new FlowLayout(FlowLayout.CENTER) );
        JLabel title = new JLabel("<html><span style='font-size:40px'>Contacte</span></html>");
        titleContainer.add(title);
        titleContainer.setBackground( new Color(199, 199, 193) );
        this.add(titleContainer);
        // Creating the container of the group of buttons
        generateButtonGroup(contact);
        // Creating the form
        generateForm(contact);
    }
    /**
     * generateButtonGroup()
     * This procedure generates the button group of the form
     * @author Sergio Baena Lopez
     * @version 5.0
     * @param Contact contact the contact to modify or null (if the form is a register)
     */
    private void generateButtonGroup(Contact contact) {
        JPanel buttonGroupContainer = new JPanel();
        buttonGroupContainer.setLayout( new FlowLayout(FlowLayout.CENTER) );
        JButton addPhoneButton = new JButton("Afegir telèfon");
        addPhoneButton.addActionListener(new ActionListener() {public void actionPerformed(ActionEvent ae) {
            controller.addPhoneField();
        }});
        JButton removePhoneButton = new JButton("Eliminar telèfon");
        removePhoneButton.addActionListener(new ActionListener() {public void actionPerformed(ActionEvent ae) {
            controller.removePhoneField();
        }});
        JButton confirmButton;
        if(contact == null) { // the form is a register
            confirmButton = new JButton("Afegir");
        } else {
            confirmButton = new JButton("Editar");
        }
        JButton cancelButton = new JButton("Cancel·lar");
        cancelButton.addActionListener(new ActionListener() {public void actionPerformed(ActionEvent ae) {
            controller.cancelContactForm();
        }});
        buttonGroupContainer.add(addPhoneButton);
        buttonGroupContainer.add(removePhoneButton);
        buttonGroupContainer.add(confirmButton);
        buttonGroupContainer.add(cancelButton);
        buttonGroupContainer.setBackground( new Color(199, 199, 193) );
        this.add(buttonGroupContainer);
    }
    /**
     * generateForm()
     * This procedure generates the form
     * @author Sergio Baena Lopez
     * @version 5.0
     * @param Contact contact the contact to modify or null (if the form is a register)
     */
    private void generateForm(Contact contact) {
        form = new JPanel();
        form.setLayout( new GridLayout(0, 2) );
        JLabel changePhoto = new JLabel("Canviar foto");
        form.add(changePhoto);
        
        if(contact == null) { // the form is a register
            generateRegisterForm();
        } else {
            generateEditionForm(contact);
        }
        
        JScrollPane scrollForm = new JScrollPane(form);
        this.add(scrollForm);
    }
    /**
     * generateRegisterForm()
     * This procedure generates the register form
     * @author Sergio Baena Lopez
     * @version 5.0
     */
    private void generateRegisterForm() {
        imageComponent = new ImageComponent (
            ImageFile.CONTAINER_DIRECTORY_PATH + ImageFile.DEFAULT_IMAGE_PATH
        );
        form.add(imageComponent);
        // Name field
        JLabel nameLabel = new JLabel("Nom");
        nameTextbox = new JTextField();
        
        form.add(nameLabel);
        form.add(nameTextbox);
        
        addPhoneField();
    }
    /**
     * generateEditionForm()
     * This procedure generates the form of edition
     * @author Sergio Baena Lopez
     * @version 5.0
     * @param Contact contact the contact to modify
     */
    private void generateEditionForm(Contact contact) {
        Photo photo = contact.getPhoto();
        String name = contact.getName();
        
        if(photo == null) { // the contact hasn't photo
            imageComponent = new ImageComponent (
                ImageFile.CONTAINER_DIRECTORY_PATH + ImageFile.DEFAULT_IMAGE_PATH
            );
        } else { // the contact has photo
            imageComponent = new ImageComponent (
                ImageFile.CONTAINER_DIRECTORY_PATH  + 
                name                                +
                "."                                 + 
                photo.getType()
            );
        }
        form.add(imageComponent);
        // Name field 
        JLabel nameLabel = new JLabel("Nom");
        nameTextbox = new JTextField(name);
        
        form.add(nameLabel);
        form.add(nameTextbox);
        // Phones fields
        List<Phone> phoneList = contact.getPhoneList();
        for(int i = 0; i < phoneList.size(); i++) {
            Phone aPhone = phoneList.get(i);
            // Phone field
            JComboBox aSelect = obtainSelect(aPhone);
            JTextField aTextbox = new JTextField( aPhone.getNumber() );
            
            selectList.add(aSelect);
            textboxList.add(aTextbox);
            
            form.add(aSelect);
            form.add(aTextbox);
        }
    }
    /**
     * obtainSelect()
     * This function obtains a select about phones' type.
     * @author Sergio Baena Lopez
     * @version 5.0
     * @param Phone selectedPhone the selected phone
     * @return JComboBox a select about phones' type
     */
     private JComboBox obtainSelect(Phone selectedPhone) { 
         JComboBox select = new JComboBox();
         
         select.insertItemAt("Mòbil", Phone.MOBILE);
         select.insertItemAt("Fix", Phone.LANDLINE);
         
         select.setSelectedIndex( selectedPhone.getType() );
         
         return select;
     }
    /**
     * obtainSelect()
     * This function obtains a select about phones' type. The selected option is mobile (default)
     * @author Sergio Baena Lopez
     * @version 5.0
     * @return JComboBox a select about phones' type
     */
     private JComboBox obtainSelect() {
         return obtainSelect( new Phone(Phone.MOBILE) );
     }
     /**
      * addPhoneField()
      * This procedure adds a new phone's field
      * @author Sergio Baena Lopez
      * @version 5.1
      */
     public void addPhoneField() {
        JComboBox aSelect = obtainSelect();
        JTextField aTextbox = new JTextField();

        selectList.add(aSelect);
        textboxList.add(aTextbox);

        form.add(aSelect);
        form.add(aTextbox);
        
        this.validate();
     }
     /**
      * removePhoneField()
      * This procedure removes the last phone's field
      * @author Sergio Baena Lopez
      * @version 5.2
      * @throws PhoneFieldNotFoundException if the phone field wasn't found in the form
      */
     public void removePhoneField() throws PhoneFieldNotFoundException {
         if( selectList.isEmpty() ) { // There aren't more phone fields
             throw new PhoneFieldNotFoundException("The removing of phone fields are impossible");
         }
         
         int index = selectList.size() - 1;
         
         form.remove( selectList.get(index) );
         form.remove( textboxList.get(index) );
         
         form.validate();
         
         selectList.remove(index);
         textboxList.remove(index);
     }
}

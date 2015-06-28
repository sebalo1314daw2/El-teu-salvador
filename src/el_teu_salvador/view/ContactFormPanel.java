package el_teu_salvador.view;

import el_teu_salvador.control.Controller;
import el_teu_salvador.model.Contact;
import el_teu_salvador.model.Phone;
import el_teu_salvador.model.Photo;
import el_teu_salvador.model.exceptions.PhoneFieldNotFoundException;
import el_teu_salvador.model.exceptions.PhotoNotSelectedException;
import el_teu_salvador.model.persistence.ImageFile;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

public class ContactFormPanel extends JPanel {
    // =============================== Attributes ======================================================
    private Controller controller;
    private MainView parent;
    private JPanel form;
    private ImageComponent imageComponent;
    private JTextField nameTextbox;
    private List<JComboBox> selectList;
    private List<JTextField> textboxList;
    private Map<String, List<Object>> fieldsInfo;
    
    public static final String NO_PHOTO_MSG = "Has de seleccionar una foto.";
    public static final String WRONG_DIMENSION_MSG = "La foto ha de ser 96x96.";
    public static final String SUCCESSFUL_ADDITION_MSG = "S'ha afegit el contacte satisfactòriament";
    public static final String CONTACT_ALREADY_EXISTS_MSG = "Aquest contacte ja existeix";
    // ================================ Constructors =====================================================
    public ContactFormPanel(Controller controller, MainView parent, Contact contact) {
        this.controller = controller;
        this.parent = parent;
        selectList = new ArrayList<JComboBox>();
        textboxList = new ArrayList<JTextField>();
        fieldsInfo = new HashMap<String, List<Object>>();
        initComponents(contact);
        initFieldsInfo();
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
     * @version 5.5
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
            confirmButton.addActionListener(new ActionListener() {public void actionPerformed(ActionEvent ae) {
                controller.addContact();
            }});
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
     * @version 5.4
     */
    private void generateRegisterForm() {
        Photo photo = new Photo ( 
            new File (
                ImageFile.CONTAINER_DIRECTORY_PATH + ImageFile.DEFAULT_IMAGE_PATH
            )  
        );
        imageComponent = new ImageComponent(photo);
        imageComponent.setCursor( new Cursor(Cursor.HAND_CURSOR) );
        imageComponent.setToolTipText("Fes clic per canviar la foto");
        imageComponent.addMouseListener(new MouseAdapter(){public void mouseClicked(MouseEvent me) {
             controller.selectNewPhoto();
        }});
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
     * @version 5.4
     * @param Contact contact the contact to modify
     */
    private void generateEditionForm(Contact contact) {
        Photo photo = contact.getPhoto();
        String name = contact.getName();
        File source;
        
        if(photo == null) { // the contact hasn't photo
            source = new File(ImageFile.CONTAINER_DIRECTORY_PATH + ImageFile.DEFAULT_IMAGE_PATH);
        } else { // the contact has photo
            source = new File (
                ImageFile.CONTAINER_DIRECTORY_PATH  + 
                name                                +
                "."                                 + 
                photo.getType()
            );
        }
        photo.setSource(source);
        imageComponent = new ImageComponent(photo);
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
      * @version 5.5
      */
     public void addPhoneField() {
        JComboBox aSelect = obtainSelect();
        JTextField aTextbox = new JTextField();

        selectList.add(aSelect);
        textboxList.add(aTextbox);
        
        int index = textboxList.size() - 1;
        List<Object> valueAPhone = new ArrayList<Object>();
        valueAPhone.add("El telèfon " + (index + 1) + " no és vàlid.");
        valueAPhone.add(aTextbox);
        fieldsInfo.put(Phone.NUMBER_ATTR_OF_PHONE + index, valueAPhone);
        
        form.add(aSelect);
        form.add(aTextbox);
        
        this.validate();
     }
     /**
      * removePhoneField()
      * This procedure removes the last phone's field
      * @author Sergio Baena Lopez
      * @version 5.5
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
         
         fieldsInfo.remove(Phone.NUMBER_ATTR_OF_PHONE + index);
     }
     /**
      * read()
      * This function reads the form
      * @author Sergio Baena Lopez
      * @version 5.5
      * @return Contact the read contact
      */
     public Contact read() {
         String name = nameTextbox.getText();
         
         List<Phone> phoneList = new ArrayList<Phone>();
         for(int i = 0; i < selectList.size(); i++) {
             int aType = selectList.get(i).getSelectedIndex();
             String aNumber = textboxList.get(i).getText();
             Phone aPhone = new Phone(aType, aNumber);
             phoneList.add(aPhone);
         }
         
         Photo photo = imageComponent.getPhoto();
         
         return new Contact(name, phoneList, photo);
     }
     /**
      * selectPhoto()
      * This function affords to select a photo to the user
      * @author Sergio Baena Lopez
      * @version 5.4
      * @throws PhotoNotSelectedException if the user didn't select any photo
      * @return Photo the selected photo
      */
     public Photo selectPhoto() throws PhotoNotSelectedException {
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Imatge", "jpg", "jpeg", "png", "gif");
        fileChooser.setFileFilter(filter);
        int selection = fileChooser.showDialog(this, "Seleccionar foto");
        if(selection != JFileChooser.APPROVE_OPTION) {
            throw new PhotoNotSelectedException("The user doesn't select any photo");
        }
        return new Photo( fileChooser.getSelectedFile() ); 
     }
     /**
      * changePhoto()
      * This procedure changes the photo of the form
      * @author Sergio Baena Lopez
      * @version 5.4
      * @param Photo photo the photo to change
      */
     public void changePhoto(Photo photo) {
         imageComponent.setPhoto(photo);
     }
     /**
      * showErrors()
      * This procedure shows the errors of the form
      * @author Sergio Baena Lopez
      * @version 5.5
      * @param List<String> invalidFieldList the list of the invalid fields
      */
     public void showErrors(List<String> invalidFieldList) {
         StringBuilder errList = new StringBuilder();
         
         for(int i = 0; i < invalidFieldList.size(); i++) {
             String anInvalidField = invalidFieldList.get(i);
             List<Object> anInfo = fieldsInfo.get(anInvalidField);
             
             String anErrMsg = (String)anInfo.get(0);
             JComponent aComponent = (JComponent)anInfo.get(1);
             
             errList.append(anErrMsg);
             errList.append("\n");
             
             aComponent.setBackground(Color.RED);
             aComponent.setForeground(Color.WHITE);
         }
         
         parent.showErrorMsg( errList.toString() );
     }
     /**
      * initFieldsInfo()
      * This procedure initializes the fieldsInfo attribute which contains information about each 
      * field.
      * @author Sergio Baena Lopez
      * @version 5.5
      */
     private void initFieldsInfo() {       
        List<Object> valueName = new ArrayList<Object>();
        valueName.add("El nom està buit.");
        valueName.add(nameTextbox);
        fieldsInfo.put(Contact.NAME_ATTR_OF_CONTACT, valueName);
     }
     /**
      * clearErrors()
      * This procedure clears the errors of the form
      * @author Sergio Baena Lopez
      * @version 5.5
      */
     public void clearErrors() {
         nameTextbox.setBackground(Color.WHITE);
         nameTextbox.setForeground(Color.BLACK);
         
         for(int i = 0; i < textboxList.size(); i++) {
             textboxList.get(i).setBackground(Color.WHITE);
             textboxList.get(i).setForeground(Color.BLACK);
         }         
     }
}

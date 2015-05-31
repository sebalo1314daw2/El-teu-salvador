package el_teu_salvador.control;

import el_teu_salvador.model.Contact;
import el_teu_salvador.model.ContactList;
import el_teu_salvador.model.exceptions.ContactNotFoundException;
import el_teu_salvador.model.exceptions.NoContactSpecifiedException;
import el_teu_salvador.model.exceptions.VCFNotSelectedException;
import el_teu_salvador.model.persistence.ImageFile;
import el_teu_salvador.model.persistence.VCF;
import el_teu_salvador.view.ContactAdminPanel;
import el_teu_salvador.view.MainView;
import java.io.IOException;
import javax.swing.JCheckBox;

public class Controller {
    // ================================ Attributes =====================================================
    private MainView mainView;
    private ContactAdminPanel contactAdminPanel;
    private ContactList totalContactList;
    private ContactList partialContactList;
    private ContactList selectedContactList;
    // ================================ Constructors =====================================================
    public Controller() {
        initViews();
        selectedContactList = new ContactList();
    }
    // ================================ Methods =====================================================
    /**
     * initViews()
     * This procedure inits all the views of the application
     * @author Sergio Baena Lopez
     * @version 1.0
     */
    private void initViews() {
        mainView = new MainView(this);
    }
    /**
     * importContacts()
     * This procedure imports all the contacts from a VCF file
     * @author Sergio Baena Lopez
     * @version 3.0
     */
    public void importContacts() {
//        http://www.forosdelweb.com/f45/netbeans-java-convertir-string-imagen-898488/
//        http://descifrandosoftware.blogspot.com.es/2011/12/transformar-archivos-string-en-java.html
        try {
            VCF vcf = mainView.selectVCF();
            if( vcf.validate() ) { // VCF is valid --> We read the VCF file
                totalContactList = vcf.read();
                partialContactList = new ContactList(totalContactList); 
                ImageFile.generate(totalContactList);
                contactAdminPanel = new ContactAdminPanel(this, partialContactList);
                mainView.changeView(contactAdminPanel);
            } else { // VCF isn't valid --> we show an error message
                mainView.showErrorMsg(MainView.INVALID_VCF_MSG);
            }
        } catch(VCFNotSelectedException e) {
            // We do nothing
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * modifySelectedContactList()
     * This procedure modifies the list of selected contacts. If the user checks a checkbox, we'll
     * add the specified contact to the list. In the opposite case, if the user unchecks a checkbox,
     * we'll remove the specified contact of the list
     * @author Sergio Baena Lopez
     * @version 2.0
     * @param JCheckBox aCheckbox the checkbox which was modified
     * @param Contact aContact the contact whose checkbox had associated
     */
    public void modifySelectedContactList(JCheckBox aCheckbox, Contact aContact) {
        if( aCheckbox.isSelected() ) { // the modified checkbox was checked
            selectedContactList.add(aContact);
        } else { // the modified checkbox was unchecked
            selectedContactList.remove(aContact);
        }
    }
    /**
     * selectAllContacts()
     * This procedure selects all the contacts 
     * @author Sergio Baena Lopez
     * @version 3.0
     */
    public void selectAllContacts() {
        contactAdminPanel.checkAll();
        selectedContactList = new ContactList(partialContactList);
    }
    /**
     * deselectAllContacts()
     * This procedure deselects all the contacts 
     * @author Sergio Baena Lopez
     * @version 2.2
     */
    public void deselectAllContacts() {
        contactAdminPanel.uncheckAll();
        selectedContactList.clear();
    }
    /**
     * quitPlaceholderIfIsNecessary()
     * This procedure quits the placeholder if is it necessary
     * @author Sergio Baena Lopez
     * @version 3.0
     */
    public void quitPlaceholderIfIsNecessary() {
        contactAdminPanel.quitPlaceholderIfIsNecessary();
    }
   /**
    * putPlaceholderIfIsNecessary()
    * This procedure puts the placeholder if is it necessary
    * @author Sergio Baena Lopez
    * @version 3.0
    */
    public void putPlaceholderIfIsNecessary() {
        contactAdminPanel.putPlaceholderIfIsNecessary();
    }
    /**
     * findContacts()
     * This procedure finds the read contacts in the total list of contacts and it'll set
     * the new partial list of contacts 
     * @author Sergio Baena Lopez
     * @version 4.0
     */
    public void findContacts() {
        try {
            Contact readContact = contactAdminPanel.readSearchEngine();
            partialContactList = totalContactList.find(readContact);
            updateContactTable();
        } catch(ContactNotFoundException e) {
            mainView.showErrorMsg(ContactAdminPanel.CONTACT_NOT_FOUND_MSG);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * listAllContacts()
     * This procedure lists all the contacts
     * @author Sergio Baena Lopez
     * @version 4.0
     */
    public void listAllContacts() {
        try {
            partialContactList = new ContactList(totalContactList);
            updateContactTable();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * removeContacts()
     * This procedure removes all the selected contacts
     * @author Sergio Baena Lopez
     * @version 4.0
     */
    public void removeContacts() {
        try {
            partialContactList.remove(selectedContactList);
            totalContactList.remove(selectedContactList);
            
            updateContactTable();
            
            mainView.showSuccessMsg(ContactAdminPanel.SUCCESSFUL_REMOVING_MSG);
        } catch(NoContactSpecifiedException e) {
            mainView.showErrorMsg(ContactAdminPanel.NO_CONTACT_SELECTED_MSG);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * updateContactTable()
     * This procedure updates the contact table
     * @author Sergio Baena Lopez
     * @version 4.0
     */
    private void updateContactTable() throws IOException {
        selectedContactList.clear();
        contactAdminPanel.generateTable(partialContactList);
    }
}
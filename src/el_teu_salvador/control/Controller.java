package el_teu_salvador.control;

import el_teu_salvador.model.Contact;
import el_teu_salvador.model.exceptions.VCFNotSelectedException;
import el_teu_salvador.model.persistence.ImageFile;
import el_teu_salvador.model.persistence.VCF;
import el_teu_salvador.view.ContactAdminPanel;
import el_teu_salvador.view.MainView;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JCheckBox;

public class Controller {
    // ================================ Attributes =====================================================
    private MainView mainView;
    private ContactAdminPanel contactAdminPanel;
    private List<Contact> contactList;
    private List<Contact> selectedContactList;
    // ================================ Constructors =====================================================
    public Controller() {
        initViews();
        selectedContactList = new ArrayList<Contact>();
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
     * @version 1.0
     */
    public void importContacts() {
//        http://www.forosdelweb.com/f45/netbeans-java-convertir-string-imagen-898488/
//        http://descifrandosoftware.blogspot.com.es/2011/12/transformar-archivos-string-en-java.html
        try {
            VCF vcf = mainView.selectVCF();
            if( vcf.validate() ) { // VCF is valid --> We read the VCF file
                contactList = vcf.read();
                ImageFile.generate(contactList);
                contactAdminPanel = new ContactAdminPanel(this, contactList);
                mainView.changeView(contactAdminPanel);
            } else { // VCF isn't valid --> we show an error message
                mainView.showErrorMsg(MainView.INVALID_VCF);
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
     * @version 2.1
     */
    public void selectAllContacts() {
        contactAdminPanel.checkAll();
        selectedContactList = new ArrayList<Contact>(contactList);
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
}

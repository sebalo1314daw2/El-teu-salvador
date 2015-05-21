package el_teu_salvador.control;

import el_teu_salvador.model.Contact;
import el_teu_salvador.model.exceptions.VCFCorruptedException;
import el_teu_salvador.model.exceptions.VCFNotSelectedException;
import el_teu_salvador.model.persistence.ImageFile;
import el_teu_salvador.model.persistence.VCF;
import el_teu_salvador.view.ContactAdminPanel;
import el_teu_salvador.view.MainView;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.StringTokenizer;
import javax.swing.JCheckBox;

public class Controller {
    // ================================ Attributes =====================================================
    private MainView mainView;
    private List<Contact> contactList;
    // ================================ Constructors =====================================================
    public Controller() {
        initViews();
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
                mainView.changeView( new ContactAdminPanel(this, contactList) );
            } else { // VCF isn't valid --> we show an error message
                mainView.showErrorMsg(MainView.INVALID_VCF);
            }
        } catch(VCFNotSelectedException e) {
            // We do nothing
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void modifySelectedContactList(JCheckBox aCheckbox, Contact aContact) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
}

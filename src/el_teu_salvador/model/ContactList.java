package el_teu_salvador.model;

import el_teu_salvador.model.exceptions.ContactNotFoundException;
import java.util.ArrayList;
import java.util.Collection;

public class ContactList extends ArrayList<Contact> {
    // ================================ Constructors =====================================================
    public ContactList() {
    }

    public ContactList(Collection<? extends Contact> clctn) {
        super(clctn);
    }   
    // ================================ Methods =====================================================
    public ContactList find(Contact contact) throws ContactNotFoundException {
        String nameToFind = contact.getName().toLowerCase();
        ContactList foundContactList = new ContactList();
        if( nameToFind.equals("") ) {
            foundContactList = new ContactList(this);
        } else {
            for(int i = 0; i < size(); i++) {
                Contact aContact = get(i);
                String aName = aContact.getName().toLowerCase();
                if( aName.indexOf(nameToFind) != -1) { // the name was found
                    foundContactList.add(aContact);
                }
            }
            if( foundContactList.isEmpty() ) { // we haven't found any contact 
                throw new ContactNotFoundException("The contact " + contact + " wasn't found");
            }
        }
        return foundContactList;
    }
}

package el_teu_salvador.model;

import el_teu_salvador.model.exceptions.ContactNotFoundException;
import el_teu_salvador.model.exceptions.NoContactSpecifiedException;
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
    /**
     * find()
     * This function searches the specified contact in the list
     * @author Sergio Baena Lopez
     * @version 4.0
     * @throws ContactNotFoundException if the contact wasn't found
     * @param Contact contact the contact to search
     * @return ContactList the list of found contacts 
     */
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
    /**
     * remove()
     * This procedure removes all the specified contacts from the list
     * @author Sergio Baena Lopez
     * @version 4.0
     * @throws NoContactSpecifiedException if no contact was specified (the specified list is empty)
     * @param ContactList contactList the list of contacts to remove
     */
    public void remove(ContactList contactList) throws NoContactSpecifiedException {
        if( contactList.isEmpty() ) {
            throw new NoContactSpecifiedException("No contact was specified to remove");
        }
        for(int i = 0; i < contactList.size(); i++) {
            this.remove( contactList.get(i) );
        }
    }
}

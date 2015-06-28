package el_teu_salvador.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Contact {
    // ================================ Attributes =====================================================
    private String name;
    private List<Phone> phoneList;
    private Photo photo;
    
    public static final String NAME_ATTR_OF_CONTACT = "name";
    // ================================ Constructors =====================================================
    public Contact() {
        phoneList = new ArrayList<Phone>();
    }

    public Contact(String name) {
        this.name = name;
    }

    public Contact(String name, List<Phone> phoneList, Photo photo) {
        this.name = name;
        this.phoneList = phoneList;
        this.photo = photo;
    }
    // ================================ Accessors =====================================================
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Phone> getPhoneList() {
        return phoneList;
    }

    public void setPhoneList(List<Phone> phoneList) {
        this.phoneList = phoneList;
    }

    public Photo getPhoto() {
        return photo;
    }

    public void setPhoto(Photo photo) {
        this.photo = photo;
    }

    // ================================ Methods =====================================================
    @Override
    public String toString() {
        return "Contact{" + "name=" + name + ", phoneList=" + phoneList + ", photo=" + photo + "}\n";
    }
    /**
     * validate()
     * This function validates the contact
     * @author Sergio Baena Lopez
     * @version 5.5
     * @return List<String> the list of the invalid attributes
     */
    public List<String> validate() {
        List<String> invalidAttrList = new ArrayList<String>();
        
        // Validating the name attribute
        if( !Utilities.isValidText(name) ) { 
            invalidAttrList.add(NAME_ATTR_OF_CONTACT);
        }
        // Validating the phoneList attribute
        for(int i = 0; i < phoneList.size(); i++) {
            invalidAttrList.addAll( phoneList.get(i).validate(i) );
        }
        
        return invalidAttrList;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Contact other = (Contact) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        return true;
    }
    
}

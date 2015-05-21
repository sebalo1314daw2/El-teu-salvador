package el_teu_salvador.model;

import java.util.ArrayList;
import java.util.List;

public class Contact {
    // ================================ Attributes =====================================================
    private String name;
    private List<Phone> phoneList;
    private Photo photo;

    // ================================ Constructors =====================================================
    public Contact() {
        phoneList = new ArrayList<Phone>();
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
}

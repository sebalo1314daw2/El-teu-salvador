package el_teu_salvador.model;

import static el_teu_salvador.model.Contact.NAME_ATTR_OF_CONTACT;
import java.util.ArrayList;
import java.util.List;

public class Phone {
    // ================================ Attributes =====================================================
    private int type;
    private String number;
    
    public static final int MOBILE = 0;
    public static final int LANDLINE = 1;
    
    public static final String NUMBER_ATTR_OF_PHONE = "number";
    // ================================ Constructors =====================================================
    public Phone() {}

    public Phone(int type) {
        this.type = type;
    }

    public Phone(int type, String number) {
        this.type = type;
        this.number = number;
    }
    // ================================ Accessors =====================================================
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number.replaceAll("[ ]+", "");
    }

    // ================================ Methods =====================================================
    @Override
    public String toString() {
        return "Phone{" + "type=" + type + ", number=" + number + '}';
    }
     /**
     * validate()
     * This function validates this phone
     * @author Sergio Baena Lopez
     * @version 5.5
     * @param int index the index of this phone
     * @return List<String> the list of the invalid attributes
     */
    public List<String> validate(int index) {
        List<String> invalidAttrList = new ArrayList<String>();

        // Validating the number attribute 
        if( !Utilities.isValidPhone(number) ) { 
            invalidAttrList.add(NUMBER_ATTR_OF_PHONE + index);
        }

        return invalidAttrList;
    }
}

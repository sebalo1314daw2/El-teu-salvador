package el_teu_salvador.model;

public class Phone {
    // ================================ Attributes =====================================================
    private int type;
    private String number;
    
    public static final int MOBILE = 0;
    public static final int LANDLINE = 1;
    // ================================ Constructors =====================================================
    public Phone() {}

    public Phone(int type) {
        this.type = type;
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
        this.number = number;
    }

    // ================================ Methods =====================================================
    @Override
    public String toString() {
        return "Phone{" + "type=" + type + ", number=" + number + '}';
    }
}

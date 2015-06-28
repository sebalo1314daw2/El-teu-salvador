package el_teu_salvador.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utilities {
    /**
     * compareRegExpWithString()
     * This function compares the specified string with the specified regular expession
     * @author Sergio Baena Lopez
     * @version 5.5
     * @param String regExp the regular expression
     * @param String string the string to compare
     * @return boolean if the string matches with the regular expression
     */
    public static boolean compareRegExpWithString(String regExp, String string) {
        Pattern regExpObject = Pattern.compile(regExp);
        Matcher matcher =  regExpObject.matcher(string);
        return matcher.matches();
    }
    /**
     * isValidText()
     * This function indicates if the specified text is valid or not. We consider invalid if 
     * the text is empty string or only spaces
     * @author Sergio Baena Lopez
     * @version 5.5
     * @param String text the text to check
     * @return boolean if the specified text is valid or not
     */
    public static boolean isValidText(String text) {
        return !compareRegExpWithString("^[ ]*$", text);
    }
    /**
     * isValidPhone()
     * This function indicates if the specified phone is valid or not. We consider a valid phone 
     * when it's (+nn(n))nnnnnnnnn.
     * @author Sergio Baena Lopez
     * @version 5.5
     * @param String phone the phone to check
     * @return boolean if the specified phone is valid or not
     */
    public static boolean isValidPhone(String phone) {
        return compareRegExpWithString("^([+][0-9]{2,3})?[0-9]{9}$", phone);
    }
}

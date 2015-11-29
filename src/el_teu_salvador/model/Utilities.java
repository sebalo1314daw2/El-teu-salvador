package el_teu_salvador.model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utilities {
    // ================================ Constants =====================================================
    public static final int OK = 0;
    public static final int NOT_EXISTED = 1;
    public static final int CANNOT_WRITE = 2;
    // ================================ Static methods =====================================================
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
    /**
     * validateDirectory()
     * This function validates the specified directory. It checks if the specified directory exists
     * (NOT_EXISTED) and if we can write in it (CANNOT_WRITE).
     * @author Sergio Baena Lopez
     * @version 9.0
     * @param File directory the directory to validate
     * @return int if the directory is valid (OK) or not (NOT_EXISTED or CANNOT_WRITE)
     */
    public static int validateDirectory(File directory) {
        int valid = Utilities.OK;
        
        if( !directory.exists() ) {
            valid = Utilities.NOT_EXISTED;
        } else if( !directory.canWrite() ) {
            valid = Utilities.CANNOT_WRITE;
        }
        
        return valid;
    }
    /**
     * obtainTokenList()
     * This function obtains a list of tokens from the specified string tokenezer
     * @author Sergio Baena Lopez
     * @version 9.0
     * @param StringTokenizer tokenizer the string tokenizer which we obtain a list of tokens from
     * @return List<String> the list of obtained tokens from the specified string tokenezer
     */
    public static List<String> obtainTokenList(StringTokenizer tokenizer) {
        List<String> tokenList = new ArrayList<String>();
        
        while( tokenizer.hasMoreTokens() ) {
            tokenList.add( tokenizer.nextToken() );
        }
        
        return tokenList;
    }
    /**
     * removeDoubleSpaces()
     * This function removes the double spaces of the specified text
     * @author Sergio Baena Lopez
     * @version 9.0
     * @param String txt the text to remove its double spaces
     * @return String the text with the removed double spaces
     */
    public static String removeDoubleSpaces(String txt) {
        // Start and end --> All the spaces
        txt = txt.trim();
        // Middle --> A space will only stay 
        txt = txt.replaceAll("[ ]{2,}", " ");
        
        return txt;
    }
}
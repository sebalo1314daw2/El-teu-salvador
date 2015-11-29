package el_teu_salvador.model.persistence;

import el_teu_salvador.model.Contact;
import el_teu_salvador.model.ContactList;
import el_teu_salvador.model.Phone;
import el_teu_salvador.model.Photo;
import el_teu_salvador.model.Utilities;
import el_teu_salvador.model.exceptions.VCFCorruptedException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.StringTokenizer;
import javax.swing.filechooser.FileNameExtensionFilter;

public class VCF {
    // ================================ Attributes =====================================================
    private File source;
    private String version;
    
    private static final String BEGINNING = "BEGIN:VCARD";
    private static final String VERSION_KEY = "VERSION";
    private static final String [] AVALIABLE_VERSIONS_LIST = {"2.1", "3.0", "4.0"};
    private static final String NAME_WITHOUT_FORMAT_KEY = "N"; 
    private static final String NAME_KEY = "FN";
    private static final String PHONE_KEY = "TEL";
    private static final String PHOTO_KEY = "PHOTO";
    private static final String END_KEY = "END";
    private static final String MOBILE = "CELL";
    private static final String LANDLINE = "HOME";
    private static final String ENCODING = "ENCODING";
    private static final String VCARD = "VCARD";
    private static final String PRIMARY_DELIMETER = ":";
    private static final String SECONDARY_DELIMETER = ";";
    private static final String TERTIARY_DELIMETER = "=";
    
    private static final String DEFAULT_FILENAME = "Els-teus-contactes.vcf";
    private static final int MAX_CHARS_IN_PHOTO_LINE = 74;
    // ================================ Constructors =====================================================
    public VCF(File source) {
        this.source = source;
    }

    // ================================ Accessors =====================================================
    public File getSource() {
        return source;
    }

    public void setSource(File source) {
        this.source = source;
    }

    public String getVersion() {
        return version;
    }
    // ================================ Methods =====================================================
    /**
     * validate()
     * This function validates the VCF file. a VCF file is considered valid when its extension is
     * "vcf".
     * @author Sergio Baena Lopez
     * @version 1.0
     * @return boolean if the VCF file is valid or not
     */
    public boolean validate() {
        boolean valid = false;
        if( !source.isDirectory() ) { // VCF isn't directory
            FileNameExtensionFilter filter = new FileNameExtensionFilter("VCF", "vcf");
            valid = filter.accept(source);
        }
        return valid;
    }
    /**
     * read()
     * This function reads the VCF. 
     * @author Sergio Baena Lopez
     * @version 3.0
     * @throws VCFCorruptedException if the VCF file is corrupted
     * @return ContactList the got list of contacts
     */
    public ContactList read() throws FileNotFoundException, IOException, VCFCorruptedException {
        BufferedReader file = new BufferedReader( new FileReader(source) );
        
        for(int i = 0; i < 2; i++) {
            String readLine = file.readLine();
            if(readLine != null) { // we've read a line
                switch(i) {
                    case 0: // We've read the first line --> We expect BEGIN:VCARD 
                        if( !readLine.equals(BEGINNING) ) { // VCF is corrupted
                            throw new VCFCorruptedException(BEGINNING + " wasn't found");
                        }
                        break;
                    case 1: // We've read the second line --> We expect VERSION:<version>
                        StringTokenizer tokenizer = new StringTokenizer(readLine, PRIMARY_DELIMETER);
                        if( tokenizer.countTokens() != 2) { // VCF is corrupted
                            throw new VCFCorruptedException("VERSION:<version> wasn't found");
                        }
                        if( !tokenizer.nextToken().equals(VERSION_KEY) ) { // VCF is corrupted
                             throw new VCFCorruptedException("The key VERSION wasn't found");
                        }
                        String version = tokenizer.nextToken();
                        boolean wasFound =  false;
                        for(int j = 0; j < AVALIABLE_VERSIONS_LIST.length; j++) {
                            if( version.equals( AVALIABLE_VERSIONS_LIST[j] ) ) { // The version was found
                                wasFound = true;
                                this.version = version;
                            }
                        }
                        if(!wasFound) { // VCF is corrupted
                            throw new VCFCorruptedException("The version isn't right");
                        }
                        break;
                }
            } else { // we've read the EOF --> the VCF is corrupted
               throw new VCFCorruptedException("EOF was found");
            }
        }
        // We've already read all which we want       
        // We've to close the file and we've to call the method read which is appropiated
        file.close();
        ContactList contactList = null; 
        switch(version) {
            case "2.1":
                contactList = read2_1();
                break;
            case "3.0":
                contactList = read3_0();
                break;
            case "4.0":
                contactList = read4_0();
                break;
        }
        return contactList;
    }
    /**
     * read2_1()
     * This function reads the VCF with the 2.1 version 
     * @author Sergio Baena Lopez
     * @version 3.0
     * @throws VCFCorruptedException if the VCF file is corrupted
     * @return ContactList the got list of contacts
     */
    private ContactList read2_1() throws FileNotFoundException, IOException, VCFCorruptedException {
        BufferedReader file = new BufferedReader( new FileReader(source) );
        ContactList contactList = new ContactList();
        boolean isEOF = false;
        int i = 0;
        String readLine = null;
        boolean isReadingPhoto = false;
        while(!isEOF) { // We read all the VCards
            boolean isEndVCard = false;
            int j = 0;
            Contact aContact = new Contact();
            while(!isEndVCard) { // We read a VCard
                if(j != 0 || i == 0) { 
                    readLine = file.readLine();
                    i++;
                }
                if(readLine != null) { // we've read a line
                    StringTokenizer tokenizer;
                    switch(j) {
                        case 0: // We've read the first line --> We expect BEGIN:VCARD 
                            if( !readLine.equals(BEGINNING) ) { // VCF is corrupted
                                throw new VCFCorruptedException("Line "+ i + ": " + BEGINNING + " wasn't found");
                            }
                            break;
                        case 1: // We've read the second line --> We expect VERSION:<version>
                            tokenizer = new StringTokenizer(readLine, PRIMARY_DELIMETER);
                            if( tokenizer.countTokens() != 2) { // VCF is corrupted
                                throw new VCFCorruptedException("Line "+ i + ": " + "VERSION:<version> wasn't found");
                            }
                            if( !tokenizer.nextToken().equals(VERSION_KEY) ) { // VCF is corrupted
                                 throw new VCFCorruptedException("Line "+ i + ": " + "The key VERSION wasn't found");
                            }
                            String version = tokenizer.nextToken();
                            if( !version.equals(this.version) ) { // VCF is corrupted
                                throw new VCFCorruptedException("Line "+ i + ": " + "The found version isn't right");
                            }
                            break;
                        default: // We've read another line
                            if(!isReadingPhoto) {
                                tokenizer = new StringTokenizer(readLine, PRIMARY_DELIMETER);
                                if( tokenizer.countTokens() != 2) { // VCF is corrupted
                                    throw new VCFCorruptedException("Line "+ i + ": " + "The VCF file doesn't meet the criteria the rule key-value");
                                }
                                StringTokenizer keyTokenizer = new StringTokenizer(tokenizer.nextToken(), SECONDARY_DELIMETER);
                                String key =  keyTokenizer.nextToken();
                                String value = tokenizer.nextToken();                            
                                // We search the keys which we want
                                switch(key) {
                                    case NAME_KEY: // Setting name of the contact
                                        aContact.setName(value);
                                        break;
                                    case PHONE_KEY: // Setting phone of the contact
                                        Phone aPhone = new Phone();
                                        String typePhoneString = keyTokenizer.nextToken();
                                        switch(typePhoneString) {
                                            case MOBILE:
                                                aPhone.setType(Phone.MOBILE);
                                                break;
                                            case LANDLINE:
                                                aPhone.setType(Phone.LANDLINE);
                                                break;
                                        }
                                        aPhone.setNumber(value);
                                        aContact.getPhoneList().add(aPhone);
                                        break;
                                    case PHOTO_KEY: // Setting photo of the contact
                                        // This is the reading of the first line
                                        isReadingPhoto = true;
                                        Photo aPhoto = new Photo();
                                        StringTokenizer encodingTokenizer = new StringTokenizer (
                                            keyTokenizer.nextToken(), TERTIARY_DELIMETER
                                        );
                                        if( !encodingTokenizer.nextToken().equals(ENCODING) ) { // VCF is corrupted
                                            throw new VCFCorruptedException("Line "+ i + ": " + "The photo's encoding was found");
                                        }
                                        aPhoto.setEncoding( encodingTokenizer.nextToken() );
                                        aPhoto.setType( keyTokenizer.nextToken() );
                                        aPhoto.setContent(value);
                                        aContact.setPhoto(aPhoto);
                                        break;
                                    case END_KEY: // We've just finished a VCard
                                        if( !value.equals(VCARD) ) { // VCF is corrupted
                                            throw new VCFCorruptedException("Line "+ i + ": " + "The closing of the VCard isn't right");
                                        }
                                        isEndVCard = true;
                                        break;
                                }
                            } else { // We are reading a photo (It isn't the first line)
                                if( !readLine.equals("") ) { // readLine is more content of the photo
                                    aContact.getPhoto().addContent(readLine);
                                } else { // We've already finished the reading of the photo
                                    isReadingPhoto = false;
                                }
                            }
                            break;
                    }
                } else { // we've read the EOF --> the VCF is corrupted
                   throw new VCFCorruptedException("Line "+ i + ": " + "EOF was found");
                }
                j++;
            }
            // We add the contact to the list
            contactList.add(aContact);
            // We check if the next line is a EOF
            readLine = file.readLine();
            i++;
            if(readLine == null) { // The read line is a EOF
                isEOF = true;
            }
        }
        return contactList;
    }
    /**
     * read3_0()
     * This function reads the VCF with the 3.0 version 
     * @author Sergio Baena Lopez
     * @version 3.0
     * @throws VCFCorruptedException if the VCF file is corrupted
     * @return ContactList the got list of contacts
     */
    private ContactList read3_0() {
        return null; // TODO
    }
    /**
     * read4_0()
     * This function reads the VCF with the 4.0 version 
     * @author Sergio Baena Lopez
     * @version 3.0
     * @throws VCFCorruptedException if the VCF file is corrupted
     * @return ContactList the got list of contacts
     */
    private ContactList read4_0() {
        return null; // TODO
    }
    /**
     * generate2_1()
     * This procedure generates an VCF file with the specified contacts in the specified directory (version 2.1)
     * @author Sergio Baena Lopez
     * @version 9.0
     * @param ContactList contactList the contacts who will be written in VCF file
     * @param File directory the directory where we'll generate the VCF file
     */
    public static void generate2_1(ContactList contactList, File directory) throws IOException {
        BufferedWriter file = new BufferedWriter( new FileWriter( new File(directory, DEFAULT_FILENAME) ) );
        
        for(int i = 0; i < contactList.size(); i++) { // A contact
            Contact contact = contactList.get(i);
            // Beginning
            file.write(BEGINNING);
            file.newLine();
            // Version
            file.write(VERSION_KEY + PRIMARY_DELIMETER + AVALIABLE_VERSIONS_LIST[0]);
            file.newLine();
            // Contact's name 
            if(contact.getName() != null) {
                file.write( NAME_WITHOUT_FORMAT_KEY + PRIMARY_DELIMETER + obtainNameWithoutFormat(contact) );
                file.newLine();
                file.write( NAME_KEY + PRIMARY_DELIMETER + contact.getName() );
                file.newLine();
            }
            // List of phones of contact
            List<Phone> phoneList = contact.getPhoneList();
            for(int j = 0; j < phoneList.size(); j++) { // A phone
                Phone phone = phoneList.get(j);
                String aLine = PHONE_KEY + SECONDARY_DELIMETER;
                switch( phone.getType() ) {
                    case Phone.MOBILE:
                        aLine += MOBILE;
                        break;
                    case Phone.LANDLINE:
                        aLine += LANDLINE;
                        break;
                }
                aLine += PRIMARY_DELIMETER + phone.getNumber();
                file.write(aLine);
                file.newLine();
            }
            // Contact's photo
            Photo photo = contact.getPhoto();
            if(photo != null) {
                String someLine = 
                    PHOTO_KEY + 
                    SECONDARY_DELIMETER + 
                    ENCODING + 
                    TERTIARY_DELIMETER +
                    photo.getEncoding() +
                    SECONDARY_DELIMETER +
                    photo.getType() +
                    PRIMARY_DELIMETER;
                int initiationIndexFirstLine = someLine.length();
                someLine += tabPhotoContent(photo, initiationIndexFirstLine);
                file.write(someLine);
                file.newLine();
            }
            // End
            file.write(END_KEY + PRIMARY_DELIMETER + VCARD);
            file.newLine();
        } // We've already written all the contacts
        file.close();
    }
    /**
     * obtainNameWithoutFormat()
     * This function obtains the name without format from the specified contact
     * @author Sergio Baena Lopez
     * @version 9.0
     * @param Contact contact the contact where we'll obtain the name without format from.
     * @return String the name without format
     */
    private static String obtainNameWithoutFormat(Contact contact) {
        String nameWithoutFormat;
        String nameWithFormat = Utilities.removeDoubleSpaces( contact.getName() );
        StringTokenizer nameWithFormatTokenizer = new StringTokenizer(nameWithFormat, " ");
        switch( nameWithFormatTokenizer.countTokens() ) {
            case 1:
                nameWithoutFormat = obtainNameWithoutFormat( nameWithFormatTokenizer.nextToken() );
                break;
            case 2:
                nameWithoutFormat = obtainNameWithoutFormat ( 
                    nameWithFormatTokenizer.nextToken(), 
                    nameWithFormatTokenizer.nextToken() 
                );
                break;
            case 3:
                nameWithoutFormat = obtainNameWithoutFormat ( 
                        nameWithFormatTokenizer.nextToken(), 
                        nameWithFormatTokenizer.nextToken(),
                        nameWithFormatTokenizer.nextToken() 
                );
                break;
            default:
                List<String> wordList = Utilities.obtainTokenList(nameWithFormatTokenizer);
                nameWithoutFormat = obtainNameWithoutFormat(wordList);
                break;
        }
        return nameWithoutFormat;
    }
    /**
     * obtainNameWithoutFormat()
     * This function obtains the name without format from the specified three words
     * @author Sergio Baena Lopez
     * @version 9.0
     * @param String word1 the first word
     * @param String word2 the second word
     * @param String word3 the third word
     * @return String the name without format
     */
    private static String obtainNameWithoutFormat(String word1, String word2, String word3) {
        return  word3                   + 
                SECONDARY_DELIMETER     +
                word1                   + 
                SECONDARY_DELIMETER     + 
                word2                   + 
                SECONDARY_DELIMETER     + 
                SECONDARY_DELIMETER;
    }
    /**
     * obtainNameWithoutFormat()
     * This function obtains the name without format from the specified word
     * @author Sergio Baena Lopez
     * @version 9.0
     * @param String word the word
     * @return String the name without format
     */
    private static String obtainNameWithoutFormat(String word) {
        return obtainNameWithoutFormat(word, "", "");
    }
   /**
    * obtainNameWithoutFormat()
    * This function obtains the name without format from the specified two words
    * @author Sergio Baena Lopez
    * @version 9.0
    * @param String word1 the first word
    * @param String word2 the second word
    * @return String the name without format
    */
    private static String obtainNameWithoutFormat(String word1, String word2) {
         return obtainNameWithoutFormat(word1, "", word2);
    }
   /**
    * obtainNameWithoutFormat()
    * This function obtains the name without format from the specified four words or more (it's contained in
    * a list)
    * @author Sergio Baena Lopez
    * @version 9.0
    * @param List<String> wordList the list of words where we obtain the name without format from
    * @return String the name without format
    */
    private static String obtainNameWithoutFormat(List<String> wordList) {
        String word1 = "";
        int numWordsToConcatenate = wordList.size() - 2;
        for(int i = 0; i < numWordsToConcatenate; i++) {
            word1 += wordList.get(i) + " ";
        }
        word1 = word1.trim();
        
        int nextToLastIndex = wordList.size() - 2;
        int lastIndex = wordList.size() - 1;

        String word2 = wordList.get(nextToLastIndex);
        String word3 = wordList.get(lastIndex);
        
        return obtainNameWithoutFormat(word1, word2, word3);
    }
    /**
     * tabPhotoContent()
     * This function tabulates the content of the specified photo. It's necessary for its persistence in VCF.
     * @author Sergio Baena Lopez
     * @version 9.0
     * @param Photo photo the photo which content we want to tabulate
     * @param int initiationIndexFirstLine the initiation's index of the first line of the content of the photo
     * @return String the tabulated content of the photo
     */
    private static String tabPhotoContent(Photo photo, int initiationIndexFirstLine) {
        StringBuilder tabulatedContentBuilder = new StringBuilder();
        String content = photo.getContent();
        int contentLength = content.length();
        // Getting first line
        int j = MAX_CHARS_IN_PHOTO_LINE - initiationIndexFirstLine; // exclusive
        tabulatedContentBuilder.append( content.substring(0, j) );
        tabulatedContentBuilder.append("\n");
        // Getting another lines
        int i = j;
        j = i + MAX_CHARS_IN_PHOTO_LINE - 1;
        
        while(j <= contentLength) {
            tabulatedContentBuilder.append(" ");
            tabulatedContentBuilder.append( content.substring(i, j) );
            tabulatedContentBuilder.append("\n");
            
            i = j;
            j = i + MAX_CHARS_IN_PHOTO_LINE - 1;
        }
        
        if(i != contentLength) { // "i" doesn't point to a position more which the last character has 
            // There's something yet. It's a line which isn't completed
            tabulatedContentBuilder.append(" ");
            tabulatedContentBuilder.append( content.substring(i) );
            tabulatedContentBuilder.append("\n");
        }
        
        return tabulatedContentBuilder.toString();
    }
}

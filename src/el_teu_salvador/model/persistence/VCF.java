package el_teu_salvador.model.persistence;

import el_teu_salvador.model.Contact;
import el_teu_salvador.model.Phone;
import el_teu_salvador.model.Photo;
import el_teu_salvador.model.exceptions.VCFCorruptedException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
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
     * @version 1.0
     * @throws VCFCorruptedException if the VCF file is corrupted
     * @return List<Contact> the got list of contacts
     */
    public List<Contact> read() throws FileNotFoundException, IOException, VCFCorruptedException {
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
        List<Contact> contactList = null; 
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
     * @version 1.0
     * @throws VCFCorruptedException if the VCF file is corrupted
     * @return List<Contact> the got list of contacts
     */
    private List<Contact> read2_1() throws FileNotFoundException, IOException, VCFCorruptedException {
        BufferedReader file = new BufferedReader( new FileReader(source) );
        List<Contact> contactList = new ArrayList<Contact>();
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
     * @version 1.0
     * @throws VCFCorruptedException if the VCF file is corrupted
     * @return List<Contact> the got list of contacts
     */
    private List<Contact> read3_0() {
        return null; // TODO
    }
    /**
     * read4_0()
     * This function reads the VCF with the 4.0 version 
     * @author Sergio Baena Lopez
     * @version 1.0
     * @throws VCFCorruptedException if the VCF file is corrupted
     * @return List<Contact> the got list of contacts
     */
    private List<Contact> read4_0() {
        return null; // TODO
    }
}

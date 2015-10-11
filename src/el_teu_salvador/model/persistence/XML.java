package el_teu_salvador.model.persistence;

import el_teu_salvador.model.Contact;
import el_teu_salvador.model.ContactList;
import el_teu_salvador.model.Phone;
import el_teu_salvador.model.Photo;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

public class XML extends DefaultHandler {
    // ================================ Attributes =====================================================
    private static final String CONTAINER_DIRECTORY_PATH = "xml/";
    private static final String PATH = "contact-list.xml";
    
    private static final String VERSION = "1.0";
    
    private static final String CONTACT_LIST_TAG = "contact-list";
    private static final String CONTACT_TAG = "contact";
    private static final String NAME_TAG = "name";
    private static final String PHONE_LIST_TAG = "phone-list";
    private static final String PHONE_TAG = "phone";
    private static final String TYPE_TAG = "type";
    private static final String NUMBER_TAG = "number";
    private static final String PHOTO_TAG = "photo";
    private static final String ENCODING_TAG = "encoding";
    private static final String CONTENT_TAG = "content";
    
    private ContactList contactList;
    private String readText;
    private Contact aContact;
    private Phone aPhone;
    private Photo aPhoto;
    private boolean readingPhoneList;
    // ================================ Static methods =====================================================
    /**
     * generate()
     * This procedure generates a XML document which stores the specified list of contacts
     * @author Sergio Baena Lopez
     * @version 7.0
     * @param ContactList contactList the list of contacts to solve
     */
    public static void generate(ContactList contactList) 
            throws ParserConfigurationException, TransformerConfigurationException, TransformerException {
        // First, we create the document XML with its root element
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        DOMImplementation implementation = builder.getDOMImplementation();
        Document document = implementation.createDocument(null, CONTACT_LIST_TAG, null);
        document.setXmlVersion(VERSION);
        
        Element contactListTag = document.getDocumentElement();
        
        // Then, we create all the contact tags
        if( contactList.isEmpty() ) {
            Text contactListTagValue = document.createTextNode(" ");
            contactListTag.appendChild(contactListTagValue);
        } else {
            for(int i = 0; i < contactList.size(); i++) {
                Contact contact = contactList.get(i);
                List<Phone> phoneList = contact.getPhoneList();
                Photo photo = contact.getPhoto();

                Element contactTag = document.createElement(CONTACT_TAG);

                String nameContact = contact.getName();
                if(nameContact != null) {
                    Element nameTag = document.createElement(NAME_TAG);
                    Text nameTagValue = document.createTextNode(nameContact);
                    nameTag.appendChild(nameTagValue);
                    contactTag.appendChild(nameTag);
                }

                Element phoneListTag = obtainPhoneListTag(phoneList, document);
                contactTag.appendChild(phoneListTag);

                if(photo != null) {
                    Element photoTag = obtainPhotoTag(photo, document);
                    contactTag.appendChild(photoTag);
                }

                contactListTag.appendChild(contactTag);
            } // We've already made the document
        }
        // Finally, we generate the XML file
        Source source = new DOMSource(document);
        Result result = new StreamResult( new File(CONTAINER_DIRECTORY_PATH + PATH) );
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.transform(source, result);
    }
    /**
     * obtainPhoneListTag()
     * This procedure obtains the phone's list tag from the specified list of phones
     * @author Sergio Baena Lopez
     * @version 7.0
     * @param List<Phone> phoneList the list of phones which we need to obtain the phone's list tag
     * @param Document document the XML document
     * @return Element the phone's list tag from the specified list of phones
     */
    private static Element obtainPhoneListTag(List<Phone> phoneList, Document document) {
        Element phoneListTag = document.createElement(PHONE_LIST_TAG);
        
        if( phoneList.isEmpty() ) {
            Text phoneListTagValue = document.createTextNode(" ");
            phoneListTag.appendChild(phoneListTagValue);
        } else {
            for(int j = 0; j < phoneList.size(); j++) {
                Phone phone = phoneList.get(j);

                Element phoneTag = document.createElement(PHONE_TAG);

                Element typeTag = document.createElement(TYPE_TAG);
                Text typeTagValue = document.createTextNode( String.valueOf( phone.getType() ) );
                typeTag.appendChild(typeTagValue);
                phoneTag.appendChild(typeTag);

                Element numberTag = document.createElement(NUMBER_TAG);
                Text numberTagValue = document.createTextNode( phone.getNumber() );
                numberTag.appendChild(numberTagValue);
                phoneTag.appendChild(numberTag);

                phoneListTag.appendChild(phoneTag);
            }
        }
        
        return phoneListTag;
    }
    /**
     * obtainPhotoTag()
     * This procedure obtains the photo tag from the specified photo
     * @author Sergio Baena Lopez
     * @version 7.0
     * @param Photo photo the photo which we need to obtain the photo tag
     * @param Document document the XML document
     * @return Element the photo tag from the specified photo
     */
    private static Element obtainPhotoTag(Photo photo, Document document) {
        Element photoTag = document.createElement(PHOTO_TAG);
        
        Element encodingTag = document.createElement(ENCODING_TAG);
        Text encodingTagValue = document.createTextNode( photo.getEncoding() );
        encodingTag.appendChild(encodingTagValue);
        photoTag.appendChild(encodingTag);

        Element typeTag = document.createElement(TYPE_TAG);
        Text typeTagValue = document.createTextNode( photo.getType() );
        typeTag.appendChild(typeTagValue);
        photoTag.appendChild(typeTag);
        
        Element contentTag = document.createElement(CONTENT_TAG);
        Text contentTagValue = document.createTextNode( photo.getContent() );
        contentTag.appendChild(contentTagValue);
        photoTag.appendChild(contentTag);

        return photoTag;
    }
    /**
     * read()
     * This function reads the XML document
     * @author Sergio Baena Lopez
     * @version 7.1
     * @return ContactList the read list of contacts
     */
    public static ContactList read() throws ParserConfigurationException, SAXException, IOException {
        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        saxParserFactory.setValidating(false);
        SAXParser saxParser = saxParserFactory.newSAXParser();
        XMLReader xmlReader = saxParser.getXMLReader();
        XML xml = new XML();
        xmlReader.setContentHandler(xml);
        xmlReader.parse(CONTAINER_DIRECTORY_PATH + PATH);
        return xml.contactList;
    }
    /**
     * characters()
     * This procedure stores the read text in the readText attribute.
     * @author Sergio Baena Lopez
     * @version 7.1
     */
    @Override 
    public void characters(char[] buffer, int start, int length) {
        readText = new String(buffer, start, length);
    }
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        switch(qName) {
            case CONTACT_LIST_TAG:
                contactList = new ContactList();
                break;
            case CONTACT_TAG:
                aContact = new Contact();
                break;
            case PHONE_LIST_TAG:
                readingPhoneList = true;
                break;
            case PHONE_TAG:
                aPhone = new Phone();
                break;  
            case PHOTO_TAG:
                aPhoto = new Photo();
                break;  
        }        
    }
    @Override
    public void endElement(String uri, String localName, String qName) {
        switch(qName) {
            case NAME_TAG:
                aContact.setName(readText);
                break;
            case TYPE_TAG:
                if(readingPhoneList) { // type is attribute of phone object
                    aPhone.setType( Integer.parseInt(readText) );
                } else {
                    aPhoto.setType(readText);
                }
                break;
            case NUMBER_TAG:
                aPhone.setNumber(readText);
                break;
            case PHONE_TAG:
                aContact.getPhoneList().add(aPhone);
                break;
            case PHONE_LIST_TAG:
                readingPhoneList = false;
                break;
            case ENCODING_TAG:
                aPhoto.setEncoding(readText);
                break;
            case CONTENT_TAG:
                aPhoto.setContent(readText);
                break;
            case PHOTO_TAG:
                aContact.setPhoto(aPhoto);
                break;
            case CONTACT_TAG:
                contactList.add(aContact);
                break;   
        }
    }
    /**
     * exists()
     * This function indicates if the XML document exists or not
     * @author Sergio Baena Lopez
     * @version 7.1
     * @return boolean if the XML document exists or not
     */
    public static boolean exists() {
        return new File(CONTAINER_DIRECTORY_PATH + PATH).exists();
    }
}

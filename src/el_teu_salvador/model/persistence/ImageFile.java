package el_teu_salvador.model.persistence;

import el_teu_salvador.model.Contact;
import el_teu_salvador.model.Photo;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import sun.misc.BASE64Decoder;

public class ImageFile {
    // ================================ Attributes =====================================================
    private static final String CONTAINER_DIRECTORY_PATH = "img/";
    // ================================ Static methods =====================================================
    /**
     * generate()
     * This procedure generates image files with the specified list of contacts
     * @author Sergio Baena Lopez
     * @version 1.0
     * @param List<Contact> contactList the list of contacts whose image files will be generated
     */
    public static void generate(List<Contact> contactList) throws IOException {
        for(int i = 0; i < contactList.size(); i++) {
            Contact aContact = contactList.get(i);
            Photo aPhoto = aContact.getPhoto();
            if(aPhoto != null) { // We've to create an image file
                byte [] binaryContent = obtainBinaryContent(aPhoto);
                // Now, we create and write a binary file
                DataOutputStream file = new DataOutputStream (
                    new FileOutputStream ( 
                        CONTAINER_DIRECTORY_PATH + aContact.getName() + "." + aPhoto.getType()
                    ) 
                );
                file.write(binaryContent, 0, binaryContent.length); 
                // We've already written all the content
                file.close();
            }
        }
    }
    /**
     * obtainBinaryContent()
     * This function obtains the binary content from the specified photo
     * @author Sergio Baena Lopez
     * @version 1.0
     * @param Photo photo the photo whose binary content we want to obtain
     * @return byte[] the binary content
     */
    private static byte [] obtainBinaryContent(Photo photo) throws IOException {
        byte [] binaryContent = null;
        switch( photo.getEncoding() ) {
            case "BASE64":
                binaryContent = obtainBinaryContentFromBase64(photo);
                break;
        }
        return binaryContent;
    }
    /**
     * obtainBinaryContentFromBase64()
     * This function obtains the binary content from the specified photo (encoding=Base64)
     * @author Sergio Baena Lopez
     * @version 1.0
     * @param Photo photo the photo whose binary content we want to obtain
     * @return byte[] the binary content
     */
    private static byte [] obtainBinaryContentFromBase64(Photo photo) throws IOException {
        BASE64Decoder decoder = new BASE64Decoder();
        return decoder.decodeBuffer( photo.getContent() );
    }
}

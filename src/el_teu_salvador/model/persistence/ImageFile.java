package el_teu_salvador.model.persistence;

import el_teu_salvador.model.Contact;
import el_teu_salvador.model.ContactList;
import el_teu_salvador.model.Photo;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class ImageFile {
    // ================================ Attributes =====================================================
    public static final String CONTAINER_DIRECTORY_PATH = "img/";
    public static final String DEFAULT_IMAGE_PATH = "user-without-photo.PNG";
    // ================================ Static methods =====================================================
    /**
     * generate()
     * This procedure generates image files with the specified list of contacts
     * @author Sergio Baena Lopez
     * @version 5.5
     * @param ContactList contactList the list of contacts whose image files will be generated
     */
    public static void generate(ContactList contactList) throws IOException {
        for(int i = 0; i < contactList.size(); i++) {
            Contact aContact = contactList.get(i);
            Photo aPhoto = aContact.getPhoto();
            if(aPhoto != null) { // We've to create an image file
                byte [] binaryContent = obtainBinaryContent(aPhoto);
                // Now, we create and write a binary file
                DataOutputStream file = new DataOutputStream (
                    new FileOutputStream ( 
                        CONTAINER_DIRECTORY_PATH + aContact.getId() + "." + aPhoto.getType()
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
     * obtainBinaryContent()
     * This function obtains the binary content from the specified contact
     * @author Sergio Baena Lopez
     * @version 6.2
     * @param Contact contact the contact whose binary content we want to obtain
     * @return ByteList the binary content
     */
     private static ByteList obtainBinaryContent(Contact contact) throws FileNotFoundException, 
             IOException {
         ByteList binaryContent = new ByteList(); 
         
         DataInputStream file = new DataInputStream (
            new FileInputStream ( 
                CONTAINER_DIRECTORY_PATH                +
                contact.getId()                         +
                "."                                     + 
                contact.getPhoto().obtainType()
            ) 
         );
//         file.read(binaryContent);
         boolean isEOF = false;
         while(!isEOF) {
             try {
                binaryContent.add( file.readByte() );
             } catch(EOFException e) {
                 isEOF = true;
             }
         }
         
         file.close();

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
    /**
     * obtainStringContentFromBase64()
     * This function obtains the string content the specified binary content (encoding=Base64)
     * @author Sergio Baena Lopez
     * @version 5.5
     * @param ByteList binaryContent the binary content to convert to string
     * @return String the string content
     */
    private static String obtainStringContentFromBase64(ByteList binaryContent) {
        BASE64Encoder encoder = new BASE64Encoder(); 
        return encoder.encode( binaryContent.toByteArray() );
    }
    /**
     * generate()
     * This procedure generates an image file from the specified contact
     * @author Sergio Baena Lopez
     * @version 5.5
     * @param Contact contact the contact who we generate the image file
     */
    public static void generate(Contact contact) throws IOException {
        copyToRightDirectory(contact);
        fillPhoto(contact);
    }
    /**
     * copyToRightDirectory()
     * This procedure copies the image file to its right directory
     * @author Sergio Baena Lopez
     * @version 6.2
     * @param Contact contact the contact who has all the information about the image to copy
     */
    private static void copyToRightDirectory(Contact contact) throws IOException {
        if( !isDefaultImage(contact) ) {
            File source = contact.getPhoto().getSource();
            int idContact = contact.getId();
            Photo photo = contact.getPhoto();
            String type = photo.obtainType();

            Path from = Paths.get( source.getPath() );
            Path to = Paths.get(CONTAINER_DIRECTORY_PATH + idContact + "." + type);
            CopyOption [] options = {StandardCopyOption.REPLACE_EXISTING};

            Files.copy(from, to, options);
        }
    }
    /**
     * fillPhoto()
     * This procedure fills the specified photo (it'll be in the contact)
     * @author Sergio Baena Lopez
     * @version 5.5
     * @param Contact contact the contact who contains the photo to fill
     */
    private static void fillPhoto(Contact contact) throws IOException {
        Photo photo = contact.getPhoto();
        
        if( !isDefaultImage(contact) ) {
            photo.setEncoding("BASE64");
            photo.setType( photo.obtainType() );

            ByteList binaryContent = obtainBinaryContent(contact);
            String stringContent = obtainStringContentFromBase64(binaryContent);
            photo.setContent(stringContent);
        } else {
            contact.setPhoto(null);
        }
        
        photo.setSource(null);
    }
    /**
     * isDefaultImage()
     * This function indicates if the photo of the specified contact is the default image or not
     * @author Sergio Baena Lopez
     * @version 5.5
     * @param Contact contact the contact whose photo we want to know if it is the default image or
     * not
     * @return boolean if the photo of the specified contact is the default image or not
     */
    private static boolean isDefaultImage(Contact contact) {
        File source = contact.getPhoto().getSource();
        File defaultImage = new File(CONTAINER_DIRECTORY_PATH + DEFAULT_IMAGE_PATH);
        
        return defaultImage.equals(source);
    }
}

class ByteList extends ArrayList<Byte> {
    /**
     * toByteArray()
     * This function returns an array of bytes from the list of Bytes
     * @author Sergio Baena Lopez
     * @version 5.5
     * @return byte[] an array of bytes from the list of Bytes
     */
    public byte [] toByteArray() {
        Object [] noPrimitiveByteArray = this.toArray();
        byte [] primitiveByteArray = new byte[noPrimitiveByteArray.length];
        
        for(int i = 0; i < noPrimitiveByteArray.length; i++) {
            Byte aNoPrimitiveByte = (Byte)noPrimitiveByteArray[i];
            primitiveByteArray[i] = aNoPrimitiveByte.byteValue();
        }
        
        return primitiveByteArray;
    }
}

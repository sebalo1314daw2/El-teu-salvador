package el_teu_salvador.model;

import java.io.File;
import javax.swing.ImageIcon;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Photo {
    // ================================ Attributes ================================================
    private String encoding;
    private String type;
    private String content;
    private File source;
    
    public static final int OK = 0;
    public static final int NO_PHOTO = 1;
    public static final int WRONG_DIMENSION = 2;
    // ================================ Constructors ================================================
    public Photo() {}

    public Photo(File source) {
        this.source = source;
    }
    // ================================ Accessors ================================================
    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public File getSource() {
        return source;
    }

    public void setSource(File source) {
        this.source = source;
    }
    // ================================ Methods ================================================
    @Override
    public String toString() {
        return "Photo{" + "encoding=" + encoding + ", type=" + type + ", content=" + content + ", source=" + source + '}';
    }
    /**
     * addContent()
     * This procedure adds the specified content into the content attribute
     * @author Sergio Baena Lopez
     * @version 1.0
     * @param String content the content to add
     */
    public void addContent(String content) {
        StringBuilder builder = new StringBuilder();
       
        builder.append(this.content);
        builder.append( content.trim() );
        
        this.content = builder.toString();
    }
    /**
     * validate()
     * This function validates the image file. An image file is considered valid when its extension is
     * jpg, jpeg, png and gif. It checks if the photo is 96x96, too.
     * @author Sergio Baena Lopez
     * @version 5.4
     * @return int the validation's result (OK, NO_PHOTO, WRONG_DIMENSION)
     */
    public int validate() {
        int result = OK;
        if( !source.isDirectory() ) { // source isn't directory
            FileNameExtensionFilter filter = new FileNameExtensionFilter (
                "Imatge", "jpg", "jpeg", "png", "gif"
            );
            if( filter.accept(source) ) { // the source is a photo
                ImageIcon imgIcon = new ImageIcon( source.getAbsolutePath() );
                int width = imgIcon.getIconWidth();
                int height = imgIcon.getIconHeight();
                if( !(width == 96 && height == 96) ) { // the dimensions are wrong
                    result = WRONG_DIMENSION; 
                }
            } else { // the source isn't a photo
                result = NO_PHOTO;
            }
        } else { // source is a directory --> It isn't valid
            result = NO_PHOTO;
        }
        return result;
    }
    /**
     * obtainExtension()
     * This function obtains the extension of the photo
     * @author Sergio Baena Lopez
     * @version 5.5
     * @return String the extension of the photo
     */
    public String obtainExtension() {
        String [] splitedNameSource = source.getName().split("[.]");
        return splitedNameSource[splitedNameSource.length - 1].toUpperCase();
    }
    /**
     * obtainType()
     * This function obtains the type of the photo. The possible types are JPEG, PNG and GIF.
     * @author Sergio Baena Lopez
     * @version 5.5
     * @return String the type of the photo
     */
    public String obtainType() {
       String type = null;
       
       switch( obtainExtension() ) {
           case "JPG":
           case "JPEG":
               type = "JPEG";
               break;
           case "PNG":
               type = "PNG";
               break;
            case "GIF":
               type = "GIF";
               break;
       }
       
       return type;
    }
}

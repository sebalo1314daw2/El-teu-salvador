package el_teu_salvador.model;

public class Photo {
    // ================================ Attributes ================================================
    private String encoding;
    private String type;
    private String content;
    // ================================ Constructors ================================================
    public Photo() {}
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

    // ================================ Methods ================================================
    @Override
    public String toString() {
        return "Photo{" + "encoding=" + encoding + ", type=" + type + ", content=" + content + '}';
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
}

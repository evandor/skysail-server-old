package de.twenty11.skysail.server.restlet;

public class DocumentationInfo {

//    /** The language of that documentation element. */
//    private Language language;
//
//    /** The mixed content of that element. */
//    private Graph mixedContent;
//
    /** The title of that documentation element. */
    private String title;

    /**
     * Constructor.
     */
    public DocumentationInfo() {
        super();
    }

    /**
     * Constructor with mixed content.
     * 
     * @param mixedContent
     *            The mixed content.
     */
//    public DocumentationInfo(Graph mixedContent) {
//        super();
//        this.mixedContent = mixedContent;
//    }

    /**
     * Constructor with text content.
     * 
     * @param textContent
     *            The text content.
     */
    public DocumentationInfo(String textContent) {
        super();
        //setTextContent(textContent);
    }

    /**
     * Returns the language of that documentation element.
     * 
     * @return The language of this documentation element.
     */
//    public Language getLanguage() {
//        return this.language;
//    }

    /**
     * Returns the mixed content of that element.
     * 
     * @return The mixed content of that element.
     */
//    public Graph getMixedContent() {
//        return this.mixedContent;
//    }

//    /**
//     * Returns the language of that documentation element.
//     * 
//     * @return The content of that element as text.
//     */
//    public String getTextContent() {
//        return this.mixedContent.getTextContent();
//    }
//
    /**
     * Returns the title of that documentation element.
     * 
     * @return The title of that documentation element.
     */
    public String getTitle() {
        return this.title;
    }

//    /**
//     * The language of that documentation element.
//     * 
//     * @param language
//     *            The language of that documentation element.
//     */
//    public void setLanguage(Language language) {
//        this.language = language;
//    }

//    /**
//     * Sets the mixed content of that element.
//     * 
//     * @param mixedContent
//     *            The mixed content of that element.
//     */
//    public void setMixedContent(Graph mixedContent) {
//        this.mixedContent = mixedContent;
//    }
//
//    /**
//     * Sets the content of that element as text.
//     * 
//     * @param textContent
//     *            The content of that element as text.
//     */
//    public void setTextContent(String textContent) {
//        try {
//            Document doc = new DomRepresentation(MediaType.TEXT_XML)
//                    .getDocument();
//            this.mixedContent = doc.createTextNode(textContent);
//        } catch (IOException e) {
//        }
//    }

    /**
     * Sets the title of that documentation element.
     * 
     * @param title
     *            The title of that documentation element.
     */
    public void setTitle(String title) {
        this.title = title;
    }


}
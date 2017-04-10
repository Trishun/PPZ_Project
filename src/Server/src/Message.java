import java.util.ArrayList;

/**
 * Message class
 * Created by PD on 10.04.2017.
 */
class Message {
    private String header;
    private Boolean boolContent;
    private String stringContent;
    private Double numberContent;
    private ArrayList<Float> coordinatesContent;


    Message() {

    }

    Message(String header, Boolean boolContent) {
        setHeader(header);
        setBoolContent(boolContent);
    }

    Message(String header, String stringContent) {
        setHeader(header);
        setStringContent(stringContent);
    }

    Message(String header, Double numberContent) {
        setHeader(header);
        setNumberContent(numberContent);
    }

    Message(String header, ArrayList<Float> coordinatesContent) {
        setHeader(header);
        setCoordinatesContent(coordinatesContent);
    }

    Message(Boolean boolContent) {
        setHeader("boolean");
        setBoolContent(boolContent);
    }

    Message(String stringContent) {
        setHeader("string");
        setStringContent(stringContent);
    }

    Message(Double numberContent) {
        setHeader("number");
        setNumberContent(numberContent);
    }

    Message(ArrayList<Float> coordinatesContent) {
        setHeader("coordinates");
        setCoordinatesContent(coordinatesContent);
    }
    public Object getContent() {
        if (getBoolContent() != null)
            return getBoolContent();
        else if (getStringContent() != null) {
            return getStringContent();
        }
        else if (getNumberContent() != null) {
            return getNumberContent();
        }
        else if (getCoordinatesContent() != null) {
            String data ="";
            for (Float coord : getCoordinatesContent()) {
                data = data.concat(String.valueOf(coord)+'%');
            }
            return data;
        }
        return null;
    }

    String getHeader() {
        return header;
    }

    void setHeader(String header) {
        this.header = header;
    }

    Boolean getBoolContent() {
        return boolContent;
    }

    void setBoolContent(Boolean boolContent) {
        this.boolContent = boolContent;
    }

    String getStringContent() {
        return stringContent;
    }

    void setStringContent(String stringContent) {
        this.stringContent = stringContent;
    }

    Double getNumberContent() {
        return numberContent;
    }

    void setNumberContent(Double numberContent) {
        this.numberContent = numberContent;
    }

    ArrayList<Float> getCoordinatesContent() {
        return coordinatesContent;
    }

    void setCoordinatesContent(ArrayList<Float> coordinatesContent) {
        this.coordinatesContent = coordinatesContent;
    }
}

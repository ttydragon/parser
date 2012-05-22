package container;

/**
 * User: Andrey
 * Date: 22.05.12
 * Time: 17:47
 */
public class Element {
    public int id;
    public String type;
    public int tag_id;
    public String tag_name;
    public String value;

    public Element(int id, String type, int tag_id, String tag_name, String value) {
        this.id = id;
        this.type = type;
        this.tag_id = tag_id;
        this.tag_name = tag_name;
        this.value = value;
    }

    public Element() {
    }
}

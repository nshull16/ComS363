import java.util.ArrayList;
import java.util.List;

public class Table {
    List<Attribute> attribute = new ArrayList<>();
    List<Record> record = new ArrayList<>();


    public List<Attribute> getAttribute() {
        return attribute;
    }

    public void setAttribute(List<Attribute> attribute) {
        this.attribute = attribute;
    }

    public List<Record> getRecord() {
        return record;
    }

    public void setRecord(List<Record> record) {
        this.record = record;
    }
}
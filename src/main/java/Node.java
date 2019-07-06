import java.util.ArrayList;
import java.util.List;

public class Node {
    private List<Node> friends;
    private int units;

    public Node() {
        this.friends = new ArrayList<>();
        this.units = 0;
    }

    public Node(int units) {
        this();
        this.units = units;
    }

    public void produceUnits(int number) {
        units += number;
    }

    public List<Node> getFriends() {
        return friends;
    }

    public void connect(Node node) {
           friends.add(node);
           if (!node.friends.contains(this)) {
               node.friends.add(this);
           }
    }

    public boolean isConnected(Node node) {
        return friends.contains(node);
    }

    public int getUnits() {
        return this.units;
    }

    public void give(Node node, int number) {
        if (units < number) {
            String message = toString() + " tried to give " + number + " units to "
                    + node.toString() + " but had only " + units + " units.\n";
            throw new NotEnoughUnitsException(message);
        }
        units -= number;
        node.units += number;
    }
    public void take(Node node, int number) {
        if (node.units < number) {
            String message = toString() + " tried to take " + number + " units from "
                    + node.toString() + " but " + node.toString() + " had only " + units + " units.\n";
            throw new NotEnoughUnitsException(message);
        }
        node.units -= number;
        units += number;
    }
}

import java.util.ArrayList;
import java.util.List;

public class Node {
    private List<Node> friends;
    private UtilityFunction utilityFunction;
    private int units;
    private static UtilityFunction defaultUtilityFunction = (Node node) -> node.units;

    public Node() {
        this(0, defaultUtilityFunction);
    }

    public Node(int units) {
        this(units, defaultUtilityFunction);
    }

    public Node(UtilityFunction utilityFunction) {
        this(0, utilityFunction);
    }

    public Node(int units, UtilityFunction utilityFunction) {
        this.friends = new ArrayList<>();
        this.units = units;
        this.utilityFunction = utilityFunction;
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

    public int computeUtility() {
        return utilityFunction.compute(this);
    }

    @Override
    public String toString() {
        return "Node{" +
                "friends=" + friends +
                ", utilityFunction=" + utilityFunction +
                ", units=" + units +
                '}';
    }

}

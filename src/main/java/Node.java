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

    public void produceUnits(int quantity) {
        this.units += quantity;
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
}

import java.util.ArrayList;
import java.util.List;

public class Node {
    private List<Node> friends = new ArrayList<>();
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
}

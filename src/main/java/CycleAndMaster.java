import java.util.ArrayList;
import java.util.List;

public class CycleAndMaster implements Simulation {
    @Override
    public void run() {

        final int citizensNumber = 500;
        UtilityFunction masterUtilityFunction = master -> {
            int sum = 0;
            for (Node friend : master.getFriends()) {
                sum += friend.getUnits();
            }
            return sum;
        };


        Node master = new Node(masterUtilityFunction);
        UtilityFunction citizenUtilityFunction = node -> {
            int friendSignificance = 1;
            int sum = 0;
            for (Node friend : node.getFriends()) {
                if (!friend.equals(master)) sum += friend.getUnits() * friendSignificance;
            }
            return 3*node.getUnits() + sum;
        };


        List<Node> citizens = new ArrayList<>();
        for (int i=0; i<citizensNumber; i++) {
            citizens.add(new Node(10, citizenUtilityFunction));
        }
        for (int i=0; i<citizensNumber; i++) {
            //create cycle
            Node citizen = citizens.get(i);
            citizen.connect(citizens.get((i+1)%citizensNumber));
            citizen.connect(master);
        }
        for (Node citizen : citizens) {
            int utility = citizen.computeUtility();
        }
        System.out.println("Master utility: " + master.computeUtility());

    }
}

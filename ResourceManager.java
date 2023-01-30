package teams.student.evilGrimlock;

import objects.entity.node.Node;
import objects.resource.Resource;
import player.Player;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;


public class ResourceManager {
    public static ArrayList<Resource> resourceQueue;
    public static PriorityQueue<Node> nodeQueue;
    public static Player plr;

    public static void init(Player player)  {
        plr = player;
        resourceQueue = new ArrayList<>();
        resourceQueue.addAll(player.getAllResources());
        resourceQueue.sort(new ResourceDistanceComparator());
        nodeQueue = new PriorityQueue<Node>(new NodeDistanceComparator());
        nodeQueue.addAll(player.getAllNodes());
    }

    public static void reloadResourceQueue(Player player)    {
        resourceQueue.clear();
        resourceQueue.addAll(player.getAllResources());
    }





}

class ResourceDistanceComparator implements Comparator<Resource> {

    public int compare(Resource o1, Resource o2) {
        float o1Dist = o1.getDistance(ResourceManager.plr.getMyBase().getHomeBase());
        float o2Dist = o2.getDistance(ResourceManager.plr.getMyBase().getHomeBase());
        if (o1Dist > o2Dist) {
            return 1;
        }
        if (o2Dist > o1Dist) {
            return -1;
        }
        return 0;
    }
}
class NodeDistanceComparator implements Comparator<Node> {
    @Override
    public int compare(Node o1, Node o2) {
        float o1Dist = o1.getDistance(ResourceManager.plr.getMyBase().getHomeBase());
        float o2Dist = o2.getDistance(ResourceManager.plr.getMyBase().getHomeBase());
        if (o1Dist > o2Dist) {
            return 1;
        }
        if (o2Dist > o1Dist) {
            return -1;
        }
        return 0;
    }
}

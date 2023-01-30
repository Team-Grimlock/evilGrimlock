package teams.student.evilGrimlock;

import java.util.Comparator;

public class HurtComparator implements Comparator<GrimlockUnit> {
    @Override
    public int compare(GrimlockUnit o1, GrimlockUnit o2) {
        if (o1.getPercentPlating() + o1.getPercentStructure() < o2.getPercentPlating() + o2.getPercentStructure()) {
            return 1;
        }
        if (o1.getPercentPlating() + o1.getPercentStructure() > o2.getPercentPlating() + o2.getPercentStructure()) {
            return -1;
        }
        return 0;
    }

}

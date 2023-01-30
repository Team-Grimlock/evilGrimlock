package teams.student.evilGrimlock;

import objects.entity.unit.BaseShip;
import objects.entity.unit.Unit;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import java.util.ArrayList;

public class ClumpManager {
    protected EvilGrimlock plr;
    public ArrayList<Clump> allyClumps;
    public ArrayList<Clump> enemyClumps;

    protected ArrayList<Clump> clumpRemoval;

    public ClumpManager(EvilGrimlock player)    {
        plr = player;
        init();
    }

    public void init()  {
        allyClumps = new ArrayList<Clump>();
        enemyClumps = new ArrayList<Clump>();
        clumpRemoval = new ArrayList<Clump>();
    }

    public void update()    {

        createClumps();
        mergeClumps();
        pruneClumps();
        updateClumps();

//        fillClump();
    }

    private void updateClumps() {
        allyClumps.forEach(Clump::update);
        enemyClumps.forEach(Clump::update);
    }

    private void fillClump()    {
        for(Unit u: plr.getMyBase().getAlliesExcludeBaseShip()) {
            for (Clump clump : allyClumps) {
                if (unitNotInClump(u, allyClumps) && clump.contains(u.getCenterX(), u.getCenterY()) && !clump.units.contains(u) && u.isAlive()) {
                    clump.units.add(u);
                }
            }
        }
        for(Unit u: plr.getMyBase().getEnemiesExcludeBaseShip()) {
            for (Clump clump : enemyClumps) {
                if (unitNotInClump(u, enemyClumps) && clump.contains(u.getCenterX(), u.getCenterY()) && !clump.units.contains(u) && u.isAlive()) {
                    clump.units.add(u);
                }
            }
        }
    }

    private void createClumps() {
        for (Unit u: plr.getMyBase().getAlliesExcludeBaseShip())  {
            if(unitNotInClump(u, allyClumps) && !(u instanceof BaseShip)) {
                Clump temp = new Clump(u.getCenterX(), u.getCenterY(), u.getMaxRange());
                temp.units.add(u);
                allyClumps.add(temp);
            }
        }
        for (Unit u: plr.getMyBase().getEnemiesExcludeBaseShip())  {
            if(unitNotInClump(u, enemyClumps) && !(u instanceof BaseShip)) {
                Clump temp = new Clump(u.getCenterX(), u.getCenterY(), u.getMaxRange());
                temp.units.add(u);
                enemyClumps.add(temp);
            }
        }
    }

    private void pruneClumps()  {
        allyClumps.removeIf(clump -> clump.getUnits().size() == 0);
        enemyClumps.removeIf(clump -> clump.getUnits().size() == 0);
    }

    private boolean unitNotInClump(Unit unit, ArrayList<Clump> clumps)   {
        for (Clump clump : clumps) {
            if(clump.contains(unit.getCenterX(), unit.getCenterY()) || clump.units.contains(unit))    {
                return false;
            }
        }
        return true;
    }
    private boolean unitInClump(Unit unit, ArrayList<Clump> clumps)   {
        for (Clump clump : clumps) {
            if(clump.contains(unit.getCenterX(), unit.getCenterY()) || clump.units.contains(unit))    {
                return true;
            }
        }
        return false;
    }

    private Clump mergedClump(Clump c1, Clump c2)    {
        Clump temp = new Clump((c1.getCenterX()+c2.getCenterX())/2, (c1.getCenterY()+c2.getCenterY())/2, (c1.radius+c2.radius)/2);
        temp.units.addAll(c2.units);
        temp.units.addAll(c1.units);
        clumpRemoval.add(c2);
        clumpRemoval.add(c1);
        return temp;
    }

    private void mergeClumps()  {
        ArrayList<Clump> newAllyClumps = new ArrayList<>();
        ArrayList<Clump> newEnemyClumps = new ArrayList<>();
        for (Clump c1 : allyClumps) {
            for(Clump c2 : allyClumps)   {
                if(c1.intersects(c2) && (c1.radius + c2.radius) < Clump.MAX_DIAMETER)   {
                    newAllyClumps.add(mergedClump(c1,c2));
                }
            }
        }
        for (Clump c1 : enemyClumps) {
            for(Clump c2 : enemyClumps)   {
                if(c1.intersects(c2) && (c1.radius + c2.radius) < Clump.MAX_DIAMETER)   {
                    newEnemyClumps.add(mergedClump(c1,c2));
                }
            }
        }
        allyClumps.removeAll(clumpRemoval);
        enemyClumps.removeAll(clumpRemoval);
        clumpRemoval.clear();
        allyClumps.addAll(newAllyClumps);
        enemyClumps.addAll(newEnemyClumps);
    }


    public void draw(Graphics g)    {

        allyClumps.forEach(clump -> {
            g.setColor(new Color(Color.cyan.r, Color.cyan.g, Color.cyan.b, 0.1f));
            clump.draw(g);
        });

        enemyClumps.forEach(clump -> {
            g.setColor(new Color(255, 0, 0, 0.1f));
            clump.draw(g);
        });
    }
}

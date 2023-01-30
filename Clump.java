package teams.student.evilGrimlock;


import objects.entity.Entity;
import objects.entity.unit.Unit;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Circle;

import java.util.ArrayList;

public class Clump extends Circle {

    public static final int MAX_DIAMETER = 1000;
    public static final int MAX_RADIUS = MAX_DIAMETER /2;



    public ArrayList<Unit> units;

    public int getScore()   {//TODO make resource ships contribute less value or have a boolean that determines whether the clump is a clump of miners
        int score = 0;
        for (Unit u : units) {
            switch(u.getFrame())    {
                case LIGHT:
                    score+=1;
                    break;
                case MEDIUM:
                    score+=2;
                    break;
                case HEAVY:
                    score+=3;
                    break;
                case ASSAULT:
                    score+=4;
            }
            if(u.hasWeaponOne())    {
                score+= Constants.weaponScore.get(u.getWeaponOne().getClass());
            }
            if(u.hasWeaponTwo())    {
                score+= Constants.weaponScore.get(u.getWeaponTwo().getClass());
            }
        }
        return score;
    }

    public ArrayList<Unit> getUnits() {
        return units;
    }

    public Clump(float xCoord, float yCoord, float radius) {
        super(xCoord, yCoord, radius);
        units = new ArrayList<Unit>();
    }

    public void update()    {
        cleanUnits();
        if(units.size() > 0) {
            setCenterX(averageX());
            setCenterY(averageY());
            setRadius(furthestUnit().getMaxRange());
        }
    }

    private float averageX()  {

        int xTotal = 0;
        for(Unit u: units)  {
            xTotal += u.getCenterX();
        }
        return xTotal/units.size();
    }
    private float averageY()  {
        int yTotal = 0;
        for(Unit u: units)  {
            yTotal += u.getCenterY();
        }
        return yTotal/units.size();
    }

    private void cleanUnits()   {
        units.removeIf(Entity::isDead);
        removeUnitsOutOfRadius();
    }

    private void removeUnitsOutOfRadius()   {
        if(furthestUnit() != null && furthestUnit().getDistance(this.getCenterX(), this.getCenterY()) > MAX_RADIUS) {
            units.remove(furthestUnit());
            removeUnitsOutOfRadius();
        }
    }

    private Unit furthestUnit()  {
        Unit result = null;
        int distance = 0;
        for(Unit u: units)    {
            if(u.getDistance(getCenterX(),getCenterY()) > distance)   {
                result = u;
            }
        }
        return result;
    }


    public void draw(Graphics g)  {
        g.fill(this);
        g.fill(new Circle(getCenterX(),getCenterY(), MAX_RADIUS));
        g.setColor(Color.white);
        g.drawString("Score: " + String.valueOf(getScore()), getCenterX() + 100, getCenterY() + 100);
        g.drawString("Count: " + String.valueOf(units.size()), getCenterX() + 100, getCenterY() + 200);

    }

}

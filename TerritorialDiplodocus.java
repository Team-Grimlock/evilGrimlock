package teams.student.evilGrimlock;

import components.upgrade.CargoBay;
import objects.entity.unit.Frame;
import objects.entity.unit.Style;
import org.newdawn.slick.Graphics;

public class TerritorialDiplodocus extends Diplodocus {
    public TerritorialDiplodocus(EvilGrimlock p, Snarl captain) {
        super(p, ((GrimlockUnit)captain));
    }

    public void design()    {
        setFrame(Frame.MEDIUM);
        setStyle(Style.BUBBLE);
        addWeapon(new components.weapon.resource.Collector(this));
        addUpgrade(new CargoBay(this));
        addUpgrade(new CargoBay(this));
    }

    @Override
    public void init() {
        super.init();
    }

    @Override
    public void draw(Graphics g)    {

    }

    public void shipBehavior()    {
        if(flee > 0)	{
            returnResources();
            flee--;
        }
        else {

            if (hasCapacity() ) {
                if(getDistance(getNearestResource()) < getMaxRange() * 2) {
                    moveTo(getNearestResource());
                    if (getDistance(getNearestResource()) < getWeaponOne().getMaxRange() * 0.9f) {
                        collect();
                    }
                }
                follow(captain, 300);
            } else {
                returnResources();
            }
            if (getEnemiesInRadius(1000).size() > 0) {
                flee = getEnemiesInRadius(1000).size() * 600;
            }
        }
    }
}

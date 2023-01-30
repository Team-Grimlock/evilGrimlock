package teams.student.evilGrimlock;

import components.upgrade.Rangefinder;
import components.weapon.explosive.ShortRangeMissile;
import components.weapon.resource.MiningLaser;
import components.weapon.utility.SpeedBoost;
import objects.entity.unit.Frame;
import objects.entity.unit.Style;
import org.newdawn.slick.Graphics;
import player.Player;

public class RKelly extends GrimlockUnit {



    public RKelly(Player p) {
        super(p);
    }

    @Override
    public void design() {
        setFrame(Frame.MEDIUM);
        setStyle(Style.WEDGE);
        addWeapon(new ShortRangeMissile(this));
        addWeapon(new SpeedBoost(this));
        //addWeapon(new ShadowflightMissile(this));
        //addUpgrade(new OptimizedAlgorithms(this));
        addUpgrade(new Rangefinder(this));
    }

    public void shipBehavior()    {
        //
    }

    public void draw(Graphics g)    {
        lineTarget(g, target);
    }

    protected void movement()   {
        if(target != null && target.hasWeapon(MiningLaser.class))
        {
            // Only approach if I have shield
            if(getDistance(target) > getMaxRange() && getCurShield() > 0)
            {
                moveTo(target);
            }
            else
            {
                turnTo(target);
                turnAround();
                move();
            }
        }
    }

    protected void findTarget() {

    }
}

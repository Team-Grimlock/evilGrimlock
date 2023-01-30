package teams.student.evilGrimlock;

import components.upgrade.Rangefinder;
import components.upgrade.Shield;
import components.weapon.Weapon;
import components.weapon.utility.CommandRelay;
import components.weapon.utility.ElectromagneticPulse;
import components.weapon.utility.Pullbeam;
import objects.entity.unit.Frame;
import objects.entity.unit.Style;
import objects.entity.unit.Unit;
import player.Player;

public class LightningClaw extends GrimlockUnit {
    public LightningClaw(Player p) {
        super(p);
    }

    @Override
    public void design() {
        setFrame(Frame.MEDIUM);
        setStyle(Style.ARROW);
        addWeapon(new Pullbeam(this));
        addUpgrade(new Shield(this));
        addUpgrade(new Rangefinder(this));
    }

    public void shipBehavior()  {
        if(!getEnemies().isEmpty()) {
            if (getDistance(getNearestAlly()) < 50) {
                moveAway(getNearestAlly());
            } else if (getDistance(getNearestEnemyInBoundsExcludeBaseShip()) < getPlayer().greemlock.getDistance(getNearestEnemyInBoundsExcludeBaseShip()) || getHomeBase().getDistance(getPlayer().resCaptain) > getHomeBase().getDistance(getNearestEnemy())) {
                moveTo(getPlayer().greemlock);
            } else {
                skirmish(getWeaponOne(), getNearestEnemyInBoundsWithoutWeapon(ElectromagneticPulse.class));
            }
        }
    }

    public void defend()    {
        if(getHomeBase().getDistance(getNearestEnemyInBounds()) < getHomeBase().getMaxRange()*4)	{
            if(getDistance(getHomeBase()) > getHomeBase().getMaxRange()*4)	{
                moveTo(getHomeBase());
            }
        }
        else if(outOfRange(getPlayer().resCaptain, CommandRelay.RADIUS/2f)) {
            moveTo(getPlayer().resCaptain);
        }

        skirmish(getWeaponOne(), getNearestEnemyInBoundsWithoutWeapon(ElectromagneticPulse.class));

    }

    public void skirmish(Weapon w, Unit u)  {
        target = u;
        if(getDistance(u) > w.getMaxRange())    {
            moveTo(u);
        }
        else if(getDistance(u) < w.getMaxRange()*0.9)   {
            moveTo(getHomeBase());
        }
        w.use(u);
    }


}

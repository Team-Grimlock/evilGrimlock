package teams.student.evilGrimlock;

import components.weapon.Weapon;
import components.weapon.explosive.AntimatterMissile;
import components.weapon.resource.Collector;
import components.weapon.resource.MiningLaser;
import components.weapon.utility.CommandRelay;
import objects.entity.unit.Frame;
import objects.entity.unit.Style;
import objects.entity.unit.Unit;
import player.Player;

public class Pterasaur extends GrimlockUnit {
int isolated = 0;
    public Pterasaur(EvilGrimlock p)
    {
        super(p);
    }

    public Pterasaur(Player p, GrimlockUnit captain) {
        super(p, captain);
    }

    public void design()
    {
        setFrame(Frame.HEAVY);
        setStyle(Style.ARROW);
        addWeapon(new AntimatterMissile(this));
        addWeapon(new AntimatterMissile(this));
//        addUpgrade(new Rangefinder(this));
//        addUpgrade(new OptimizedAlgorithms(this));
    }

    public void shipBehavior() {
        if(!getEnemies().isEmpty()) {
            if (getDistance(getNearestAlly()) < 50) {
                moveAway(getNearestAlly());
            } else if (getDistance(getNearestEnemyInBoundsExcludeBaseShip()) < getPlayer().greemlock.getDistance(getNearestEnemyInBoundsExcludeBaseShip()) || getHomeBase().getDistance(getPlayer().resCaptain) > getHomeBase().getDistance(getNearestEnemy())) {
                moveTo(getPlayer().greemlock);
            } else {
                skirmish(getWeaponOne(), getNearestEnemyInBounds());
                skirmish(getWeaponTwo(), getNearestEnemyInBounds());
                if (attackBaseShipCondition()) {
                    skirmish(getWeaponOne(), getEnemyBase());
                    skirmish(getWeaponTwo(), getEnemyBase());
                }
            }
            getWeaponOne().use(getNearestEnemyInBounds());
            getWeaponTwo().use(getNearestEnemyInBounds());
        }
    }

    @Override
    public void defend() {
        if(getDistance(getNearestAllyUnit()) > 100) {
            moveTo(getNearestAllyUnit());
        }
        else if(getHomeBase().getDistance(getNearestEnemyInBounds()) < getHomeBase().getMaxRange()*4)	{
            if(getDistance(getHomeBase()) > getHomeBase().getMaxRange()*4)	{
                moveTo(getHomeBase());
            }
        }
        else if(outOfRange(getPlayer().resCaptain, CommandRelay.RADIUS/2f)) {
            moveTo(getPlayer().resCaptain);
        }

        skirmish(getWeaponOne(), getNearestEnemyInBoundsExcludeBaseShip());
        skirmish(getWeaponTwo(), getNearestEnemyInBoundsExcludeBaseShip());

    }

    //    @Override
    protected void skirmish(Weapon w, Unit u) {
        target = u;
        if(getDistance(u) > w.getMaxRange())    {
            moveTo(u);
        }
        else if(getDistance(u) < w.getMaxRange()*0.9)   {
            moveTo(getHomeBase());
        }
        w.use(u);
    }

    public boolean fleeing(){
        if(!getNearestEnemyInBounds().hasWeapon(MiningLaser.class) && !getNearestEnemyInBounds().hasWeapon(Collector.class)) {
            if(getNearestEnemyInBounds().getMaxRange() *1.1 > getDistance(getNearestEnemyInBounds())) {
                return true;
            }
            return false;
        }
        return false;
    }

}

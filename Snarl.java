package teams.student.evilGrimlock;

import components.weapon.resource.MiningLaser;
import components.weapon.utility.CommandRelay;
import objects.entity.unit.Frame;
import objects.entity.unit.Style;

public class Snarl extends Orodromines {
    private int fleeTime;
    public Snarl(EvilGrimlock p) {
        super(p);
        this.assignCaptain(this);
    }

    @Override
    public void design() {
        setFrame(Frame.MEDIUM);
        setStyle(Style.WEDGE);
        addWeapon(new MiningLaser(this));
        addWeapon(new CommandRelay(this));
    }

    public void shipBehavior() {
        if (target == null) {
            locateNode();
        }
        if (getDistance(target) < getWeaponOne().getMaxRange() * 2) {
            getWeaponTwo().use();
        }
            if (getPlayer().timer - fleeTime < 60 && timer > 60) {
                if(getPercentEffectiveHealth() < 0.5)	{
                    fleeTime = -600;
                    //moveTo(getNearestAllyUnit(getPlayer(), Tag.FIGHTER));
                    moveTo(getHomeBase());
//                    if(getDistance(getNearestAllyUnit(getPlayer(), Tag.FIGHTER)) < 50)
                    if(getDistance(getHomeBase()) < 50)	{
                        fleeTime = 0;

                    }
                }
                else if (!attackBaseShipCondition()) {
                    kite(getNearestEnemyUnit());
                    getWeaponOne().use(getNearestEnemyUnit());
                    getWeaponTwo().use(getNearestEnemyUnit());
                }

            }else {
                if (target == null) {
                    target = ((Snarl) captain).getTarget();
                } else {
                    harvest(target, getWeaponOne());
                    harvest(target, getWeaponTwo());
                    if (target.isDead())
                        target = null;
                }
                if (getDistance(getNearestEnemyUnit()) < 500) {
//				getWeaponOne().use(getNearestEnemyUnit());
//				getWeaponTwo().use(getNearestEnemyUnit());
                    fleeTime = getPlayer().timer;
                }

            }
    }

}

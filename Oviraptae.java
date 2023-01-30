package teams.student.evilGrimlock;

import components.weapon.kinetic.MachineGun;
import components.weapon.resource.MiningLaser;
import objects.entity.unit.Frame;
import objects.entity.unit.Style;
import objects.entity.unit.Unit;
import player.Player;

import java.util.ArrayList;

public class Oviraptae extends GrimlockUnit {
    public Oviraptae(Player p) {
        super(p);
    }

    protected static Unit commonTarget = null;



    @Override
    public void design() {
        setFrame(Frame.LIGHT);
        setStyle(Style.WEDGE);
        addWeapon(new MachineGun(this));
        addWeapon(new MachineGun(this));
    }


    public void shipBehavior() {
        //target = getNearestEnemyInBoundsWithWeapon(MiningLaser.class);
        target = weakestEnemyMiner();
        if(commonTarget != null)  {
            if(commonTarget.isDead() || commonTarget.getDistance(getEnemyBase()) < getEnemyBase().getMaxRange())   {
                commonTarget = null;
                return;
            }
            if(getDistance(commonTarget) < getDistance(target) && target.getPercentEffectiveHealth() < commonTarget.getPercentEffectiveHealth()) {
                target = commonTarget;
            }
        }
        else {
            commonTarget = target;
        }

        movement();
        getWeaponOne().use(target);

    }

    protected void movement() {
        if (!getOuttaHere()) {
                if (getDistance(getNearestAlly(Orodromines.class)) < 1000) {
                    moveAway(getNearestAlly(Orodromines.class));

                }
            else if (target != null) {
                if (getDistance(getNearestEnemyInBounds()) + getDistance(target) - getNearestEnemyInBounds().getDistance(target) > 1000 && getNearestEnemyInBounds() != target) {
                    circle(getNearestEnemyInBounds());
                    return;
                }
                if (getPercentStructure() < 0.5) {
                    moveTo((getPlayer().getFleetCount(Sludge.class) > 0) ? getNearestAlly(Sludge.class) : getHomeBase());
                } else {
                    if (outOfRange(target, getWeaponOne())) {
                        moveTo(target);
                    } else if (tooClose(target, getWeaponOne().getMaxRange() * 0.7f)) {
                        moveAway(getNearestEnemyInBoundsWithoutWeapon(MiningLaser.class));
                    }
                }

            } else {
                moveTo(getHomeBase());
            }
        }
        else {
            moveTo(getNearestAllyUnit(getPlayer(), Tag.HEALER));
        }
    }

  protected boolean getOuttaHere() {
        return(getPercentEffectiveHealth() < 0.51);
  }
    public Unit weakestEnemyMiner() {
        ArrayList<Unit> units = getUnits(getOpponent());
        for(Unit u: units) {
            if(u.hasWeapon(MiningLaser.class)) {
                if(u.countAlliesInRadius(1000) >= getPlayer().getFleetCount(Oviraptae.class)-2) {
                    return u;
                }
            }
        }
        return null;
    }

}

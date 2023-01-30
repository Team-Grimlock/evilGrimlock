package teams.student.evilGrimlock;

import components.weapon.utility.RepairBeam;
import engine.states.Game;
import objects.entity.unit.BaseShip;
import objects.entity.unit.Frame;
import objects.entity.unit.Style;
import objects.entity.unit.Unit;
import player.Player;

import java.util.ArrayList;

public class Triceratops extends GrimlockUnit {
    public Triceratops(Player p) {
        super(p);
    }
    public Triceratops(Player p, GrimlockUnit captain) {
        super(p, captain);
    }
    @Override
    public void design() {
        setFrame(Frame.MEDIUM);
        setStyle(Style.BOXY);
        addWeapon(new RepairBeam(this));
        addWeapon(new RepairBeam(this));
    }

    @Override
    public void shipBehavior() {
        if(getDistance(getNearestAlly()) < 10)  {
            moveAway(getNearestAlly());
            return;
        }

        if(this.getCenterX() > getPlayer().greemlock.getCenterX() - 200)  {
            moveTo(getHomeBase());

        }
        else if (outOfRange(getPlayer().greemlock, 300)) {
            moveTo(getPlayer().greemlock);

        }
        else {
            follow(healTarget == null ? getPlayer().greemlock : healTarget);
            heal();
        }

    }

    @Override
    public void defend() {
        if(!getEnemies().isEmpty()) {
            if(getDistance(getNearestAllyUnit()) > 100) {
                moveTo(getNearestAllyUnit());
            }
            else if (getDistance(getNearestEnemyInBounds()) < getNearestEnemyInBounds().getMaxRange() || getHomeBase().getDistance(getPlayer().resCaptain) > getHomeBase().getDistance(getNearestEnemy())) {
                moveTo(getNearestAllyUnit(getPlayer(), Tag.FIGHTER));
            } else {
                follow(healTarget == null ? getPlayer().resCaptain : healTarget);
            }
            heal();
        }
    }

    protected void heal()   {
        if(healTarget == null)    {
            healTarget = (GrimlockUnit) locateTarget();
        }
        else {
            getWeaponOne().use(healTarget);
            getWeaponTwo().use(healTarget);
            if(outOfRange(healTarget))  {
                moveTo(healTarget);
            }
            else if(tooClose(healTarget, getMaxRange()*0.5f)){
                moveAway(healTarget);
            }
            if(healTarget.getPercentEffectiveHealth() > 0.95 || healTarget.isDead())   {
                healTarget = null;
            }
        }
    }

    private Unit locateTarget() {
        Unit target = null;
        float health = 1;
        ArrayList<Unit> nearbyAllies = getAlliesInRadius(getWeaponOne().getMaxRange()*2);
        if(!nearbyAllies.isEmpty()) {
            for (Unit u : nearbyAllies) {
                if (u.getPercentEffectiveHealth() < 1) {
                    if (u instanceof Sludge || u instanceof Grimlocke) {
                        return u;
                    }
                }
                if (u.getPercentEffectiveHealth() < health && !(u instanceof BaseShip)) {
                    target = u;
                    health = u.getPercentEffectiveHealth();
                }
            }

            return target;
        }
        for (Unit u : getAlliesInRadius(Game.getMapWidth())) {
            if (u.getPercentEffectiveHealth() < 1) {
                if (u instanceof Sludge || u instanceof Grimlocke) {
                    return u;
                }
            }
            if (u.getPercentEffectiveHealth() < health && !(u instanceof BaseShip)) {
                target = u;
                health = u.getPercentEffectiveHealth();
            }
        }
        return target;
    }
}

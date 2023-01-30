package teams.student.evilGrimlock;

import components.upgrade.Structure;
import components.weapon.kinetic.FlakBattery;
import components.weapon.utility.Aegis;
import components.weapon.utility.CommandRelay;
import objects.entity.unit.Frame;
import objects.entity.unit.Style;
import player.Player;

public class Sludge extends GrimlockUnit {


    public Sludge(Player p) {
        super(p);
    }
    public Sludge(Player p, GrimlockUnit captain) {
        super(p, captain);
    }

    @Override
    public void design() {
        setFrame(Frame.HEAVY);
        setStyle(Style.ORB);
//        addWeapon(new CommandRelay(this));
        addWeapon(new FlakBattery(this));
        addWeapon(new Aegis(this));
        addUpgrade(new Structure(this));
        addUpgrade(new Structure(this));
        addUpgrade(new Structure(this));


    }

    public void shipBehavior()  {
        if(!getEnemies().isEmpty()) {
            if (getPlayer().greemlock == null || getPlayer().greemlock.isDead()) {
                if (getDistance(getNearestAllyUnit(getPlayer(), Tag.FIGHTER)) > 100) {
                    moveTo(getNearestAllyUnit(getPlayer(), Tag.FIGHTER));
                } else if (getDistance(getNearestEnemyInBoundsExcludeBaseShip()) > getPlayer().groupAverageDistance(getAlliesInRadius(1000), getNearestEnemyInBoundsExcludeBaseShip())) {
                    moveTo(getNearestEnemyInBoundsExcludeBaseShip());
                }
                getWeaponOne().use(getNearestEnemyInBounds());
            } else {
                if (getDistance(getPlayer().greemlock) > 200) {
                    moveTo(getPlayer().greemlock);
                } else if (getDistance(getNearestEnemyInBounds()) > getPlayer().greemlock.getDistance(getNearestEnemyInBounds())) {
                    moveTo(getNearestEnemyInBounds());
                }
                if (getDistance(getNearestEnemyInBounds()) < getNearestEnemyInBounds().getMaxRange()*0.5) {
                    getWeaponTwo().use();
                }
            }
            getWeaponOne().use(getNearestEnemyInBounds());
        }
    }

    @Override
    public void defend() {
        if(!getEnemies().isEmpty()) {
            if(getDistance(getNearestAllyUnit()) > 100) {
                moveTo(getNearestAllyUnit());
            }
            else if (getHomeBase().getDistance(getNearestEnemyInBounds()) < getHomeBase().getMaxRange() * 4 || getHomeBase().getDistance(getPlayer().resCaptain) > getHomeBase().getDistance(getNearestEnemy())) {
                if (getDistance(getHomeBase()) > getHomeBase().getMaxRange() * 4) {
                    moveTo(getHomeBase());
                }
            } else if (outOfRange(getPlayer().resCaptain, CommandRelay.RADIUS / 2f)) {
                moveTo(getPlayer().resCaptain);
            }

            skirmish(getWeaponOne(), getNearestEnemyInBounds());
            if (getDistance(getNearestEnemyInBounds()) < getNearestEnemyInBounds().getMaxRange() * 0.7) {
                getWeaponTwo().use();
            }

        }
    }

}

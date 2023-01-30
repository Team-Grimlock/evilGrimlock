package teams.student.evilGrimlock;

import components.weapon.utility.ElectromagneticPulse;
import objects.entity.unit.Frame;
import objects.entity.unit.Style;
import objects.entity.unit.Unit;
import org.newdawn.slick.geom.Point;
import player.Player;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

public class Kamikaze extends GrimlockUnit {

    private int used = 0;

    public Kamikaze(Player p) {
        super(p);
    }

    @Override
    public void design() {
        setFrame(Frame.MEDIUM);
        setStyle(Style.ORB);
        addWeapon(new ElectromagneticPulse(this));
        addWeapon(new ElectromagneticPulse(this));

    }

    @Override
    public void shipBehavior() {
        if(used > 0)    {
            used--;
        }
        if(getDistance(getNearestEnemy()) < 1000) {
            target = target();
            if (getWeaponOne().onCooldown() && getWeaponTwo().onCooldown()) {
                moveTo(getPlayer().greemlock);

            }
            else {
                moveTo(target);
                if (getWeaponOne().getMaxRange() * 0.7f > getDistance(target) && used == 0) {
                    (getWeaponOne().onCooldown()?getWeaponTwo():getWeaponOne()).use();
                    used = 180;
                }
            }
        }
        else {
            moveTo(getPlayer().greemlock);
        }
    }

    public Unit target()  {
        ArrayList<Unit> enemies = getEnemiesInRadius(getWeaponOne().getMaxRange() * 2);
        if(enemies.isEmpty())   {
            return getNearestEnemyInBounds();
        }
        float totalX = 0;
        float totalY = 0;

        for(Unit u: enemies)   {
            totalX += u.getCenterX();
            totalY += u.getCenterY();
        }

        Point center = new Point(totalX/enemies.size(), totalY/enemies.size());
        AtomicReference<Unit> result = new AtomicReference<>(enemies.get(0));
        enemies.forEach(u ->{
            if(u.getDistance(center) < result.get().getDistance(center))   {
                result.set(u);
            }
        });
        return result.get();
    }
}

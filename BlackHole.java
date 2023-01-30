package teams.student.evilGrimlock;

import components.upgrade.Rangefinder;
import components.weapon.utility.GravitationalRift;
import objects.entity.unit.Frame;
import objects.entity.unit.Style;
import objects.entity.unit.Unit;
import org.newdawn.slick.geom.Point;
import player.Player;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

public class BlackHole extends GrimlockUnit {
    public BlackHole(Player p) {
        super(p);
    }

    public boolean rifted() {
        return getWeaponOne().onCooldown();
    }

    @Override
    public void design() {
        setFrame(Frame.HEAVY);
        setStyle(Style.PINCER);
        addWeapon(new GravitationalRift(this));
        addUpgrade(new Rangefinder(this));
        addUpgrade(new Rangefinder(this));
        addUpgrade(new Rangefinder(this));
        addUpgrade(new Rangefinder(this));

//        addUpgrade(new Structure(this));
    }

    @Override
    public void shipBehavior()  {

        if(getDistance(getNearestEnemyInBoundsExcludeBaseShip()) < getPlayer().greemlock.getDistance(getNearestEnemyInBoundsExcludeBaseShip()) - 200) {
            moveTo(getPlayer().greemlock);
        }
        else {
            moveTo(target() == null? getNearestEnemyInBounds():target());
        }
        getWeaponOne().use(target());

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

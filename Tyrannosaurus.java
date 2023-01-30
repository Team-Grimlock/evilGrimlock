package teams.student.evilGrimlock;

import components.upgrade.Rangefinder;
import components.upgrade.Structure;
import components.weapon.utility.CommandRelay;
import objects.entity.unit.BaseShip;
import objects.entity.unit.Frame;
import objects.entity.unit.Style;
import objects.entity.unit.Unit;
import org.newdawn.slick.geom.Point;
import player.Player;

import java.util.ArrayList;

public class Tyrannosaurus extends GrimlockUnit {
    private Point destination;

    public Tyrannosaurus(Player p) {
        super(p);
    }
    public Tyrannosaurus(Player p, GrimlockUnit captain) {
        super(p, captain);
    }

    @Override
    public void design() {
        setFrame(Frame.MEDIUM);
        setStyle(Style.WEDGE);
        addWeapon(new CommandRelay(this));
        addUpgrade(new Rangefinder(this));
        addUpgrade(new Structure(this));
    }

    public void shipBehavior()  {
        moveTo(findCenter());
        if(getAlliesInRadius(getWeaponOne().getRadius()).size() > 3)  {
            getWeaponOne().use();
        }
    }


    protected Point findCenter()  {
        ArrayList<Unit> allies = getAlliesInRadius(getWeaponOne().getRadius() * 4);
        allies.removeIf(unit -> {
            return unit instanceof BaseShip || ((GrimlockUnit) unit).tags.contains(Tag.RESOURCE);
        });
        if(allies.isEmpty())    {
            return (getPlayer().greemlock != null || getPlayer().greemlock.isAlive())?getPlayer().greemlock.getPosition() : getNearestAllyUnit(getPlayer(), Tag.FIGHTER).getPosition();
        }

        float totalX = 0;
        float totalY = 0;

        for(Unit u: allies)   {
            totalX += u.getCenterX();
            totalY += u.getCenterY();
        }

        return new Point(totalX/allies.size(), totalY/allies.size());
    }
}

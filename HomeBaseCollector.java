package teams.student.evilGrimlock;

import components.upgrade.Rangefinder;
import components.weapon.energy.Brightlance;
import components.weapon.resource.Collector;
import objects.entity.unit.Frame;
import objects.entity.unit.Style;

public class HomeBaseCollector extends GrimlockUnit {
    public HomeBaseCollector(EvilGrimlock p) {
        super(p);
    }

    @Override
    public void design() {
        setFrame(Frame.ASSAULT);
        setStyle(Style.ORB);
        addWeapon(new Collector(this));
        addWeapon(new Brightlance(this));
        addUpgrade(new Rangefinder(this));
        addUpgrade(new Rangefinder(this));
        addUpgrade(new Rangefinder(this));
    }

    public void shipBehavior()  {
        follow(getHomeBase(), 100);
        getWeaponOne().use();
        getWeaponTwo().use(getNearestEnemyInBounds());
        deposit();
    }


}

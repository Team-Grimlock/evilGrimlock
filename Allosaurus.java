package teams.student.evilGrimlock;

import components.upgrade.Structure;
import components.weapon.Weapon;
import components.weapon.energy.LargeLaser;
import components.weapon.explosive.InfernoLauncher;
import components.weapon.kinetic.Autocannon;
import components.weapon.kinetic.MassDriver;
import objects.entity.unit.Frame;
import objects.entity.unit.Style;
import player.Player;

public class Allosaurus extends GrimlockUnit {
    public Allosaurus(Player p, GrimlockUnit captain) {
        super(p, captain);
    }

    @Override
    public void design() {
        setFrame(Frame.HEAVY);
        setStyle(Style.DAGGER);
        addWeapon(new Autocannon(this));
        addWeapon(new LargeLaser(this));
        addUpgrade(new Structure(this));
        addUpgrade(new Structure(this));

    }

    public void shipBehavior()    {
        //
        if(getDistance(getNearestEnemy()) < getMaxRange()*1.3)   {
            skirmish(getWeaponOne(),getNearestEnemy());
            skirmish(getWeaponTwo(),getNearestEnemy());
        }
        else {
            target = null;
            follow(captain);
        }
    }

    @Override
    public Weapon explosiveWeapon() {
        return new InfernoLauncher(this);
    }

    @Override
    public Weapon energyWeapon() {
        return new LargeLaser(this);
    }

    @Override
    public Weapon kineticWeapon() {
        return new MassDriver(this);
    }


}

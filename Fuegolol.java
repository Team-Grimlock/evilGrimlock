package teams.student.evilGrimlock;

import components.upgrade.Structure;
import components.weapon.explosive.InfernoLauncher;
import objects.entity.unit.Frame;
import objects.entity.unit.Style;
import player.Player;

public class Fuegolol extends GrimlockUnit {

    public Fuegolol(Player p) {
        super(p);
    }



    @Override
    public void design() {
        setFrame(Frame.MEDIUM);
        setStyle(Style.PINCER);

        addWeapon(new InfernoLauncher(this));
        addUpgrade(new Structure(this));
    }

    @Override
    public void shipBehavior() {
        skirmish(getWeaponOne());
    }
}

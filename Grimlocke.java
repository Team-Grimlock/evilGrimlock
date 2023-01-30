package teams.student.evilGrimlock;

import components.upgrade.Structure;
import components.weapon.kinetic.MassDriver;
import components.weapon.utility.Aegis;
import components.weapon.utility.Pullbeam;
import objects.entity.unit.Frame;
import objects.entity.unit.Style;
import objects.entity.unit.Unit;
import player.Player;

public class Grimlocke extends GrimlockUnit {

    private boolean push;
    public Grimlocke(Player p) {
        super(p);
    }

    @Override
    public void design() {
        setFrame(Frame.HEAVY);
        setStyle(Style.DAGGER);
        addWeapon(new MassDriver(this));
        addWeapon(new Aegis(this));
        addUpgrade(new Structure(this));
        addUpgrade(new Structure(this));
        addUpgrade(new Structure(this));
        addUpgrade(new Structure(this));

    }

    public void shipBehavior()  {
        if(getPercentEffectiveHealth() < .5)   {
            moveTo(getHomeBase());
            rally = this.getPosition();
        }
        else {
            if(getDistance(getNearestEnemyInBounds()) < getNearestAlly().getDistance(getNearestEnemyInBounds()))
            {
                moveTo(getHomeBase());
                return;
            }
            if (getEnemiesInRadius(500, Unit.class ,rally != null? rally:getPosition()).isEmpty()) {
                push = true;
            }
            if (push) {
                moveTo(getNearestEnemyInBounds());
                if (getDistance(getNearestEnemyInBounds()) < 1000) {
                    rally = this.getPosition();
                    push = false;

                }
            } else {
                if(getEnemiesInRadius(1000, Unit.class, rally).size() > getAlliesInRadius(1000, Unit.class, rally).size()) {
                    rally.setLocation(((getCenterX()+getHomeBase().getCenterX())/2) * 1.5f, ((getCenterY()+getHomeBase().getCenterY())/2) * 1.5f);
                }
                if(getNearestEnemyInBounds().hasWeapon(Pullbeam.class)) {
                    if(this.getDistance(getNearestEnemyInBounds()) < Pullbeam.MAX_RANGE*1.2)   {
                        moveTo(getHomeBase());
                        return;
                    }
                }

                if(outOfRange(rally, 1000)) {
                    moveTo(rally);
                }
                else    {
                    if(outOfRange(getNearestEnemyInBounds())) {
                        moveTo(getNearestEnemyInBounds());
                    }
                }
                getWeaponTwo().use();
                getWeaponOne().use(getNearestEnemyInBounds());
            }
        }
    }


}



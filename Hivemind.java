package teams.student.evilGrimlock;

import components.upgrade.OptimizedAlgorithms;
import components.upgrade.Shield;
import components.weapon.Weapon;
import components.weapon.energy.EnergySiphon;
import components.weapon.explosive.ShortRangeMissile;
import components.weapon.kinetic.MachineGun;
import components.weapon.utility.ShieldBattery;
import objects.entity.unit.BaseShip;
import objects.entity.unit.Frame;
import objects.entity.unit.Style;
import objects.entity.unit.Unit;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import player.Player;

import java.util.ArrayList;

public class Hivemind extends GrimlockUnit {


    public Hivemind(Player p) {
        super(p);
    }


    protected boolean kamikaze;


    @Override
    public void design() {
        setFrame(Frame.MEDIUM);
        setStyle(Style.DAGGER);
        addWeapon(new EnergySiphon(this));
        addWeapon(new ShieldBattery(this));
        addUpgrade(new Shield(this));
        addUpgrade(new OptimizedAlgorithms(this));
    }


    public void shipBehavior()    {
        healHivemind();
        if(attackBaseShipCondition())   {
            kamikaze = true;
        }

        if(getPlayer().getMyUnits(Hivemind.class).size() >= 3) {
            spaceOut(getWeaponTwo().getMaxRange()*0.7f);



            if (target == null) {
                skirmish(getWeaponOne(), getNearestEnemy());
                if (getNearestEnemiesInRadius(1000, 5).size() > 0) {
                    target = getNearestEnemiesInRadius(1000, 1).get(0);
                }
                for (Unit ally : getPlayer().getMyUnits(Hivemind.class)) {
                    if (allyHasTarget((GrimlockUnit) ally) && !((Hivemind) ally).targetInaccessible()) {
                        target = ((GrimlockUnit) ally).getTargetEnemy();

                    }
                }
            } else {
                if (targetInaccessible()) {
                    target = null;
                }
                else if (overextending() && !kamikaze) {
                    moveTo(getHomeBase());
                }
                else {
                    skirmish(getWeaponOne(), target);
                }

            }
        }
        else {
            defend();
        }
        if(hurt() && !kamikaze) {
            hurtRetreat();
        }
    }


    public Weapon explosiveWeapon() {
//        return new InfernoLauncher(this);
        return new ShortRangeMissile(this);
    }

    @Override
    public Weapon energyWeapon() {
//        return new LargeLaser(this);
        return new EnergySiphon(this);
    }

    @Override
    public Weapon kineticWeapon() {
//        return new Autocannon(this);
        return new MachineGun(this);
    }


    public void defend() {
        if(getDistance(getHomeBase()) > 500)	{
            moveTo(getHomeBase());
            return;
        }
        if(getDistance(getNearestEnemyInBounds()) < 500 || target == null)	{
            target = getNearestEnemyInBounds();
        }

        if(target.isAlive())	{
            skirmish(getWeaponOne(), target);
            if(target.isDead() || !target.isInBounds())	{
                target = null;
            }
        }
    }

    public boolean targetInaccessible() {
        return ((!target.isInBounds()||(((target instanceof BaseShip && !kamikaze)) || target.getDistance(getEnemyBase()) < getEnemyBase().getMaxRange())
                || ((target.isDead() || target.getCenterX() > getAverageXOfUnits(getEnemiesInRadius(1000))))));
    }

    @Override
    public boolean attackBaseShipCondition() {
        return getPlayer().getMyUnits(Hivemind.class).size() >= 20 && super.attackBaseShipCondition();
    }

    protected void hurtRetreat()    {//later add hivemind healer check which increases percent shield check to 0.5
        if(!healQueue.contains(this)) {
            healQueue.add(this);

        }
        else {
            if(!hurt()) {
                healQueue.remove(this);
            }
        }
        if(getDistance(getNearestRelevantHealer()) < 400 ) {
            moveTo(getHomeBase());
        }
        else {
            moveTo(getNearestRelevantHealer());
        }
    }



    protected boolean hurt()    {
        return(this.getPercentPlating() < 0.3 ||
                (this.getPercentShield() < (unitInGroupHasWeapon(getPlayer().getMyUnits(Hivemind.class), ShieldBattery.class)?0.3:0.1) ||
                this.getPercentStructure() < 0.5));
    }

    private boolean unitInGroupHasWeapon(ArrayList<Unit> myUnits, Class<? extends Weapon> weapon) {
        for(Unit u: myUnits)    {
            if(u.hasWeapon(weapon)) {
                return true;
            }
        }
        return false;
    }

    protected void healHivemind()   {
        if(healTarget != null)    {

            if(getDistance(healTarget) > getWeaponTwo().getMaxRange()*0.9)  {
                moveTo(healTarget);
            }
            else if(getDistance(healTarget) < getWeaponTwo().getMinRange()*1.2) {
                moveAway(healTarget);
            }
            getWeaponTwo().use(healTarget);

            if(((healTarget.hasShield())?healTarget.getPercentShield() > 0.9:
                    (healTarget.hasPlating())?(healTarget.getPercentPlating() > 0.9 && healTarget.getPercentStructure() > 0.9) :
                    healTarget.getPercentStructure() > 0.9) || healTarget.isDead()) {
                healQueue.remove(healTarget);
                healTarget = null;
            }

        }
        else {
            if(healQueue.size() > 0) {
                healQueue.sort(new HurtComparator());
                for(GrimlockUnit u : healQueue) {
                    if(u instanceof Hivemind && (getWeaponTwo().getClass().equals(ShieldBattery.class))?u.hasShield():u.hasMaxStructure())   {
                        healTarget = u;
                        healQueue.remove(u);
                        return;
                    }
                }
            }
        }
    }


    public void draw(Graphics g)    {
        super.draw(g);
        g.setColor(Color.cyan);
        if(getNearestUnit(getPlayer(), Hivemind.class) != null) {
            lineTarget(g, getNearestUnit(getPlayer(), Hivemind.class));
        }
        g.setColor(getWeaponTwo().getClass().equals(ShieldBattery.class)? Color.blue: Color.green);
        if(healTarget != null) {
            lineTarget(g, healTarget);
        }
        g.setColor(Color.red);
        if(target != null) {
            lineTarget(g, target);
        }
        if(hurt() || kamikaze)  {
            g.setColor(!kamikaze?new Color(255, 255, 255,0.5f): new Color(255,0,0,0.5f));
            g.fillOval(this.getX(), this.getY(), this.getWidth(), this.getHeight());
        }
    }


}



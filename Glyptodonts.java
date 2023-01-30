package teams.student.evilGrimlock;

import components.upgrade.Structure;
import components.weapon.energy.LargeLaser;
import components.weapon.explosive.AntimatterMissile;
import components.weapon.utility.CommandRelay;
import objects.entity.unit.Frame;
import objects.entity.unit.Style;
import player.Player;

public class Glyptodonts extends GrimlockUnit
{
	float a;
	float b;
	float c;
	float d;
	float e;
	float f;

	public Glyptodonts(EvilGrimlock p)
	{
		super(p);
		a=p.getAverageEnemyMaxShield();
		b=p.getAverageEnemyMaxPlating();
		c=p.getAverageEnemyMaxStructure() * .4f;
		d=p.getTotalEnergyEnemies();
		e=p.getTotalKineticEnemies();
		f=p.getTotalExplosiveEnemies();
	}
	public Glyptodonts(Player p, GrimlockUnit captain) {
		super(p, captain);
	}

	public void design()
	{	
		setFrame(Frame.HEAVY);
		setStyle(Style.DAGGER);
		addWeapon(new LargeLaser(this));
		addWeapon(new AntimatterMissile(this));
		addUpgrade(new Structure(this));
	}
	
	public void shipBehavior() {
		if (getDistance(getNearestAlly()) < 50) {
			moveAway(getNearestAlly());

		}
		else {
			if (getDistance(captain) > 300) {
				moveTo(captain);
			} else if (getDistance(getNearestEnemyInBoundsExcludeBaseShip()) < getPlayer().greemlock.getDistance(getNearestEnemyInBoundsExcludeBaseShip())) {
				moveAway(getNearestEnemyInBounds());
			}
			else {
				skirmish(getWeaponOne(), getNearestEnemyInBounds());
				skirmish(getWeaponTwo(), getNearestEnemyInBounds());
				if (attackBaseShipCondition()) {
					skirmish(getWeaponOne(), getEnemyBase());
					skirmish(getWeaponTwo(), getEnemyBase());
				}
			}
			getWeaponOne().use(getNearestEnemyInBounds());
			getWeaponTwo().use(getNearestEnemyInBounds());
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

			skirmish(getWeaponOne(), getNearestEnemyInBoundsExcludeBaseShip());
			skirmish(getWeaponTwo(), getNearestEnemyInBoundsExcludeBaseShip());
		}
	}
	


}

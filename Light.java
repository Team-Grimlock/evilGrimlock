package teams.student.evilGrimlock;

import components.upgrade.Structure;
import components.weapon.explosive.ShadowflightMissile;
import objects.entity.unit.Frame;
import objects.entity.unit.Style;

public class Light extends GrimlockUnit
{
	float a;
	float b;
	float c;
	float d;
	float e;
	float f;
	
	public Light(EvilGrimlock p)
	{
		super(p);
		a=p.getAverageEnemyMaxShield();
		b=p.getAverageEnemyMaxPlating();
		c=p.getAverageEnemyMaxStructure() * .4f;
		d=p.getTotalEnergyEnemies();
		e=p.getTotalKineticEnemies();
		f=p.getTotalExplosiveEnemies();
	}
	
	public void design()
	{	
		setFrame(Frame.LIGHT);
		setStyle(Style.DAGGER);
		addWeapon(new ShadowflightMissile(this));
		addUpgrade(new Structure(this));
	}


	
	public void shipBehavior()
	{
			if(attackBaseShipCondition())	{
				skirmish(getWeaponOne(), getEnemyBase());
			}
			skirmish(getWeaponOne());
//			skirmish(getWeaponTwo());
	}

	


}

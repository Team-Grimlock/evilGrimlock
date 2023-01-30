package teams.student.evilGrimlock;

import components.weapon.explosive.LongRangeMissile;
import components.weapon.kinetic.MachineGun;
import objects.entity.unit.Frame;
import objects.entity.unit.Style;

public class Medium extends GrimlockUnit
{
	float a;
	float b;
	float c;
	float d;
	float e;
	float f;

	public Medium(EvilGrimlock p)
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
		setFrame(Frame.MEDIUM);
		setStyle(Style.ARROW);
		addWeapon(new LongRangeMissile(this));
		addWeapon(new MachineGun(this));
	}
	
	public void shipBehavior()
	{
		if(attackBaseShipCondition())	{
			skirmish(getWeaponOne(), getEnemyBase());
			skirmish(getWeaponTwo(), getEnemyBase());
		}
//		super.action();
			skirmish(getWeaponOne());
			skirmish(getWeaponTwo());
	}
	


}

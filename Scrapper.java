package teams.student.evilGrimlock;


import components.weapon.Weapon;
import components.weapon.resource.MiningLaser;
import objects.entity.node.Node;
import objects.entity.unit.Frame;
import objects.entity.unit.Style;
import objects.entity.unit.Unit;

public class Scrapper extends GrimlockUnit
{
	
	public Scrapper(EvilGrimlock p)
	{
		super(p);
	}
	
	public void design()
	{
		setFrame(Frame.MEDIUM);
		setStyle(Style.WEDGE);
		addWeapon(new MiningLaser(this));
		addWeapon(new MiningLaser(this));
	}

	public void shipBehavior()
	{
		super.action();
		harvest(getNearestNode(), getWeaponOne());
		harvest(getNearestNode(), getWeaponTwo());
	}
	
	
	public void harvest(Node n, Weapon w)
	{
		int r = w.getMaxRange();
		Unit enemy = getNearestEnemy();
		r = r*2;
		// Approach the node
		if(getDistance(enemy) > 2000) {
		if(getDistance(n) > w.getMaxRange())
		{
			moveTo(n);
		}
		//back up if too close
		else if(getDistance(n) < r * .9f)
		{
			moveTo(getHomeBase());
		}
		}
		else {
			if(enemy != null) {
			moveTo(enemy);
			w.use(enemy);
			}
		}
		
		
		
		w.use(n);
	}
}

	

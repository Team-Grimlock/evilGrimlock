package teams.student.evilGrimlock;


import components.upgrade.CargoBay;
import objects.GameObject;
import objects.entity.unit.Frame;
import objects.entity.unit.Style;
import objects.entity.unit.Unit;
import objects.resource.Resource;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Circle;

import java.util.ArrayList;


public class Diplodocus extends GrimlockUnit
{
	protected int flee;
	public static ArrayList<Resource> claimList = new ArrayList<Resource>();
	protected ArrayList<Resource> rQueue;
	protected Resource currentResource;
	public Diplodocus(EvilGrimlock p)
	{
		super(p);


	}
	public Diplodocus(EvilGrimlock p , GrimlockUnit captain)
	{
		super(p, captain);
	}

	public void init()	{
		super.init();
		rQueue = new ArrayList<Resource>();
		flee = 0;
	}
	
	public void design()
	{
		setFrame(Frame.MEDIUM);
		setStyle(Style.BUBBLE);
		addWeapon(new components.weapon.resource.Collector(this));
		addUpgrade(new CargoBay(this));
		addUpgrade(new CargoBay(this));
//		addUpgrade(new Shield(this));

	}

	public void shipBehavior()
	{
		if(flee > 0)	{
			returnResources();
			flee--;
			if(getDistance(getHomeBase()) < 100)	{
				flee = 0;
				claimList.removeAll(rQueue);
				rQueue.clear();
				locateResources();
			}
		}
		else {
			if (currentResource == null) {
				if (rQueue.size() > 0) {
					currentResource = rQueue.get(0);
				}
				locateResources();
				if (getEnemiesInRadius(2000).size() > 0) {
					flee = getEnemiesInRadius(2000).size() * 1200;
				}
			} else {
				if (getEnemiesInRadius(2000, Unit.class, currentResource.getPosition()).size() > 0) {
					flee = getEnemiesInRadius(2000).size() * 1200;
				}
				gatherResources();
			}
		}
	}

	public void draw(Graphics g)	{
		super.draw(g);
		drawPath(g);
	}

//	public void updateResourceQueue()	{
////		rQueue.removeIf(r -> r.wasPickedUp() || !r.isInBounds());
////		rQueue.sort(new CollectorComparator(this));
////		if(rQueue.size() < getOpenCapacity())	{
////			ArrayList<Resource> globalQueue = ResourceManager.resourceQueue;
////			globalQueue.sort(new CollectorComparator(this));
////			if(globalQueue.size() -1 > getOpenCapacity()) {
////				for (int i = 0; i < getOpenCapacity(); i++) {
////					if (!rQueue.contains(globalQueue.get(i))) {
////						rQueue.add(ResourceManager.resourceQueue.remove(i));
////					}
////				}
////			}
////		}
////		rQueue.sort(new CollectorComparator(this));
//	}
//
	public void gatherResources()
	{
		if(hasCapacity()) {


			if (currentResource.wasPickedUp() || !currentResource.isInBounds()) {
				rQueue.remove(currentResource);
				currentResource = null;
			} else {
				moveTo(currentResource);

//			Unit enemy = getNearestEnemy();
				if (getHomeBase().getDistance(currentResource) < this.getDistance(getHomeBase())) {
					moveTo(getHomeBase());
				}
				if (currentResource.getDistance(this) < this.getMaxRange() * 0.7f) {
					collect();

				}
			}

		}
		else {
			returnResources();
		}

	}

	protected void collect()
	{
		getWeaponOne().use();
	}

	public void returnResources()
	{
		moveTo(getHomeBase());
		if(getDistance(getHomeBase()) < 100) {
			deposit();
			currentResource = null;
			claimList.removeAll(rQueue);
			rQueue.clear();
			locateResources();
		}
	}

	protected void drawPath(Graphics g)	{
		g.setColor(Color.yellow);
		GameObject ref = this;
		for(int i = 0; i < rQueue.size()-1; i++) {
			g.drawLine(ref.getCenterX(), ref.getCenterY(), rQueue.get(i).getCenterX(), rQueue.get(i).getCenterY());
			g.getColor().a = g.getColor().a/2;
			ref = rQueue.get(i);
		}
	}

	protected void locateResources()	{
		GameObject ref = this;
		while(rQueue.size() < getOpenCapacity())	{
			Resource r = getNearestFreeResource(ref);
			if(r == null)	{
				return;
			}
			claimList.add(r);
			rQueue.add(r);
			ref = r;

		}
	}
	protected Resource getNearestFreeResource(GameObject o)	{
		float nearestDistance = Float.MAX_VALUE;
		Resource nearestResource = null;
		ArrayList<Resource> resources = getPlayer().getAllResources();

		for(Resource r : resources)
		{
			if((r.isInBounds() && o.getDistance(r.getPosition()) < nearestDistance) && !claimList.contains(r))
			{
				nearestResource = r;
				nearestDistance = o.getDistance(r);
			}
		}

		return nearestResource;
	}

	protected boolean resourceIsSafe(Resource resource)	{
		Circle path = new Circle((resource.getCenterX() + this.getCenterX())/2, (resource.getCenterY() + this.getCenterY())/2, getDistance(resource)/2);
		return path.contains(getNearestEnemyUnit().getCenterX(), getNearestEnemyUnit().getCenterY());
	}
}

//class CollectorComparator implements Comparator<Resource> {
//
//	protected  GrimlockUnit grimlockUnit;
//	protected CollectorComparator(GrimlockUnit grimlockUnit)	{
//		super();
//		this.grimlockUnit = grimlockUnit;
//	}
//
//	@Override
//	public int compare(Resource o1, Resource o2) {
//		float o1Dist = o1.getDistance(grimlockUnit);
//		float o2Dist = o2.getDistance(grimlockUnit);
//		if (o1Dist > o2Dist) {
//			return 1;
//		}
//		if (o2Dist > o1Dist) {
//			return -1;
//		}
//		return 0;
//	}
//}


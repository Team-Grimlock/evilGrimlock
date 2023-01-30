package teams.student.evilGrimlock;


import components.weapon.Weapon;
import components.weapon.resource.MiningLaser;
import objects.entity.node.Node;
import objects.entity.unit.Frame;
import objects.entity.unit.Style;
import objects.entity.unit.Unit;
import org.newdawn.slick.Graphics;

import java.util.ArrayList;

public class Orodromines extends GrimlockUnit {
	private int fleeTime;

	protected Node target;

	public Node getTarget() {
		return target;
	}

	public void setTarget(Node node) {
		this.target = node;
	}

	public Orodromines(EvilGrimlock p) {
		super(p);
	}

	public Orodromines(EvilGrimlock p, Snarl captain) {
		super(p);
		assignCaptain(captain);
	}


	public void design() {
		setFrame(Frame.MEDIUM);
		setStyle(Style.WEDGE);
		addWeapon(new MiningLaser(this));
		addWeapon(new MiningLaser(this));
	}


	public void shipBehavior() {
		if(fleeTime > 0)	{
			fleeTime--;
			if(getPercentEffectiveHealth() < 0.5)	{
				fleeTime = 6000;
				moveTo(getHomeBase());
				if(getDistance(getHomeBase()) < 50)	{
					fleeTime = 0;

				}
			}
			else if (!attackBaseShipCondition()) {
				kite(getNearestEnemyUnit());
				getWeaponOne().use(getNearestEnemyUnit());
				getWeaponTwo().use(getNearestEnemyUnit());
			}
		}
		else {
			if (target == null) {
				target = ((Snarl) captain).getTarget();
			} else {
				harvest(target, getWeaponOne());
				harvest(target, getWeaponTwo());
				if (target.isDead())
					target = null;
			}
			if (getDistance(getNearestEnemyUnit()) < 500) {
				getWeaponOne().use(getNearestEnemyUnit());
				getWeaponTwo().use(getNearestEnemyUnit());

			}
			if(getPercentEffectiveHealth() < 0.75) {
				fleeTime = 600;
			}
			if(captain.isDead())	{
				fleeTime = 2000;
			}
		}

	}

	public void draw (Graphics g){
		super.draw(g);

	}

	protected void locateNode () {
		//target = ResourceManager.nodeQueue.remove();
		ArrayList<Node> potentialNodes = new ArrayList<Node>();
		for(Node node : getPlayer().getAllNodes())	{
			if(getHomeBase().getDistance(node)*2 < getEnemyBase().getDistance(node))	{
				potentialNodes.add(node);
			}
		}
		if (!potentialNodes.isEmpty()) {
			Node candidate = potentialNodes.get(0);
			for (Node node : potentialNodes) {
				if (getDistance(node) < getDistance(candidate)) {
					candidate = node;
				}
			}
		}
		target = getHomeBase().getNearestNode();

	}


	public void harvest (Node n, Weapon w)
	{
		int r = w.getMaxRange();
		Unit enemy = getNearestEnemy();
		r = r * 2;
		// Approach the node

		if (getDistance(n) < w.getMinRange() * 1.5f) {
			moveTo(getHomeBase());
		} else {
			moveTo(n);
		}
		if (getDistance(n) < w.getMaxRange()) {
			w.use(n);
		}

	}
	@Override
	protected void init() {
		super.init();
		fleeTime = 0;
	}

	public int returnFleeTime() {
		return(fleeTime);
	}

}



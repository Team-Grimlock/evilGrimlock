package teams.student.evilGrimlock;

import components.upgrade.Plating;
import components.upgrade.Shield;
import components.upgrade.Structure;
import components.weapon.Weapon;
import components.weapon.energy.LargeLaser;
import components.weapon.explosive.InfernoLauncher;
import components.weapon.kinetic.Autocannon;
import components.weapon.utility.RepairBeam;
import components.weapon.utility.ShieldBattery;
import engine.states.Game;
import objects.GameObject;
import objects.entity.unit.Unit;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Point;
import player.Player;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public abstract class GrimlockUnit extends Unit
{
	public List<Tag> tags;

	protected static Point rally;

	public GrimlockUnit(Player p)
	{
		super(p);
		init();
	}
	public GrimlockUnit(Player p, GrimlockUnit captain)
	{
		super(p);
		init();
		assignCaptain(captain);
	}

	protected void init()	{
		timer = 0;
		tags = TagAssigner.tagKey.get(this.getClass());
		if(tags == null)	{
			tags = new ArrayList<Tag>();
		}
	}
	
	public EvilGrimlock getPlayer()
	{
		return (EvilGrimlock) super.getPlayer();
	}

	public Point getRally()	{
		return getPlayer().rally;
	}

	protected final float CIRCLING_RADIANS = (float) (Math.PI);

	protected GrimlockUnit captain;

	protected Unit target;
	protected GrimlockUnit healTarget;

	protected static ArrayList<GrimlockUnit> healQueue = new ArrayList<>();
	protected Weapon[][] Weapons = new Weapon[3][3];

	protected int timer = 0;


	public Unit getTargetEnemy()	{
		return target;
	}
	public void assignCaptain(GrimlockUnit captain)	{
		this.captain = captain;
	}

	public void action() 
	{
		timer++;
		if(target != null && !target.isInBounds())	{
			target = null;
		}
		if(!isInBounds())   {
			remainInBounds();
			return;
		}
		if(!attackBaseShipCondition() && tooCloseEnemyBase())   {
			moveTo(getHomeBase());
			return;
		}
		if(tags.contains(Tag.RESOURCE) || tags.contains(Tag.AGGRO))	{
			shipBehavior();
			return;
		}
		if(getPlayer().state == State.ATTACK) {
			shipBehavior();
		}
		else {
			defend();
		}
	}

	public void shipBehavior()	{

	}
	
	


	public void dynamicArmor()	{
		switch(getPlayer().mostDesiredArmour())	{
			case SHIELD : {
				addUpgrade(new Shield(this));
				break;
			}
			case PLATING : {
				addUpgrade(new Plating(this));
				break;
			}
			case STRUCTURE : {
				addUpgrade(new Structure(this));
				break;
			}
		}
	}
	
	public void dynamicWeapon()	{
		switch(getPlayer().mostDesiredWeapon())	{
			case ENERGY : {
				addWeapon(energyWeapon());
				break;
			}
			case KINETIC : {
				addWeapon(kineticWeapon());
				break;
			}
			case EXPLOSIVE : {
				addWeapon(explosiveWeapon());
				break;
			}
		}
	}


	public ArrayList<Unit> getUnitsSortedByDistance()	{
		ArrayList<Unit> units = getUnits(getPlayer());
		units.sort(new DistanceComparator(this));
		return units;
	}

	public Unit getNearestRelevantHealer()	{
		ArrayList<Unit> units = getUnitsSortedByDistance();
		for(Unit u : units)	{
			if(u.hasWeapon(ShieldBattery.class) && this.hasShield())	{
				return u;
			}
			else if(u.hasWeapon(RepairBeam.class)) {
				return u;
			}
		}
		return null;

	}

	public void dynamicHealing()	{
		int repairBeamDesire = ((getPlayer().getTotalArmour(getUnits(getPlayer()), Selectors.PLATING)) + (getPlayer().getTotalArmour(getUnits(getPlayer()), Selectors.STRUCTURE)))
				- getPlayer().countUnitsWithWeapon(getUnits(getPlayer()), RepairBeam.class);
		int shieldBatteryDesire = (getPlayer().getTotalArmour(getUnits(getPlayer()), Selectors.SHIELD))
				- getPlayer().countUnitsWithWeapon(getUnits(getPlayer()), ShieldBattery.class);

		if(repairBeamDesire < shieldBatteryDesire)	{
			addWeapon(new ShieldBattery(this));
			return;
		}
		addWeapon(new RepairBeam(this));

	}

	public Weapon explosiveWeapon()	{
		return new InfernoLauncher(this);
	}
	public Weapon energyWeapon()	{
		return new LargeLaser(this);
	}
	public Weapon kineticWeapon()	{
		return new Autocannon(this);
	}



	public boolean attackBaseShipCondition()	{
		return getEnemiesExcludeBaseShip().size() == 0 || getHomeBase().getDistance(getEnemyBase()) <= getHomeBase().getMaxRange() * 1.1f;
	}

	public Unit findBestTarget()	{
		Unit candidate = getNearestEnemyInBounds();


		return candidate;
	}
		
	public void attack(Weapon w)
	{
		Unit enemy = getNearestEnemyInBounds();

		if(enemy != null && w != null)
		{
			w.use(enemy);	
		}
	}
		
	public void fleeEnemy()
	{
		Unit enemy = getNearestEnemy();

		if(enemy != null)
		{		
			if(getDistance(enemy) < 1000) {
				moveTo(getHomeBase());
			}
		}
	}
	
	public void draw(Graphics g)
	{

		g.setColor(Color.white);
		g.drawString(tags.toString(), this.x, this.y);
		g.setColor(Color.red);
		if(captain != null) {
			g.drawLine(this.getCenterX(), this.getCenterY(), captain.getCenterX(), captain.getCenterY());
		}
		if(target != null)	{
			g.drawLine(this.getCenterX(), this.getCenterY(), target.getCenterX(), target.getCenterY());
		}
		if(healTarget != null) {
			g.setColor(Color.green);
			g.drawLine(this.getCenterX(), this.getCenterY(), healTarget.getCenterX(), healTarget.getCenterY());
		}
		g.setColor(Color.white);
		g.drawLine(this.getCenterX(), this.getCenterY(), (float) (this.getCenterX() + Math.cos(getTheta() * Math.PI/180) * 100), (float) (this.getCenterY() + Math.sin(getTheta()*Math.PI/180) * 100));
	}

	public void circle(GameObject obj, float min, float max, double radians) {
//        if(getDistance(obj) > max){
//            rotate(10);
//        }
		double centerX = obj.getCenterX();
		double myX = getCenterX();
		double centerY = obj.getCenterY();
		double myY = getCenterY();
        /*if (getDistance(obj) > max) {
            moveTo(obj);
        }*/
		if (getDistance(obj) > min && getDistance(obj) < max) {
			moveTo(new Point((float) (centerX + (myX - centerX) * Math.cos(1) - (myY - centerY) * Math.sin(radians)),
					(float) (centerY + (myX - centerX) * Math.sin(1) + (myY - centerY) * Math.cos(radians))));
		} else moveTo(obj);

        /*if(getDistance(obj) < min + 10){
            turnTo(obj);
            move(90);
        } else moveTo(obj);*/
	}

	public void kite(Unit target) {
		if (getDistance(target) > getMaxRange()) {
			moveTo(target);
		} else {
			moveTo(getHomeBase());
		}
	}

	public void follow(Unit unit)	{
		follow(unit, 100);
	}
	public void follow(Unit unit, int range)	{
		if(getDistance(unit) > range)	{
			moveTo(unit);
		}
	}
	public void follow(Point p, int range)	{
		if(getDistance(p) > range)	{
			moveTo(p);
		}
	}

	protected boolean tooClose(GameObject entity, Weapon w)	{
		return tooClose(entity, w.getMinRange()*1.1f);
	}
	protected boolean tooClose(GameObject entity, float distance)	{
		return getDistance(entity) < distance;
	}

	//Check if entity is out of effective max range (little lower than max range because at this range you can probably use the weapon)
	protected boolean outOfRange(Point p, float range)	{
		return range < getDistance(p);
	}
	protected boolean outOfRange(GameObject entity)	{
		return outOfRange(entity, getMaxRange()*0.9f);
	}
	protected boolean outOfRange(GameObject entity, Weapon w)	{
		return outOfRange(entity,w.getMaxRange()*0.9f);
	}
	protected boolean outOfRange(GameObject entity, float range)	{
		return outOfRange(entity.getPosition(), range);
	}

	public void spaceOut(float distance)   {
//		if (getDistance(getNearestAlly()) < distance) {
//			moveTo(new Point(getX() + 100, getY() + (Math.random() > 0.5 ? distance : -distance)));
//		}
//		if (getDistance(getNearestAlly()) > distance) {
//			turnTo((getHomeBase().getX() > 0) ? Game.getMapLeftEdge() : Game.getMapRightEdge(), this.getY());
//			move();
//		}
		if(getDistance(getNearestAlly()) < distance)	{
			moveAway(getNearestAlly());
		}
	}

	public void flank(Unit enemy) {

	}


	public void remainInBounds() {
		if (this.getPosition().getCenterX() <= Game.getMapLeftEdge() + 500) {
			if (this.getPosition().getCenterY() <= Game.getMapTopEdge() + 500) {
				turnTo(Game.getMapLeftEdge(), Game.getMapTopEdge());
				rotate((Math.random() > .5) ? 135 : 225);
				move();
			} else if (this.getPosition().getCenterY() >= Game.getMapBottomEdge() - 500) {
				turnTo(0, Game.getMapBottomEdge());
				rotate((Math.random() > .5) ? 140 : 220);
				move();
			} else {
				if (this.getRotation() < 180) {
					move(270);
				} else {
					move(90);
				}
			}
		} else if (this.getPosition().getX() >= Game.getMapRightEdge() - 500) {
			if (this.getPosition().getY() >= Game.getMapBottomEdge() - 500) {
				turnTo(Game.getMapRightEdge(), Game.getMapBottomEdge());
				rotate((Math.random() > .5) ? 135 : 225);
				move();
			}
			if (this.getPosition().getY() <= Game.getMapTopEdge() + 500) {
				turnTo(Game.getMapRightEdge(), Game.getMapTopEdge());
				rotate((Math.random() > .5) ? 140 : 220);
				move();
			} else {
				if (this.getRotation() < 180) {
					move(270);
				} else {
					move(90);
				}
			}
		} else {
			if (this.getPosition().getY() <= Game.getMapTopEdge() + 500 || this.getPosition().getY() >= Game.getMapBottomEdge() - 500) {
				{
					if (this.getRotation() < 180) {
						move(270);
					} else {
						move(90);
					}
				}
			}
		}
	}

	protected void skirmish(Weapon w) {
		skirmish(w, getNearestEnemyInBounds());
	}
	protected void skirmish(Weapon w, Unit enemy) {
		if(enemy != null)
		{
			target = enemy;

		}
		if(w != null) {
			w.use(target);

			if (getDistance(target) > w.getMaxRange() * 0.5) {
				moveTo(target);
			} else {
				moveTo(getHomeBase());
			}
		}
	}

	public void moveAway(GameObject obj)	{
		turnTo(obj);
		turnAround();
		move();
	}

	public float getAverageDistanceOfUnits(ArrayList<Unit> units)	{
		float result = 0;
		for(Unit u: units)	{
			result += getDistance(u);
		}
		return result/units.size();

	}
	public float getAverageXOfUnits(ArrayList<Unit> units)	{
		float result = 0;
		for(Unit u: units)	{
			result += u.getCenterX();
		}
		return result/units.size();

	}
	public Unit getClosestEnemyByX()	{
		return getClosestEnemyByX(5000);

	}
	public Unit getClosestEnemyByX(float radius)	{
		ArrayList<Unit> enemies = getEnemiesInRadius(radius);
		enemies.sort(new XComparator());
		return enemies.size() > 0? enemies.get(0):null;

	}

	public boolean overextending() {
	 	return getAlliesInRadius(500).size() < getEnemiesInRadius(500).size();
	}

	public Unit getNearestEnemyInBounds()	{//rewrote so ignores out of bounds enemies
		float nearestDistance = Float.MAX_VALUE;
		Unit nearestUnit = null;
		ArrayList<Unit> units =  getEnemies();

		for(Unit u : units)
		{
			if(u.isInBounds() && getDistance(u) < nearestDistance)
			{
				nearestUnit = u;
				nearestDistance = getDistance(u);

			}
		}

		return nearestUnit;
	}
	public Unit getNearestEnemyInBoundsWithWeapon(Class<? extends Weapon> weapon)	{//rewrote so ignores out of bounds enemies
		float nearestDistance = Float.MAX_VALUE;
		Unit nearestUnit = null;
		ArrayList<Unit> units =  getEnemiesExcludeBaseShip();

		for(Unit u : units)
		{
			if(u.isInBounds() && getDistance(u) < nearestDistance && u.hasWeapon(weapon))
			{
				nearestUnit = u;
				nearestDistance = getDistance(u);

			}
		}

		return nearestUnit;
	}

	public Unit getNearestEnemyInBoundsWithoutWeapon(Class<? extends Weapon> weapon)	{
		float nearestDistance = Float.MAX_VALUE;
		Unit nearestUnit = null;
		ArrayList<Unit> units =  getEnemiesExcludeBaseShip();

		for(Unit u : units)
		{
			if(u.isInBounds() && getDistance(u) < nearestDistance && !u.hasWeapon(weapon))
			{
				nearestUnit = u;
				nearestDistance = getDistance(u);

			}
		}

		return nearestUnit;
	}

	public Unit getNearestEnemyInBoundsExcludeBaseShip()	{//rewrote so ignores out of bounds enemies
		float nearestDistance = Float.MAX_VALUE;
		Unit nearestUnit = null;
		ArrayList<Unit> units =  getEnemiesExcludeBaseShip();

		for(Unit u : units)
		{
			if(u.isInBounds() && getDistance(u) < nearestDistance)
			{
				nearestUnit = u;
				nearestDistance = getDistance(u);

			}
		}

		return nearestUnit;
	}


	public boolean tooCloseEnemyBase()	{
		return getDistance(getEnemyBase()) < getEnemyBase().getMaxRange() * 1.2f;
	}

	public boolean allyHasTarget(GrimlockUnit ally)	{
		return ally.getTargetEnemy() != null;
	}

	public void defend()	{
		if(getEnemiesInRadius(getMaxRange()).size() > 0) {
			skirmish(getWeaponOne());
			skirmish(getWeaponTwo());
		}
		moveTo(getHomeBase());


	}


	public void circle(Unit u)    {//wow i didnt expect such a good circle method out of me trying to have a ship go around another ok its not that good WERUUFIUSFDS
		turnTo(u);
		float uTheta = getRotation();
		float rotate = (uTheta+45)  ;
		turnTo(rotate);
		move();
	}

	public void goAround(Unit u, Unit tar)    {//im losing my mind
		turnTo(u);
		float uTheta = getRotation();
		turnTo(tar);
		float targetTheta = getRotation();
//		float rotate = (targetTheta+());
		turnTo(tar);
		rotate((targetTheta-uTheta)/3);
		move();

	}

	
	
	protected Unit getNearestAllyUnit(EvilGrimlock p, Tag tag)	{
		float distance = Integer.MAX_VALUE;
		Unit result = getNearestAllyUnit();
		for (Unit unit:getAlliesExcludeBaseShip())	{
			if(((GrimlockUnit) unit).tags!=null &&((GrimlockUnit) unit).tags.contains(tag)	)	{
				if(getDistance(unit) < distance)	{
					result= unit;
					distance = getDistance(unit);
				}
			}
			
		}
		return result;
	}
	
	//returns a sorted queue of hurt allies that belong to a class from most percent hurt to least percent hurt
//	protected PriorityQueue<GrimlockUnit> getHurtAlliesSorted(int radius, Class<? extends Unit> clazz) {
//		ArrayList<GrimlockUnit> hurtAllies = new ArrayList<>();
//		getAlliesInRadius(radius, clazz).forEach(u -> {
//			if(u.isAlive() && u.getPercentEffectiveHealth() < 1)	{
//				hurtAllies.add((GrimlockUnit) u);
//			}
//		});
//		PriorityQueue<GrimlockUnit> hurtAlliesSorted =  new PriorityQueue<GrimlockUnit>(new HurtComparator());
//		hurtAllies.addAll(hurtAlliesSorted);
//		return hurtAlliesSorted;
//	}
//
	protected void requestHealing()	{
		if(!healQueue.contains(this)) {
			healQueue.add(this);
			healQueue.sort(new HurtComparator());
		}
	}

	public Unit getClosestUnitWithWeapon(ArrayList<Unit> group, Class<? extends Weapon> weapon)	{
		group.sort(new DistanceComparator(this));
		for(Unit u : group)	{
			if(u.hasWeapon(weapon))	{
				return u;
			}
		}
		return null;
	}

	public float getXDistance(GameObject obj)	{
		return Math.abs(this.x - obj.getX());
	}
	public float getYDistance(GameObject obj)	{
		return Math.abs(this.y - obj.getY());
	}

//
//	protected PriorityQueue<GrimlockUnit> getHurtAlliesSorted(int radius) {
//		return getHurtAlliesSorted(radius, GrimlockUnit.class);
//	}

	public void lineTarget(Graphics g, GameObject gameObject)	{
		if(gameObject != null)	{
			g.drawLine(this.getCenterX(), this.getCenterY(), gameObject.getCenterX(), gameObject.getCenterY());
		}
	}
}

class DistanceComparator implements Comparator<Unit>	{
	protected  GrimlockUnit grimlockUnit;
	protected DistanceComparator(GrimlockUnit grimlockUnit)	{
		super();
		this.grimlockUnit = grimlockUnit;
	}
	@Override
	public int compare(Unit o1, Unit o2)	{
		if(o1.getDistance(grimlockUnit) < o2.getDistance(grimlockUnit)) {
			return 1;
		}
		if(o1.getDistance(grimlockUnit) > o2.getDistance(grimlockUnit)) {
			return -1;
		}
		return 0;
	}
}

class XComparator implements Comparator<Unit>	{
	@Override
	public int compare(Unit o1, Unit o2)	{
		if(o1.getCenterX() < o2.getCenterX()) {
			return 1;
		}
		if(o1.getCenterX() > o2.getCenterX()) {
			return -1;
		}
		return 0;
	}
}

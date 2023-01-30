package teams.student.evilGrimlock;

import components.DamageType;
import components.weapon.Weapon;
import components.weapon.WeaponType;
import components.weapon.resource.Collector;
import components.weapon.resource.MiningLaser;
import objects.GameObject;
import objects.entity.unit.BaseShip;
import objects.entity.unit.Unit;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Point;
import player.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EvilGrimlock extends Player
{
//	public ClumpManager clumpy;
	//TODO CLUMP YOUR FLEET DUMBASS

	public Map<Selectors, Float> enemyComp;

	public Map<Selectors, Float> allyComp;

	private boolean noStartingResources;

	public Snarl resCaptain;

	public Grimlocke greemlock;

	public State state;

	public int timer;

	public Point rally;



	public void setup()
	{		
		setName("Evil Grimlock");
		setTitle("Not Roar lmfao");
		setTeamImage("src/teams/student/evilGrimlock/evilGrimlock.png");

		setColorPrimary(48, 213, 200);
		setColorSecondary(255, 255, 0);
		setColorAccent(255, 0, 0);

		timer = 0;
		enemyComp = new HashMap<>();
		allyComp = new HashMap<>();
		noStartingResources = true;
//		clumpy = new ClumpManager(this);
	}
	
	public void strategy() 
	{
//		clumpy.update();

		if(getFighterCount() < 10)	{
			this.state = State.DEFEND;
		}
		else {
			this.state = State.ATTACK;
		}

		if(timer == 0)	{
			rally = getMyBase().getPosition();
			ResourceManager.init(this);
			if(!getAllNodes().isEmpty()) {
				Strategies.buildEarlyMinorFleet(this);
			}
			if(getAllResources().size() > 0) {
				buildUnit(new Diplodocus(this));
				buildUnit(new Diplodocus(this));
				noStartingResources = false;
			}
			buildUnit(new Oviraptae(this));
		}
		if(ResourceManager.resourceQueue.size() == 0)	{
			ResourceManager.reloadResourceQueue(this);
		}

		if(timer > 50) {
			if(getFleetCount(Oviraptae.class) < 5 && timer < 4000) {
				buildUnit(new Oviraptae(this));
			}
			if (getFleetCount(Orodromines.class) < 5 && getAllNodes().size() > 0) {
				Strategies.buildMinorFleet(this);
			}

			if(timer > 3000) {

				if(getFleetCount(Grimlocke.class) < 1)	{
					greemlock = new Grimlocke(this);
					buildUnit(greemlock);
				}
				else {
					if (getFleetValuePercentage(Glyptodonts.class) < 0.3) {

						buildUnit(new Glyptodonts(this, greemlock));
					}
					if (getFleetValuePercentage(Pterasaur.class) < 0.2) {
						buildUnit(new Pterasaur(this, greemlock));
					}
					if (getFleetValuePercentage(Sludge.class) < 0.3) {
						buildUnit(new Sludge(this, greemlock));
					}

					if ((getFleetCount(Triceratops.class) < 5) && (getFleetCount(Triceratops.class) == 0 || getFighterCount() / (getFleetCount(Triceratops.class) == 0 ? 1 : getFleetCount(Triceratops.class)) > 1)) {
						buildUnit(new Triceratops(this, greemlock));
					}

					if(getFleetCount(Tyrannosaurus.class) < 1)	{
						buildUnit(new Tyrannosaurus(this));
					}
					if(getFleetCount(LightningClaw.class) < 2)	{
						buildUnit(new LightningClaw(this));
					}
//					if(getFleetCount(BlackHole.class) < 1)	{
//						buildUnit(new BlackHole(this));
//					}
//					if(getFleetCount(Kamikaze.class) < 3)	{
//						buildUnit(new Kamikaze(this));
//					}
				}
				if(getFighterCount() > 15) {
					if (getFleetCount(Orodromines.class) < 8) {
						buildUnit(new Orodromines(this, resCaptain));
					}
					if (getFleetCount(TerritorialDiplodocus.class) < 6) {
						buildUnit(new TerritorialDiplodocus(this, resCaptain));
					}
					if (getFleetCount(Allosaurus.class) < 5) {
						buildUnit(new Allosaurus(this, resCaptain));
					}
					if (getFleetCount(HomeBaseCollector.class) < 1) {
						buildUnit(new HomeBaseCollector(this));
					}
				}

				if(getFleetCount(Grimlocke.class) < 1) {
					buildUnit(new Grimlocke(this));
				}


                if ((getFleetCount(Triceratops.class) < 5) && (getFleetCount(Triceratops.class) == 0 || getFighterCount() / (getFleetCount(Triceratops.class) == 0 ? 1 : getFleetCount(Triceratops.class)) > 1)) {
                    buildUnit(new Triceratops(this));
                }

				if (getFleetValuePercentage(Glyptodonts.class) < 0.3) {

					buildUnit(new Glyptodonts(this));
				}

				if (getFleetValuePercentage(Sludge.class) < 0.3) {
					buildUnit(new Sludge(this));
				}

				if (getFleetCount(Pterasaur.class) < 0.3) {
					buildUnit(new Pterasaur(this));
				}
			}
		}
		if (getFleetCount(Diplodocus.class) < 4 && !noStartingResources) {
			buildUnit(new Diplodocus(this));
		}

		timer++;
	}



	public int getFighterCount()	{
		int total = 0;
		ArrayList<Unit> units = getEnemyBase().getEnemiesExcludeBaseShip();

		for (Unit u : units) {
			if (((GrimlockUnit)u).tags.contains(Tag.FIGHTER)) {
				total++;
			}

		}
		return total;
	}

	public int getFleetCount(Class<? extends GrimlockUnit> clazz) {
		int total = 0;
		ArrayList<Unit> units = getMyUnits();

		for (Unit u : units) {
			if (clazz.isInstance(u)) {
				total++;
			}

		}
		return total;
	}


	//looks at enemy weapon composition and sees how closely self composition matches those weapons and which types are underdeveloped
	public Selectors mostDesiredArmour()	{
		gatherAllyComposition();
		gatherEnemyComposition();
		int plating = (int) (enemyComp.get(Selectors.ENERGY) - allyComp.get(Selectors.PLATING));
		int shield = (int) (enemyComp.get(Selectors.KINETIC) - allyComp.get(Selectors.ENERGY));
		int structure = (int) ( enemyComp.get(Selectors.EXPLOSIVE) - allyComp.get(Selectors.STRUCTURE));
		if(shield > plating && shield >= structure)	{
			return Selectors.SHIELD;
		}
		return (plating >= structure)?Selectors.PLATING: Selectors.STRUCTURE;
	}

	public DamageType mostDesiredWeapon()	{
		gatherAllyComposition();
		gatherEnemyComposition();
		int energy = (int) (enemyComp.get(Selectors.SHIELD) - allyComp.get(Selectors.ENERGY));
		int kinetic = (int) (enemyComp.get(Selectors.PLATING) - allyComp.get(Selectors.KINETIC));
		int explosion = (int) ((enemyComp.get(Selectors.STRUCTURE)) - allyComp.get(Selectors.EXPLOSIVE));
		if(energy > kinetic && energy >= explosion)	{
			return DamageType.ENERGY;
		}
		return (kinetic >= explosion)?DamageType.KINETIC: DamageType.EXPLOSIVE;
	}

	protected void gatherEnemyComposition()	{
		enemyComp.put(Selectors.SHIELD, (float) getTotalArmour(getEnemyUnits(),Selectors.SHIELD));
		enemyComp.put(Selectors.PLATING, (float)  getTotalArmour(getEnemyUnits(),Selectors.PLATING));
		enemyComp.put(Selectors.STRUCTURE, (float)  getTotalArmour(getEnemyUnits(),Selectors.STRUCTURE));
		enemyComp.put(Selectors.ENERGY, (float) getTotalEnergyEnemies());
		enemyComp.put(Selectors.KINETIC, (float) getTotalKineticEnemies());
		enemyComp.put(Selectors.EXPLOSIVE, (float) getTotalExplosiveEnemies());
	}
	protected void gatherAllyComposition()	{
		allyComp.put(Selectors.SHIELD, (float) getTotalArmour(getMyUnits(),Selectors.SHIELD));
		allyComp.put(Selectors.PLATING, (float)  getTotalArmour(getMyUnits(),Selectors.PLATING));
		allyComp.put(Selectors.STRUCTURE, (float)  getTotalArmour(getMyUnits(),Selectors.STRUCTURE));
		allyComp.put(Selectors.ENERGY, (float) getTotalDamage(getMyUnits(), DamageType.ENERGY));
		allyComp.put(Selectors.KINETIC, (float) getTotalDamage(getMyUnits(), DamageType.KINETIC));
		allyComp.put(Selectors.EXPLOSIVE, (float) getTotalDamage(getMyUnits(), DamageType.EXPLOSIVE));
	}

	public int getTotalArmour(ArrayList<Unit> team, Selectors type)	{
		int total = 0;
		for(Unit e : team)
		{
			if(!(e instanceof BaseShip || (e.hasWeapon(Collector.class)|| e.hasWeapon(MiningLaser.class))))
			{
				switch (type)	{
					case SHIELD : {
						total += e.getMaxShield()/300;
						break;
					}
					case PLATING : {
						total += e.getMaxPlating()/300;
						break;
					}
					case STRUCTURE : {
						total += (e.getMaxStructure()/300) - 1;
						break;
					}
				}
			}

		}
		return total;
	}

	public int getTotalDamage(ArrayList<Unit> team, DamageType type)	{
		int total = 0;
		for(Unit e : team)
		{
			if(!(e instanceof BaseShip || (e.hasWeapon(Collector.class)|| e.hasWeapon(MiningLaser.class))))
			{
				switch (type)	{
					case ENERGY : {
						total += (e.getWeaponOne().getWeaponType().equals(WeaponType.ENERGY)? scoreWeapon(e.getWeaponOne().getClass()) : 0) +
								((e.getWeaponTwo() != null)?(e.getWeaponTwo().getWeaponType().equals(WeaponType.ENERGY)? scoreWeapon(e.getWeaponTwo().getClass()) : 0): 0);
						break;
					}
					case KINETIC : {
						total += (e.getWeaponOne().getWeaponType().equals(WeaponType.KINETIC)? scoreWeapon(e.getWeaponOne().getClass()) : 0) +
								((e.getWeaponTwo() != null)?(e.getWeaponTwo().getWeaponType().equals(WeaponType.KINETIC)? scoreWeapon(e.getWeaponTwo().getClass()) : 0): 0);
						break;
					}
					case EXPLOSIVE : {
						total += (e.getWeaponOne().getWeaponType().equals(WeaponType.EXPLOSIVE)? scoreWeapon(e.getWeaponOne().getClass()) : 0) +
								((e.getWeaponTwo() != null)?(e.getWeaponTwo().getWeaponType().equals(WeaponType.EXPLOSIVE)? scoreWeapon(e.getWeaponTwo().getClass()) : 0): 0);
						break;
					}
				}
			}
		}
		return total;
	}


	public int scoreWeapon(Class<? extends Weapon> weapon)	{
		return Constants.weaponScore.get(weapon);
	}
	public int scoreWeapon(Weapon weapon)	{
		if(weapon == null)	{
			return 0;
		}
		return Constants.weaponScore.get(weapon.getClass());
	}

	public int getAverageArmour(ArrayList<Unit> team, Selectors type)
	{
		return Math.round((float)getTotalArmour(team, type)/team.size());
	}
	public int getAverageDamage(ArrayList<Unit> team, DamageType type)
	{
		return Math.round((float) getTotalDamage(team, type));
	}
	
	public int getAverageEnemyMaxShield()
	{
		return getAverageArmour(getEnemyUnits(),Selectors.SHIELD);
	}
	
	public int getAverageEnemyMaxPlating()
	{
		return getAverageArmour(getEnemyUnits(),Selectors.PLATING);
	}
	
	public int getAverageEnemyMaxStructure()
	{
		return getAverageArmour(getEnemyUnits(),Selectors.STRUCTURE);
	}
	
	public int getTotalEnergyEnemies()
	{
		return getTotalDamage(getEnemyUnits(), DamageType.ENERGY);
	}
	
	public int getTotalKineticEnemies()
	{
		return getTotalDamage(getEnemyUnits(), DamageType.KINETIC);
	}
	
	public int getTotalExplosiveEnemies()
	{
		return getTotalDamage(getEnemyUnits(), DamageType.EXPLOSIVE);
	}


	public int countUnitsWithWeapon(ArrayList<Unit> team, Class<? extends Weapon> weapon)	{
		int counter = 0;
		for(Unit u : team)	{
			if(u.hasWeapon(weapon));
			counter++;
		}
		return counter;
	}


	public boolean unitInGroupHasWeapon(ArrayList<Unit> group, Class<? extends Weapon> weapon)	{
		for(Unit u : group)	{
			if(u.hasWeapon(weapon))	{
				return true;
			}
		}
		return false;
	}

	public float groupAverageDistance(ArrayList<Unit> group, GameObject go)	{
		if(go == null)	{
			return 0;
		}
		float total = 0;
		for(Unit u : group)	{
			total+= u.getDistance(go.getPosition());
		}
		return total/group.size();
	}


			
	public void draw(Graphics g) 
	{
//		addMessage("Enemy Shield : " + String.valueOf(enemyComp.get(Selectors.SHIELD)));
//		addMessage("Enemy Plating : " + String.valueOf(enemyComp.get(Selectors.PLATING)));
//		addMessage("Enemy Structure : " + String.valueOf(enemyComp.get(Selectors.STRUCTURE)));
//		addMessage("Ally Shield : " + String.valueOf(allyComp.get(Selectors.SHIELD)));
//		addMessage("Ally Plating : " + String.valueOf(allyComp.get(Selectors.PLATING)));
//		addMessage("Ally Structure : " + String.valueOf(allyComp.get(Selectors.STRUCTURE)));
//		addMessage("Enemy Energy : " + String.valueOf(enemyComp.get(Selectors.ENERGY)));
//		addMessage("Enemy Kinetic : " + String.valueOf(enemyComp.get(Selectors.KINETIC)));
//		addMessage("Enemy Explosive : " + String.valueOf(enemyComp.get(Selectors.EXPLOSIVE)));
//		addMessage("Ally Energy : " + String.valueOf(allyComp.get(Selectors.ENERGY)));
//		addMessage("Ally Kinetic : " + String.valueOf(allyComp.get(Selectors.KINETIC)));
//		addMessage("Ally Explosive : " + String.valueOf(allyComp.get(Selectors.EXPLOSIVE)));
		addMessage("Fighter Count : " + String.valueOf(getFighterCount()));
		addMessage("State : " + state);
//		addMessage("Ally Clumps : " + String.valueOf(clumpy.allyClumps.size()));
//		addMessage("Enemy Clumps : " + String.valueOf(clumpy.enemyClumps.size()));
		g.setColor(Color.white);
		if(GrimlockUnit.rally != null) {
			g.fill(new Circle(GrimlockUnit.rally.getCenterX(), GrimlockUnit.rally.getCenterY(), 50));
		}
//		clumpy.draw(g);

	}
	
}

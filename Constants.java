package teams.student.evilGrimlock;

import components.weapon.Weapon;
import components.weapon.energy.*;
import components.weapon.explosive.*;
import components.weapon.kinetic.*;
import components.weapon.resource.Collector;
import components.weapon.resource.MiningLaser;
import components.weapon.utility.*;

import java.util.HashMap;

public class Constants {
    public static HashMap<Class<? extends Weapon>, Integer> weaponScore;
    static {
        weaponScore = new HashMap<>();
        weaponScore.put(Collector.class, 0);
        weaponScore.put(MiningLaser.class, 0);
        weaponScore.put(RepairBeam.class, 3);
        weaponScore.put(ShieldBattery.class, 2);
        weaponScore.put(CommandRelay.class, 3);
        weaponScore.put(ElectromagneticPulse.class, 2);
        weaponScore.put(GravitationalRift.class, 1);
        weaponScore.put(Pullbeam.class, 2);
        weaponScore.put(Aegis.class, 1);
        weaponScore.put(AntiMissileSystem.class, 1);


        weaponScore.put(SmallLaser.class, 1);
        weaponScore.put(LargeLaser.class, 2);
        weaponScore.put(Brightlance.class, 3);
        weaponScore.put(LaserBattery.class, 3);
        weaponScore.put(EnergySiphon.class, 1);
        weaponScore.put(PulseCannon.class, 2);

        weaponScore.put(MachineGun.class, 1);
        weaponScore.put(Autocannon.class, 2);
        weaponScore.put(Megacannon.class, 3);
        weaponScore.put(FlakBattery.class, 1);
        weaponScore.put(MassDriver.class, 2);
        weaponScore.put(Railgun.class, 3);

        weaponScore.put(ShortRangeMissile.class, 1);
        weaponScore.put(LongRangeMissile.class, 2);
        weaponScore.put(AntimatterMissile.class, 3);
        weaponScore.put(ShadowflightMissile.class, 2);
        weaponScore.put(InfernoLauncher.class, 2);
        weaponScore.put(CataclysmRocket.class, 3);
    }


}

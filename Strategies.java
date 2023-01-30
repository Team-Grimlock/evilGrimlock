package teams.student.evilGrimlock;


public class Strategies {
    public static void buildSwarm(EvilGrimlock p) {

        // COST  21-28

            p.buildUnit(new Hivemind(p));
            p.buildUnit(new Hivemind(p));
            p.buildUnit(new Hivemind(p));
            p.buildUnit(new Triceratops(p));

    }

    public static void buildFuegoGrav(EvilGrimlock p)   {
        if(p.getMinerals() > 14)
        p.buildUnit(new Fuegolol(p));
        p.buildUnit(new BlackHole(p));
    }


    public static void buildEarlyMinorFleet(EvilGrimlock p) {
        if(p.getFleetCount(Snarl.class) < 1)    {
            p.resCaptain = new Snarl(p);
            p.buildUnit(p.resCaptain);

        }

        p.buildUnit(new Orodromines(p, p.resCaptain));
        p.buildUnit(new Orodromines(p, p.resCaptain));
        p.buildUnit(new Orodromines(p, p.resCaptain));
        p.buildUnit(new TerritorialDiplodocus(p, p.resCaptain));
        p.buildUnit(new TerritorialDiplodocus(p, p.resCaptain));
        p.buildUnit(new Allosaurus(p, p.resCaptain));
        p.buildUnit(new Allosaurus(p, p.resCaptain));
//            p.buildUnit(new Allosaurus(p, p.resCaptain));
//            p.buildUnit(new Allosaurus(p, p.resCaptain));
//            p.buildUnit(new Allosaurus(p, p.resCaptain));
//            p.buildUnit(new Allosaurus(p, p.resCaptain));
//            p.buildUnit(new Allosaurus(p, p.resCaptain));

    }

    public static void buildTankFleet(EvilGrimlock p) {

        p.buildUnit(new Sludge(p));//9
        p.buildUnit(new Sludge(p));//9
        p.buildUnit(new Sludge(p));//9
        p.buildUnit(new Triceratops(p));//7
        p.buildUnit(new Triceratops(p));//7
        p.buildUnit(new Triceratops(p));//7
        p.buildUnit(new Glyptodonts(p));//10
        p.buildUnit(new Glyptodonts(p));//10
        p.buildUnit(new Glyptodonts(p));//10
        p.buildUnit(new Pterasaur(p));//10
        p.buildUnit(new Pterasaur(p));//10
        p.buildUnit(new Pterasaur(p));//10
        p.buildUnit(new BlackHole(p));
        p.buildUnit(new Grimlocke(p));

    }


    //COST 24
    public static void buildMinorFleet(EvilGrimlock p){
        if(p.getFleetCount(Snarl.class) < 1)    {
            p.resCaptain = new Snarl(p);
            p.buildUnit(p.resCaptain);

        }
            p.buildUnit(new Orodromines(p, p.resCaptain));
            p.buildUnit(new Orodromines(p, p.resCaptain));
            p.buildUnit(new Allosaurus(p, p.resCaptain));
            p.buildUnit(new TerritorialDiplodocus(p, p.resCaptain));
            p.buildUnit(new TerritorialDiplodocus(p, p.resCaptain));
            p.buildUnit(new TerritorialDiplodocus(p, p.resCaptain));

    }

    //COST
    public static void buildBasicFleet(EvilGrimlock p) {

    }
}

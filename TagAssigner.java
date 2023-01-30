package teams.student.evilGrimlock;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class TagAssigner {
    public static HashMap<Class<? extends GrimlockUnit>, List<Tag>> tagKey;
    static  {
        tagKey = new HashMap<Class<? extends GrimlockUnit>, List<Tag>>();
        tagKey.put(Diplodocus.class, Arrays.asList(Tag.RESOURCE));
        tagKey.put(Orodromines.class, Arrays.asList(Tag.RESOURCE));
        tagKey.put(TerritorialDiplodocus.class, Arrays.asList(Tag.RESOURCE));
        tagKey.put(Allosaurus.class, Arrays.asList(Tag.RESOURCE));
        tagKey.put(Snarl.class, Arrays.asList(Tag.RESOURCE));
        tagKey.put(Hivemind.class, Arrays.asList(Tag.FIGHTER));
        tagKey.put(Light.class, Arrays.asList(Tag.FIGHTER));
        tagKey.put(Medium.class, Arrays.asList(Tag.FIGHTER));
        tagKey.put(Pterasaur.class, Arrays.asList(Tag.FIGHTER));
        tagKey.put(Glyptodonts.class, Arrays.asList(Tag.FIGHTER));
        tagKey.put(BlackHole.class, Arrays.asList(Tag.FIGHTER));
        tagKey.put(Fuegolol.class, Arrays.asList(Tag.FIGHTER));
        tagKey.put(GrimlockUnit.class, Arrays.asList(Tag.ERROR));
        tagKey.put(Kamikaze.class, Arrays.asList(Tag.SPECIAL));
        tagKey.put(Oviraptae.class, Arrays.asList(Tag.AGGRO));
        tagKey.put(Triceratops.class, Arrays.asList(Tag.HEALER));
        tagKey.put(HomeBaseCollector.class, Arrays.asList(Tag.RESOURCE));
        tagKey.put(RKelly.class, Arrays.asList(Tag.AGGRO));

        tagKey.put(Sludge.class, Arrays.asList(Tag.FIGHTER));

    }
}

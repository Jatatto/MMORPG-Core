package me.jwhz.mmorpgcore.utils.materials;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;

/*
    UMaterial Version: 8

    This software is created and owned by RandomHashTags, and is licensed under the GNU Affero General Public License v3.0 (https://choosealicense.com/licenses/agpl-3.0/)
    You can only find this software at https://gitlab.com/RandomHashTags/umaterial
    You can find RandomHashTags on
        Discord - RandomHashTags#1948
        Discord Server - https://discord.gg/CPTsc5X
        Dlive - https://dlive.tv/RandomHashTags
        Email - imrandomhashtags@gmail.com
        GitHub - https://github.com/RandomHashTags
        GitLab - https://gitlab.com/RandomHashTags
        MCMarket - https://www.mc-market.org/members/20858/
        Minecraft - RandomHashTags
        Mixer - https://mixer.com/randomhashtags
        PayPal - imrandomhashtags@gmail.com
        Reddit - https://www.reddit.com/user/randomhashtags/
        SpigotMC - https://www.spigotmc.org/members/76364/
        Spotify - https://open.spotify.com/user/randomhashtags
        Stackoverflow - https://stackoverflow.com/users/12508938/
        Subnautica Mods - https://www.nexusmods.com/users/77115308
        Twitch - https://www.twitch.tv/randomhashtags/
        Twitter - https://twitter.com/irandomhashtags
        YouTube - https://www.youtube.com/channel/UC3L6Egnt0xuMoz8Ss5k51jw
 */
class UPotion implements Versionable {
    private final UMaterial.PotionBase base;
    private final ItemStack potion;
    private final PotionType type;
    private final Object potiondata;
    public UPotion(UMaterial.PotionBase base, String type, boolean extended, boolean upgraded) {
        this.base = base;
        type = type.toUpperCase();
        final PotionType t = EIGHT && (type.equals("AWKWARD") || type.equals("LUCK") || type.equals("MUNDANE") || type.equals("THICK")) || (EIGHT || NINE || TEN || ELEVEN || TWELVE) && (type.equals("TURTLE_MASTER") || type.equals("SLOW_FALLING"))
                ? PotionType.WATER : PotionType.valueOf(type);
        this.type = t;
        final String bn = base.name();
        if(EIGHT) {
            potion = t.equals(PotionType.WATER) ? new Potion(t).toItemStack(1) : new Potion(t, upgraded ? 2 : 1, bn.equals("SPLASH")).toItemStack(1);
            potiondata = potion.getItemMeta();
        } else {
            final ItemStack is = new ItemStack(Material.matchMaterial(bn.equals("NORMAL") ? "POTION" : bn.contains("ARROW") ? bn.contains("TIPPED") ? "TIPPED_ARROW" : "ARROW" : bn + "_POTION"));
            final boolean a = !is.getType().equals(Material.ARROW);
            PotionMeta pm = null;
            org.bukkit.potion.PotionData pd = null;
            if(a) {
                pm = (PotionMeta) is.getItemMeta();
                pd = new org.bukkit.potion.PotionData(t, t.isExtendable() && extended, t.isUpgradeable() && upgraded);
            }
            potiondata = pd;
            if(a) {
                pm.setBasePotionData(pd);
                is.setItemMeta(pm);
            }
            potion = is;
        }
    }
    public UMaterial.PotionBase getBase() { return base; }
    public PotionType getType() { return type; }
    public ItemStack getItemStack() { return potion.clone(); }
    public Object getPotionData() { return potiondata; }
}

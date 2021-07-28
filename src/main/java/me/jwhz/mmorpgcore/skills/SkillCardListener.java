package me.jwhz.mmorpgcore.skills;

import me.jwhz.mmorpgcore.MMORPGCore;
import me.jwhz.mmorpgcore.profile.DBPlayer;
import me.jwhz.mmorpgcore.utils.NBTHelper;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.text.DecimalFormat;

public class SkillCardListener implements Listener {

    private DecimalFormat format = new DecimalFormat("#,###.#");

    private MMORPGCore core;
    private SkillCardManager skillManager;

    public SkillCardListener(MMORPGCore core) {

        this.core = core;
        this.skillManager = core.getSkillCardManager();

    }

    @EventHandler
    public void onEntityInteract(PlayerInteractEvent e) {

        if (e.getItem() != null)
            logClick(e.getPlayer(), e.getItem(), e.getAction());

    }

    public void logClick(Player player, ItemStack item, Action action) {

        if (NBTHelper.hasTag("previous clicks", item) && skillManager.getSkillCardsOnItem(item).size() > 0) {

            String clicks = NBTHelper.getString("previous clicks", item);

            if (NBTHelper.getLong("last clicked", item) < System.currentTimeMillis())
                clicks = action.name().substring(0, 1);
            else if (clicks.length() == 3)
                clicks = clicks.substring(1, 2) + action.name().substring(0, 1);
            else
                clicks += action.name().substring(0, 1);

            for (SkillCard card : skillManager.getSkillCardsOnItem(item))
                if (clicks.equalsIgnoreCase(skillManager.getBind(item, card))) {

                    DBPlayer dbPlayer = DBPlayer.getPlayer(player);

                    if (dbPlayer.getCurrentProfile().getPlayerStats().getMana() >= card.getCost())
                        core.getMagicAPI().cast(card.getSpellName(), null, player, player);

                    return;

                }

        }

    }

    public String convertCooldown(long cooldown) {

        return format.format((cooldown - System.currentTimeMillis()) / 1000);

    }

}

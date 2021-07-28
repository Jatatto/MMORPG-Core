package me.jwhz.mmorpgcore.gui.guis;

import me.jwhz.mmorpgcore.config.ConfigHandler;
import me.jwhz.mmorpgcore.config.ConfigValue;
import me.jwhz.mmorpgcore.gui.GUI;
import me.jwhz.mmorpgcore.gui.Scroller;
import me.jwhz.mmorpgcore.profile.DBPlayer;
import me.jwhz.mmorpgcore.profile.Profile;
import me.jwhz.mmorpgcore.rpgclass.levels.Level;
import me.jwhz.mmorpgcore.utils.ItemFactory;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.text.DecimalFormat;
import java.util.*;

public class LevelsGUI extends GUI {

    private Scroller<Integer> pageScroller;

    private DecimalFormat format = new DecimalFormat("#,###");

    private Profile profile;
    private Level levelInfo;

    @ConfigValue("gui.levels.name")
    String name = "&aLevels";
    @ConfigValue("gui.levels.rows")
    int rows = 4;
    @ConfigValue("gui.levels.ranks per scroll")
    int ranksPerScroll = 14;
    @ConfigValue("gui.levels.next arrow slot")
    int nextArrowSlot = 33;
    @ConfigValue("gui.levels.back arrow slot")
    int backArrowSlot = 29;
    @ConfigValue("gui.levels.slots")
    List<Integer> slots = getDefaultSlots();

    @ConfigValue("gui.levels.items.filler item")
    ItemStack fillerItem = ItemFactory.build(Material.GRAY_STAINED_GLASS_PANE, "&f");

    @ConfigValue("gui.levels.items.next arrow")
    ItemStack nextArrow = ItemFactory.build(Material.ARROW, "&aNext &7(%current_page%/%max_pages%)");

    @ConfigValue("gui.levels.items.backArrow arrow")
    ItemStack backArrow = ItemFactory.build(Material.ARROW, "&cBack &7(%current_page%/%max_pages%)");

    @ConfigValue("gui.levels.items.unlocked level")
    ItemStack unlockedLevel = ItemFactory.build(Material.BOOK,
            "&2&lLevel: &a%level%",
            "",
            "&aRequirement: &7%requirement%/%requirement%",
            "&eYour total xp: &7%mmorpg_level_totalxp%",
            "",
            "&aPassed."
    );
    @ConfigValue("gui.levels.items.next level")
    ItemStack nextLevel = ItemFactory.fakeGlow(Material.WRITABLE_BOOK,
            "&6&eLevel: &e%level%",
            " ",
            "&eRequirement: &7%mmorpg_level_currentxp%/%requirement%",
            "&eYour total xp: &7%mmorpg_level_totalxp%",
            "",
            "&eProgressing towards."
    );
    @ConfigValue("gui.levels.items.locked level")
    ItemStack lockedLevel = ItemFactory.build(Material.WRITABLE_BOOK,
            "&4&c&lLevel: &c%level%",
            " ",
            "&cRequirement: &70/%requirement%",
            "&cYour total xp: &7%mmorpg_level_totalxp%",
            "",
            "&cLocked."
    );

    public LevelsGUI(Player player) {

        ConfigHandler.setPresets(this);
        ConfigHandler.reload(this);

        this.profile = DBPlayer.getPlayer(player).getCurrentProfile();
        this.levelInfo = profile.getPlayerStats().getLevelInfo();

        List<Integer> ranks = new ArrayList<>();

        for (int i = 1; i <= levelInfo.getLevelSystem().getMaxLevel(); i++)
            ranks.add(i);

        this.pageScroller = new Scroller<>(ranks, ranksPerScroll);

        this.inventory = Bukkit.createInventory(null, rows * 9, name);

        open(player);

    }

    @Override
    public void onClick(InventoryClickEvent e) {

        e.setCancelled(true);

        if (e.getSlot() == nextArrowSlot) {

            pageScroller.next();
            setupGUI((Player) e.getWhoClicked());

        } else if (e.getSlot() == backArrowSlot) {

            pageScroller.back();
            setupGUI((Player) e.getWhoClicked());

        }

    }

    @Override
    public void setupGUI(Player player) {

        for (int i = 0; i < inventory.getSize(); i++)
            inventory.setItem(i, fillerItem);

        for (int i = 0; i < ranksPerScroll; i++) {

            if (pageScroller.getItemsOnCurrentPage().size() <= i)
                inventory.setItem(slots.get(i), null);
            else {

                int level = pageScroller.getItemsOnCurrentPage().get(i);

                inventory.setItem(slots.get(i), replaceVariables(player,
                        levelInfo.getLevel() + 1 == level ?
                                nextLevel :
                                levelInfo.getLevel() < level ?
                                        lockedLevel :
                                        unlockedLevel
                        , level));

            }

        }

        Map<String, String> arrowVariables = new HashMap<>();

        arrowVariables.put("%current_page%", (pageScroller.getCurrentPage() + 1) + "");
        arrowVariables.put("%max_pages%", pageScroller.getMaxPages() + "");

        inventory.setItem(backArrowSlot, ItemFactory.replaceVariables(player, backArrow, arrowVariables));

        inventory.setItem(nextArrowSlot, ItemFactory.replaceVariables(player, nextArrow, arrowVariables));

    }

    public ItemStack replaceVariables(Player player, ItemStack item, int level) {

        Map<String, String> variables = new HashMap<>();

        variables.put("%level%", level + "");
        variables.put("%requirement%", format.format(levelInfo.getLevelSystem().getLevelRequirement(level)));

        return ItemFactory.replaceVariables(player, item, variables);

    }

    private List<Integer> getDefaultSlots() {

        ArrayList<Integer> slots = new ArrayList<>();

        for (int slot : new int[]{10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25})
            slots.add(slot);

        return slots;

    }

}

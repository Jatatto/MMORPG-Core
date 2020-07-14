package me.jwhz.mmorpgcore.gui.guis;

import me.jwhz.mmorpgcore.config.ConfigHandler;
import me.jwhz.mmorpgcore.config.ConfigValue;
import me.jwhz.mmorpgcore.gui.GUI;
import me.jwhz.mmorpgcore.profile.DBPlayer;
import me.jwhz.mmorpgcore.profile.Profile;
import me.jwhz.mmorpgcore.response.responses.ChatResponse;
import me.jwhz.mmorpgcore.rpgclass.RPGClass;
import me.jwhz.mmorpgcore.utils.ItemFactory;
import me.jwhz.mmorpgcore.utils.materials.UMaterial;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class ProfileSelectGUI extends GUI {

    @ConfigValue("gui.profile select.rows")
    int rows = 3;
    @ConfigValue("gui.profile select.name")
    String name = "&aSelect a profile";
    @ConfigValue("gui.profile select.items.filler item")
    ItemStack fillerItem = ItemFactory.build(UMaterial.GRAY_STAINED_GLASS_PANE, "&f");

    @ConfigValue("gui.profile select.items.current profile")
    ItemStack currentProfile = ItemFactory.build(UMaterial.BOOK, "&aProfile: %mmorpg_<profile>_name%", " ", "&7- &aUUID: %mmorpg_<profile>_uuid%", "", "&aCurrent profile.");
    @ConfigValue("gui.profile select.items.has profile")
    ItemStack hasProfile = ItemFactory.build(UMaterial.BOOK, "&aProfile: %mmorpg_<profile>_name%", " ", "&7- &aUUID: %mmorpg_<profile>_uuid%", "", "&aClick to change to this profile.");
    @ConfigValue("gui.profile select.items.empty profile")
    ItemStack emptyProfile = ItemFactory.build(UMaterial.WRITABLE_BOOK, "&aEmpty Profile, ", " ", "&7Click to create a new profile.");

    @ConfigValue("gui.profile select.slots")
    Map<String, String> slots = getDefaultSlots();

    Map<ItemStack, Profile> profiles = new HashMap<>();

    public ProfileSelectGUI(Player player) {

        ConfigHandler.setPresets(this);
        ConfigHandler.reload(this);

        this.inventory = Bukkit.createInventory(null, rows * 9, name);

        open(player);

    }

    @Override
    public void onClick(InventoryClickEvent e) {

        e.setCancelled(true);

        DBPlayer player = DBPlayer.getPlayer(e.getWhoClicked().getUniqueId());

        if (e.getCurrentItem() != null && profiles.containsKey(e.getCurrentItem())) {

            if (e.getClick().isShiftClick() && profiles.get(e.getCurrentItem()) != null)
                new ViewProfileGUI(player.getPlayer(), profiles.get(e.getCurrentItem()));
            else {

                Profile profile = profiles.get(e.getCurrentItem());

                if (profile == null) {

                    e.setCurrentItem(null);
                    e.getWhoClicked().closeInventory();

                    if (core.playerManager.getMaximumProfiles(player.getPlayer()) > player.getProfiles().size()) {

                        player.sendMessage(core.messages.enterProfileName);

                        new ChatResponse(player.getPlayer()) {

                            @Override
                            public boolean onResponse(String response) {

                                if (player.getProfile(response) != null) {

                                    player.sendMessage(core.messages.profileAlreadyExists);
                                    return false;

                                }

                                player.createProfile(response);
                                core.rpgClassManager.addProfileChange(player, player.getProfile(response));
                                player.sendMessage(core.messages.profileCreated.replace("%profile%", response));


                                return true;

                            }

                        }.register();

                    } else
                        player.sendMessage(core.messages.maximumProfilesCreated);

                } else if (!profile.getProfileUUID().equals(player.getCurrentProfile().getProfileUUID()))
                    player.setCurrentProfile(profile);

            }

        }

    }

    @Override
    public void setupGUI(Player player) {

        for (int i = 0; i < inventory.getSize(); i++)
            inventory.setItem(i, ItemFactory.replacePlaceholders(player, fillerItem));

        DBPlayer dbPlayer = DBPlayer.getPlayer(player);

        for (Map.Entry<String, String> entry : slots.entrySet()) {

            String[] parts = entry.getValue().split("_");

            Profile profile = null;

            int profileNumber = -1;

            try {

                profileNumber = Integer.parseInt(parts[1]);

            } catch (Exception e) {
            }

            if (profileNumber != -1 && dbPlayer.getProfiles().size() >= profileNumber)
                profile = dbPlayer.getProfiles().get(dbPlayer.getProfiles().size() - profileNumber);

            ItemStack item;

            if (profile != null)
                item =
                        dbPlayer.getCurrentProfile().getProfileUUID().equals(profile.getProfileUUID()) ?
                                ItemFactory.replaceVariable(player, currentProfile, entry.getValue(), "<profile>") :
                                ItemFactory.replaceVariable(player, hasProfile, entry.getValue(), "<profile>");
            else
                item = ItemFactory.replaceVariable(player, emptyProfile, entry.getValue(), "<profile>");

            inventory.setItem(Integer.parseInt(entry.getKey()), item);

            profiles.put(item, profile);


        }

    }

    private Map<String, String> getDefaultSlots() {

        Map<String, String> slots = new HashMap<>();

        slots.put("10", "profile_1");
        slots.put("12", "profile_2");
        slots.put("14", "profile_3");
        slots.put("16", "profile_4");

        return slots;

    }

}

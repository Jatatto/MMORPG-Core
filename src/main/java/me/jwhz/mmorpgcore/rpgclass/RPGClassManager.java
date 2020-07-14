package me.jwhz.mmorpgcore.rpgclass;

import me.jwhz.mmorpgcore.events.PlayerLevelUpEvent;
import me.jwhz.mmorpgcore.manager.Manager;
import me.jwhz.mmorpgcore.profile.DBPlayer;
import me.jwhz.mmorpgcore.profile.Profile;
import me.jwhz.mmorpgcore.rpgclass.levels.LevelListeners;
import me.jwhz.mmorpgcore.rpgclass.passive.PassiveListeners;
import me.jwhz.mmorpgcore.rpgclass.passive.TickPassive;
import org.bukkit.Bukkit;
import org.bukkit.entity.ArmorStand;
import org.bukkit.event.EventHandler;

import java.io.File;
import java.util.*;

public class RPGClassManager extends Manager<RPGClass> {

    private File directory;
    private Map<DBPlayer, Profile> profileChanges = new HashMap<>();
    private Map<DBPlayer, Profile> changesQueue = new HashMap<>();

    private Map<Long, ArmorStand> armorStands = new HashMap<>();
    private Map<Long, ArmorStand> armorStandsQueue = new HashMap<>();


    public RPGClassManager() {

        super("config");

        this.directory = new File(core.getDataFolder() + "/classes");

        if (!directory.exists())
            directory.mkdir();

        loadRPGClasses();

        Bukkit.getPluginManager().registerEvents(new PassiveListeners(), core);
        Bukkit.getPluginManager().registerEvents(new LevelListeners(core), core);

        Bukkit.getScheduler().scheduleSyncRepeatingTask(core, () -> Bukkit.getOnlinePlayers().forEach(player -> {

            DBPlayer dbPlayer = DBPlayer.getPlayer(player);

            profileChanges.putAll(changesQueue);
            changesQueue.clear();

            Iterator<Map.Entry<DBPlayer, Profile>> iterator = profileChanges.entrySet().iterator();

            while (iterator.hasNext()) {

                Map.Entry<DBPlayer, Profile> entry = iterator.next();

                entry.getKey().setCurrentProfile(entry.getValue());

            }

            armorStands.putAll(armorStandsQueue);
            armorStandsQueue.clear();

            Iterator<Map.Entry<Long, ArmorStand>> armorStandIterator = armorStands.entrySet().iterator();

            while (armorStandIterator.hasNext()) {

                Map.Entry<Long, ArmorStand> armorStand = armorStandIterator.next();

                if (armorStand.getKey() <= System.currentTimeMillis()) {

                    armorStand.getValue().remove();
                    armorStandIterator.remove();

                }

            }

            profileChanges.clear();

            if (dbPlayer != null) {

                Profile profile = dbPlayer.getCurrentProfile();

                if (profile != null && profile.getPassives() != null)
                    profile.getPassives().forEach(passive -> {

                        if (passive instanceof TickPassive)
                            ((TickPassive) passive).onTick();

                    });

            }

        }), 0, 1);

    }

    public void addProfileChange(DBPlayer dbPlayer, Profile profile) {

        changesQueue.put(dbPlayer, profile);

    }

    public void addArmorStand(ArmorStand armorStand, Long due) {

        armorStandsQueue.put(due, armorStand);

    }

    public void loadRPGClasses() {

        getList().clear();

        Arrays.stream(Objects.requireNonNull(directory.listFiles())).forEach(file -> getList().add(new RPGClass(file)));

    }

    public boolean isRPGClass(String name) {

        return getList().stream().anyMatch(rpgClass -> rpgClass.getClassName().equalsIgnoreCase(name));

    }

    public RPGClass getRPGClass(String name) {

        return getList().stream().filter(rpgClass -> rpgClass.getClassName().equalsIgnoreCase(name)).findFirst().orElse(null);

    }

    @Override
    public void onReload() {

        loadRPGClasses();

    }


    @EventHandler
    public void onLevelUp(PlayerLevelUpEvent e) {

        e.getPlayer().sendMessage(core.messages.levelUp);

    }

}

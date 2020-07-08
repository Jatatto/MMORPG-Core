package me.jwhz.mmorpgcore.rpgclass;

import me.jwhz.mmorpgcore.manager.Manager;
import me.jwhz.mmorpgcore.profile.DBPlayer;
import me.jwhz.mmorpgcore.profile.Profile;
import me.jwhz.mmorpgcore.rpgclass.passive.TickPassive;
import org.bukkit.Bukkit;

import java.io.File;
import java.util.*;

public class RPGClassManager extends Manager<RPGClass> {

    private File directory;
    private Map<DBPlayer, Profile> profileChanges = new HashMap<>();
    private Map<DBPlayer, Profile> changesQueue = new HashMap<>();

    public RPGClassManager() {

        super("config");

        this.directory = new File(core.getDataFolder() + "/classes");

        if (!directory.exists())
            directory.mkdir();

        loadRPGClasses();

        Bukkit.getScheduler().scheduleSyncRepeatingTask(core, () -> {

            Bukkit.getOnlinePlayers().forEach(player -> {

                DBPlayer dbPlayer = DBPlayer.getPlayer(player);

                profileChanges.putAll(changesQueue);
                changesQueue.clear();

                Iterator<Map.Entry<DBPlayer, Profile>> iterator = profileChanges.entrySet().iterator();

                while (iterator.hasNext()) {

                    Map.Entry<DBPlayer, Profile> entry = iterator.next();

                    entry.getKey().setCurrentProfile(entry.getValue());

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

            });

        }, 0, 1);

    }

    public void addProfileChange(DBPlayer dbPlayer, Profile profile) {

        changesQueue.put(dbPlayer, profile);

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

}

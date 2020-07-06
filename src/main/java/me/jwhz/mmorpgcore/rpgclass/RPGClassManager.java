package me.jwhz.mmorpgcore.rpgclass;

import me.jwhz.mmorpgcore.manager.Manager;
import me.jwhz.mmorpgcore.profile.DBPlayer;
import me.jwhz.mmorpgcore.profile.Profile;
import me.jwhz.mmorpgcore.rpgclass.passive.TickPassive;
import org.bukkit.Bukkit;

import java.io.File;
import java.util.Arrays;
import java.util.Objects;

public class RPGClassManager extends Manager<RPGClass> {

    private File directory;

    public RPGClassManager() {

        super("config");

        this.directory = new File(core.getDataFolder() + "/classes");

        if (!directory.exists())
            directory.mkdir();

        loadRPGClasses();

        Bukkit.getScheduler().scheduleSyncRepeatingTask(core, () -> {

            Bukkit.getOnlinePlayers().forEach(player -> {

                DBPlayer dbPlayer = DBPlayer.getPlayer(player);

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

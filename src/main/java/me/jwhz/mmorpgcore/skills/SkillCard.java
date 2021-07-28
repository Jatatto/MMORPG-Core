package me.jwhz.mmorpgcore.skills;

import me.jwhz.mmorpgcore.manager.ManagerObject;
import me.jwhz.mmorpgcore.utils.BukkitSerialization;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

public class SkillCard extends ManagerObject<String> {

    private ConfigurationSection section;

    public SkillCard(ConfigurationSection section) {

        this.section = section;

    }

    public String getSpellName() {

        return section.getString("spell name");

    }

    public double getCost() {

        return section.getDouble("cost", 50);

    }

    public double getCooldown() {

        return section.getDouble("cooldown in ticks", 40);

    }

    public ItemStack getItem() {

        return BukkitSerialization.convertSection(section.getConfigurationSection("card item"));

    }

    @Override
    public String getIdentifier() {

        return section.getString("identifier");

    }

}

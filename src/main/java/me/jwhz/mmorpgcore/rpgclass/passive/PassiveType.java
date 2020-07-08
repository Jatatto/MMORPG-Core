package me.jwhz.mmorpgcore.rpgclass.passive;

import me.jwhz.mmorpgcore.rpgclass.passive.passives.DamageReductionPassive;
import me.jwhz.mmorpgcore.rpgclass.passive.passives.HealthRegenerationPassive;
import me.jwhz.mmorpgcore.rpgclass.passive.passives.ManaReductionPassive;
import me.jwhz.mmorpgcore.rpgclass.passive.passives.ManaRegenerationPassive;

public enum PassiveType {

    DAMAGE_REDUCTION(DamageReductionPassive.class, "damage reduction"),
    HEALTH_REGENERATION(HealthRegenerationPassive.class, "health rengeration"),
    MANA_REDUCTION(ManaReductionPassive.class, "mana reduction"),
    MANA_REGENERATION(ManaRegenerationPassive.class, "mana regeneration");

    private Class clazz;
    private String[] names;

    PassiveType(Class clazz, String... names) {

        this.clazz = clazz;

    }

    public Class getPassiveClass() {

        return clazz;

    }

    public String[] getNames() {

        return names;

    }

    public boolean isValidName(String name) {

        for (String string : getNames())
            if (name.equalsIgnoreCase(string))
                return true;

        return false;

    }

    public static PassiveType getByName(String name) {

        for (PassiveType type : values())
            if (type.name().equalsIgnoreCase(name) || type.isValidName(name))
                return type;

        return null;

    }

    public static boolean isPassiveType(String name) {

        for (PassiveType type : values())
            if (type.name().equalsIgnoreCase(name) || type.isValidName(name))
                return true;

        return false;

    }

}

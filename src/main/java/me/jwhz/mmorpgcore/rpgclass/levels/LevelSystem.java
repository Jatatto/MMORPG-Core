package me.jwhz.mmorpgcore.rpgclass.levels;

import com.udojava.evalex.Expression;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
import java.util.Map;

public class LevelSystem {


    private Map<Integer, Long> levelCost = new HashMap<>();

    private long firstLevelupRequirement;
    private String formula;
    private int maxLevel;

    public LevelSystem(String formula, int maxLevel, long firstLevelupRequirement) {

        this.formula = formula;
        this.maxLevel = maxLevel;
        this.firstLevelupRequirement = firstLevelupRequirement;

    }

    public LevelSystem(ConfigurationSection section) {

        this(section.getString("formula"), section.getInt("max level"), section.getLong("first level up requirement"));

    }

    public int getMaxLevel() {

        return maxLevel;

    }

    public String getFormula() {

        return formula;

    }

    public long getFirstLevelupRequirement() {

        return firstLevelupRequirement;

    }

    public long getLevelRequirement(int level) {

        if (level == 2)
            return getFirstLevelupRequirement();

        if (getMaxLevel() < level)
            return -1;

        if (level <= 1)
            return 0;

        if (levelCost.containsKey(level))
            return levelCost.get(level);
        else {

            long value = getFirstLevelupRequirement();

            if (getFormula().contains("n-1"))
                value = new Expression(getFormula().replace("n-1", getLevelRequirement(level - 1) + "")).eval().longValue();
            else if (getFormula().contains("x"))
                value = new Expression(getFormula().replace("x", level + "")).eval().longValue();


            if (value == getFirstLevelupRequirement())
                value *= level;

            levelCost.put(level, value);
            return value;

        }

    }

}

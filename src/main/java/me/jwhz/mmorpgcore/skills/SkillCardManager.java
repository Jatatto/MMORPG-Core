package me.jwhz.mmorpgcore.skills;

import me.jwhz.mmorpgcore.manager.Manager;
import me.jwhz.mmorpgcore.utils.NBTHelper;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class SkillCardManager extends Manager<SkillCard> {

    public SkillCardManager() {

        super("skill-cards");

        if (yamlConfiguration.isSet("cards"))
            for (String key : yamlConfiguration.getConfigurationSection("cards").getKeys(false))
                getList().add(new SkillCard(yamlConfiguration.getConfigurationSection("cards").getConfigurationSection(key)));

    }

    public List<SkillCard> getSkillCards() {

        return getList();

    }

    public SkillCard getSkillCard(String name) {

        return getList().stream().filter(card -> card.getIdentifier().equalsIgnoreCase(name)).findFirst().orElse(null);

    }

    public List<SkillCard> getSkillCardsOnItem(ItemStack item) {

        String skills = NBTHelper.getString("skill cards", item);

        List<SkillCard> cards = new ArrayList<>();

        if (skills.contains(","))
            for (String card : skills.split(","))
                if (card.length() > 0)
                    cards.add(getSkillCard(card));

        return cards;

    }

    public String getBind(ItemStack item, SkillCard skillCard) {

        return NBTHelper.getString("bind-" + skillCard.getIdentifier(), item);

    }

    public ItemStack addSkillCard(ItemStack item, SkillCard card, String bind) {

        String cards = NBTHelper.getString("skill cards", item);

        cards += card.getIdentifier() + ",";

        item = NBTHelper.addTag("skill cards", cards, item);
        return NBTHelper.addTag("bind-" + card.getIdentifier(), bind, item);

    }


}

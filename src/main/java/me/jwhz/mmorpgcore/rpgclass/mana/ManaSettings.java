package me.jwhz.mmorpgcore.rpgclass.mana;

import org.bson.Document;
import org.bukkit.configuration.ConfigurationSection;

public class ManaSettings {

    private Document document = new Document();

    public ManaSettings(Document document) {

        this.document = document;

    }

    public ManaSettings(ConfigurationSection section) {

        this(section.getDouble("max mana"), section.getDouble("mana regeneration"), section.getString("mana name", "Mana"));

    }

    public ManaSettings(double maxMana, double manaRegeneration, String manaName) {

        document.put("max mana", maxMana);
        document.put("mana regeneration", manaRegeneration);
        document.put("mana name", manaName);

    }

    public String getManaName() {

        return document.get("mana name", "Mana");

    }

    public double getMaxMana() {

        return document.get("max mana", 100.0);

    }

    public double getManaRegeneration() {

        return document.get("mana regeneration", 5.0);

    }

    public void setMaxMana(double maxMana) {

        document.put("max mana", maxMana);

    }

    public void setManaReneneration(double manaReneneration) {

        document.put("mana regeneration", manaReneneration);

    }

    public Document getDocument() {

        return document;

    }

}

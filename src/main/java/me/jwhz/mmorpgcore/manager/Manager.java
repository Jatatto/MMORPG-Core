package me.jwhz.mmorpgcore.manager;

import me.jwhz.mmorpgcore.MMORPGCore;
import me.jwhz.mmorpgcore.config.ConfigFile;

import java.util.ArrayList;
import java.util.List;

public abstract class Manager<T extends ManagerObject> extends ConfigFile {

    protected ArrayList<T> list = new ArrayList<>();

    protected MMORPGCore core = MMORPGCore.getInstance();

    public Manager(String fileName) {

        super(fileName);

    }

    public List<T> getList() {

        return list;

    }

    public void onReload() { }

    public void onEnable() { }

    public void onDisable() { }

}
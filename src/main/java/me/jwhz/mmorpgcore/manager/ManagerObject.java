package me.jwhz.mmorpgcore.manager;

import me.jwhz.mmorpgcore.MMORPGCore;

public abstract class ManagerObject<I> {

    protected MMORPGCore core = MMORPGCore.getInstance();

    public abstract I getIdentifier();

}
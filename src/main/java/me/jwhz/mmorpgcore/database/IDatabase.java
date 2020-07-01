package me.jwhz.mmorpgcore.database;

import java.util.UUID;

public interface IDatabase {

    Object retrieve(UUID id, String key);

    void store(UUID id, String key, Object value);

    boolean isSet(UUID id, String key);

    boolean isRegistered(UUID id);

}

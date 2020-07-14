package me.jwhz.mmorpgcore.gui;

import java.util.ArrayList;
import java.util.List;

public class Scroller<T> {

    private List<T> items;
    private int page;
    private int perPage;

    public Scroller(List<T> items, int perPage) {

        this.items = items;
        this.page = 0;
        this.perPage = perPage;

    }

    public int getCurrentPage() {

        return page;

    }

    public int getMaxPages() {

        return (int) Math.round(((double) items.size() / perPage) + (.5 - ((double) (1 / perPage))));

    }

    public List<T> next() {

        if ((page + 1) * perPage <= items.size())
            page += 1;

        return getItemsOnCurrentPage();

    }

    public List<T> back() {

        if ((page - 1) >= 0)
            page -= 1;

        return getItemsOnCurrentPage();

    }

    public List<T> getItemsOnCurrentPage() {

        return getPageItems(page);

    }

    private List<T> getPageItems(int page) {

        List<T> list = new ArrayList<T>();

        for (int i = page * perPage; i < (page + 1) * perPage; i++)
            if (items.size() > i)
                list.add(items.get(i));

        return list;

    }


}

package com.onemore.karungguniapp.model;

/**
 * Created with IntelliJ IDEA.
 * User: gemengqin
 * Date: 10/22/13
 * Time: 2:01 AM
 * To change this template use File | Settings | File Templates.
 */

import java.util.ArrayList;

public final class Section {

    private String title;
    private ArrayList<Advertisement> items;

    // favor "copy constructor/getInstance" over clone, clone is tricky and error prone
    // (better yet use immutable objects, but sort of overkill for this example)
    public static Section getInstance(Section section) {
        Section copy = new Section();
        copy.title = section.title;
        if ((section.items != null) && !section.items.isEmpty()) {
            copy.items = new ArrayList<Advertisement>(section.items.size());
            for (Advertisement item : section.items) {
                copy.items.add(Advertisement.getInstance(item));
            }
        } else {
            copy.items = new ArrayList<Advertisement>();
        }

        return copy;
    }

    public Section() {
        this.items = new ArrayList<Advertisement>();
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<Advertisement> getItems() {
        return this.items;
    }

    public void setItems(ArrayList<Advertisement> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        if (this.title != null) {
            return this.title;
        }
        return "NULL";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.items == null) ? 0 : this.items.hashCode());
        result = prime * result + ((this.title == null) ? 0 : this.title.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Section other = (Section) obj;
        if (this.items == null) {
            if (other.items != null) {
                return false;
            }
        } else if (!this.items.equals(other.items)) {
            return false;
        }
        if (this.title == null) {
            if (other.title != null) {
                return false;
            }
        } else if (!this.title.equals(other.title)) {
            return false;
        }
        return true;
    }
}

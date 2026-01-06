package telegram.bot.graphics.core;

import java.util.ArrayList;
import java.util.List;

public abstract class Instance {
    public String name;
    private Instance parent;
    private List<Instance> childrens;

    public Instance() {
        name = this.getClass().getSimpleName();
        childrens = new ArrayList<>();
    }

    public Instance parent() {
        return parent;
    }

    public void setParent(Instance parent) {
        if (this == parent) {return;}

        if (this.parent != null) {
            this.parent.childrens.remove(this);
        }

        this.parent = parent;

        if (parent != null) {
            parent.childrens.add(this);
        }
    }

    public Instance getChild(String name) {
        for (Instance child : childrens) {
            if (child.name == name) {return child;}
        }
        return null;
    }

    public List<Instance> children() {
        return new ArrayList<>(childrens);
    }
}

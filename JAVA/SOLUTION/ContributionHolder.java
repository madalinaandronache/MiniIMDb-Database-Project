package SOLUTION;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

public class ContributionHolder {
    private static List<Actor> actors = new ArrayList<>();
    private static List<Production> productions = new ArrayList<>();

    public static void addActor(Actor a) {
        actors.add(a);
    }

    public static void removeActor(Actor a) {
        actors.remove(a);
    }

    public static void addProduction(Production p) {
        productions.add(p);
    }

    public static List<Actor> getActors() {
        return  actors;
    }

    public static List<Production> getProductions() {
        return productions;
    }

    public static void displayActors() {
        for(Actor actor : actors) {
            System.out.println("  Name: " + actor.getName());
        }
    }

    public static Actor removeActorContributions(String name) {
        for(Actor actor : actors) {
            if(actor.getName().equals(name)) {
                removeActor(actor);
                return actor;
            }
        }
        return null;
    }
}

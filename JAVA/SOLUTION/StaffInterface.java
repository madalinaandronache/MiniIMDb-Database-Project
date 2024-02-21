package SOLUTION;

public interface StaffInterface {
    void addProductionSystem(Production p);
    void addActorSystem(Actor a);
    Production removeProductionSystem(String name);
    Actor removeActorSystem(String name);
    void updateProduction(Production p);
    void updateActor(Actor a);
    void resolveUserRequests();
}

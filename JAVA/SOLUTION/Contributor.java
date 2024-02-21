package SOLUTION;

public class Contributor extends Staff implements RequestsManager, Observer{
    public Contributor(Information information, String username, int experience) {
        super(information, AccountType.Contributor, username, experience);
    }

    @Override
    public void createRequest(Request r, User<?> user) {
        ((Staff<?>) user).addRequest(r);
    }

    @Override
    public void removeRequest(Request request, User<?> user) {
        ((Staff<?>) user).deleteRequest(request);
    }

    public void update(String notification) {
        System.out.println("Contributor User " + this.getUsername() + " received notification: " + notification);
    }
}

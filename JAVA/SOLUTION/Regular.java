package SOLUTION;

public class Regular extends User implements RequestsManager, Observer {
    public Regular(Information information, String username, int experience) {
        super(information, AccountType.Regular, username, experience);
    }

    @Override
    public void createRequest(Request r, User<?> user) {
        ((Staff<?>) user).addRequest(r);
    }

    @Override
    public void removeRequest(Request request, User<?> user) {
        ((Staff<?>) user).deleteRequest(request);
    }

    @Override
    public void update(String notification) {
        System.out.println("Regular User " + this.getUsername() + " received notification: " + notification);
    }
}

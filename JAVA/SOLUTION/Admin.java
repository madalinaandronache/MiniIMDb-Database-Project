package SOLUTION;

public class Admin extends Staff implements Observer{
    public Admin(Information information, String username, int experience) {
        super(information, AccountType.Admin, username, experience);
    }

    public void displayRequests() {
        System.out.println("All Admins Requests:");
        for (Request request : RequestHolder.getAllRequests()) {
            request.displayInfo();
            System.out.println();
        }
    }

    @Override
    public void update(String notification) {
        System.out.println("Admin User " + this.getUsername() + " received notification: " + notification);
    }
}

package SOLUTION;

public class UserFactory {
    public static User<?> createUser(AccountType accountType, User.Information information, String username, int experience) {
        switch (accountType) {
            case Regular:
                return new Regular(information, username, experience);
            case Contributor:
                return new Contributor(information, username, experience);
            case Admin:
                return new Admin(information, username, experience);
            default:
                throw new IllegalArgumentException("Unknown account type: " + accountType);
        }
    }
}

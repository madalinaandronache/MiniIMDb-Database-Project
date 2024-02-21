package SOLUTION;

public interface RequestsManager {
    void createRequest(Request r, User<?> user);
    void removeRequest(Request request, User<?> user);
}

package SOLUTION;

import java.util.ArrayList;
import java.util.List;
public class RequestHolder {
    private static final List<Request> allRequests = new ArrayList<>();
    public static void addRequest(Request request) {
        allRequests.add(request);
    }
    public static void deleteRequest(Request request) {
        allRequests.remove(request);
    }
    public static List<Request> getAllRequests() {
        return allRequests;
    }
}

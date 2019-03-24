package interfaces;

public interface extentReport {
    void startExtentReport(String name, String host, String env, String user);
    void flushExtentReport();
}

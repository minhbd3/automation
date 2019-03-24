package interfaces;

public interface httpRequest {
    int requestToGetStatusCode(String url);
    String requestToGetBody(String url);
    void requestToServer(String type, String url);
    void requestToServerWithAuthentication(String url, String token);
    void requestAndGroupUrlBaseStatusCode(String url, int code);
}

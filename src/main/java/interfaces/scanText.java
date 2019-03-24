package interfaces;

public interface scanText {
    String readMessageByOcr(String language);
    String readMessageTerminal(String command);
}

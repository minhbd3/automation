package interfaces;

public interface copy {
    void copyFile(String sourceFileName, String destinationFileName);
    void copyFiles(String fileLocationSource, String fileLocationDestination, int numberOfFilesToCopy);
}

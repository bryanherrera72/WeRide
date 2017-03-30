
package www.weride.com.amazonaws.mobile.content;

import java.io.File;

public interface TransferHelper {
    String DIR_DELIMITER = "/" ;

    void download(String filePath, long fileSize, ContentProgressListener listener);
    void upload(File file, String filePath, ContentProgressListener listener);
    void setProgressListener(String filePath, ContentProgressListener listener);
    void clearProgressListeners();
    long getSizeTransferring();
    boolean isTransferring(String filePath);
    boolean isTransferWaiting(String filePath);
    void destroy();
}

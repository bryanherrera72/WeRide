
package www.weride.com.amazonaws.mobile.content;

import java.io.File;

/** All Content managed by ContentManager implements this interface. */
public interface ContentItem {
    /**
     * @return the time that this content item was last modified.
     */
    long getLastModifiedTime();

    /**
     * @return the relative path and name of the file for this content item.
     */
    String getFilePath();

    /**
     * @return the size of the file this content item represents.
     */
    long getSize();

    /**
     * @return the state of this content, See {@link ContentState}
     */
    ContentState getContentState();

    /**
     * Set the content state for this content item.
     * @param contentState the new content state.
     */
    void setContentState(final ContentState contentState);

    /**
     * @return The File reference if this file is in the local cache, otherwise returns null.
     */
    public File getFile();
}

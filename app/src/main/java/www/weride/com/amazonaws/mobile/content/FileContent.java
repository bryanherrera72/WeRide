
package www.weride.com.amazonaws.mobile.content;

import java.io.File;

public class FileContent implements ContentItem {
    /** The underlying file reference. */
    private File file;
    /** Snapshot of the last modified time. */
    private long lastModified;
    /* Snapshot of the file size. */
    private long size;
    /* Relative path to the file. */
    private String relativePath;

    /** Flag to keep track of whether this file has a newer version available. */
    private ContentState state;

    public FileContent(final File file, final String relativePath) {
        this.file = file;
        this.relativePath = relativePath;
        this.state = ContentState.CACHED;

        refresh();
    }

    /** {@inheritDoc} */
    @Override
    public long getLastModifiedTime() {
        return lastModified;
    }

    /** {@inheritDoc} */
    @Override
    public String getFilePath() {
        return relativePath;
    }

    /** {@inheritDoc} */
    @Override
    public long getSize() {
        return size;
    }

    /** {@inheritDoc} */
    @Override
    public ContentState getContentState() {
        return state;
    }

    /** {@inheritDoc} */
    @Override
    public void setContentState(final ContentState contentState) {
        this.state = contentState;
    }

    /**
     * Refreshes the state of the content item.
     */
    public void refresh() {
        lastModified = file.lastModified();
        size = file.length();
    }

    /** {@inheritDoc} */
    @Override
    public File getFile() {
        return file;
    }
}

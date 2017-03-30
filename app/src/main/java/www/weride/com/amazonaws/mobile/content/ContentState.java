
package www.weride.com.amazonaws.mobile.content;

public enum ContentState {
    /** The content is remote (not available in the cache) and not currently downloading. */
    REMOTE,

    /** The content is not in the cache, but is waiting to be downloaded. */
    TRANSFER_WAITING,

    /** The content is not in the cache, but is currently being downloaded. */
    TRANSFERRING,

    /** The content is available in the cache. */
    CACHED,

    /** The content is cached, but a transfer is currently waiting to retrieve a newer version. */
    CACHED_NEW_VERSION_TRANSFER_WAITING,

    /** The content is available in the cache, but a newer version is currently transferring. */
    CACHED_TRANSFERRING_NEW_VERSION,

    /** The content is available, but outdated (a newer version of the content exits remotely). */
    CACHED_WITH_NEWER_VERSION_AVAILABLE,

    /** The content represents a directory. */
    REMOTE_DIRECTORY;

    public static boolean isWaitingToTransfer(final ContentState state) {
        if (state == TRANSFER_WAITING ||
            state == CACHED_NEW_VERSION_TRANSFER_WAITING) {
            return true;
        }
        return false;
    }

    public static boolean isTransferring(final ContentState state) {
        return state == TRANSFERRING || state == CACHED_TRANSFERRING_NEW_VERSION;
    }

    public static boolean isCachedWithNewerVersionAvailableOrTransferring(final ContentState state) {
        return state == ContentState.CACHED_WITH_NEWER_VERSION_AVAILABLE
            || state == ContentState.CACHED_TRANSFERRING_NEW_VERSION
            || state == ContentState.CACHED_NEW_VERSION_TRANSFER_WAITING;
    }

    public static boolean isCached(final ContentState state) {
        return state == ContentState.CACHED || isCachedWithNewerVersionAvailableOrTransferring(state);
    }

    public static boolean isTransferringOrWaitingToTransfer(final ContentState state) {
        if (state == TRANSFER_WAITING ||
            state == CACHED_NEW_VERSION_TRANSFER_WAITING ||
            state == TRANSFERRING ||
            state == CACHED_TRANSFERRING_NEW_VERSION) {
            return true;
        }
        return false;
    }
}

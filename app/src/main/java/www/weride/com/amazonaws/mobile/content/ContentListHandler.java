
package www.weride.com.amazonaws.mobile.content;

import java.util.List;

/**
 * Handles the response to a list content operation.
 */
public interface ContentListHandler {
    /**
     * Handler to receive content listed using
     * {@link ContentManager#listAvailableContent(String,ContentListHandler)}.
     * Listing may be aborted by returning false.
     * @param startIndex the index of the first item retrieved.
     * @param partialResults the list of results received.
     * @param hasMoreResults flag indicating whether more results exist.
     * @return true to continue listing or false to abort listing.
     */
    boolean onContentReceived(int startIndex,
                              List<ContentItem> partialResults,
                              boolean hasMoreResults);

    void onError(Exception ex);
}

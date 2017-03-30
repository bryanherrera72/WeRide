package www.weride.com.amazonaws.models.nosql;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBIndexHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBIndexRangeKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBRangeKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable;

import java.util.Set;

@DynamoDBTable(tableName = "weride-mobilehub-833431439-Conversation")

public class ConversationDO {
    private String _userId;
    private String _conversationId;
    private String _createdAt;
    private String _groupId;
    private Set<String> _imageUrl;
    private Set<String> _message;

    @DynamoDBHashKey(attributeName = "userId")
    @DynamoDBAttribute(attributeName = "userId")
    public String getUserId() {
        return _userId;
    }

    public void setUserId(final String _userId) {
        this._userId = _userId;
    }
    @DynamoDBRangeKey(attributeName = "conversationId")
    @DynamoDBAttribute(attributeName = "conversationId")
    public String getConversationId() {
        return _conversationId;
    }

    public void setConversationId(final String _conversationId) {
        this._conversationId = _conversationId;
    }
    @DynamoDBIndexRangeKey(attributeName = "createdAt", globalSecondaryIndexName = "ByCreationDate")
    public String getCreatedAt() {
        return _createdAt;
    }

    public void setCreatedAt(final String _createdAt) {
        this._createdAt = _createdAt;
    }
    @DynamoDBIndexHashKey(attributeName = "groupId", globalSecondaryIndexName = "ByCreationDate")
    public String getGroupId() {
        return _groupId;
    }

    public void setGroupId(final String _groupId) {
        this._groupId = _groupId;
    }
    @DynamoDBAttribute(attributeName = "imageUrl")
    public Set<String> getImageUrl() {
        return _imageUrl;
    }

    public void setImageUrl(final Set<String> _imageUrl) {
        this._imageUrl = _imageUrl;
    }
    @DynamoDBAttribute(attributeName = "message")
    public Set<String> getMessage() {
        return _message;
    }

    public void setMessage(final Set<String> _message) {
        this._message = _message;
    }

}

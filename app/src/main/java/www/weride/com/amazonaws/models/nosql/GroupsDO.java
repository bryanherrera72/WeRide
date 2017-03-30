package www.weride.com.amazonaws.models.nosql;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBIndexHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBIndexRangeKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBRangeKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable;

import java.util.Set;

@DynamoDBTable(tableName = "weride-mobilehub-833431439-Groups")

public class GroupsDO {
    private String _userId;
    private String _groupId;
    private String _createdAt;
    private String _name;
    private Set<String> _users;

    @DynamoDBHashKey(attributeName = "userId")
    @DynamoDBAttribute(attributeName = "userId")
    public String getUserId() {
        return _userId;
    }

    public void setUserId(final String _userId) {
        this._userId = _userId;
    }
    @DynamoDBRangeKey(attributeName = "groupId")
    @DynamoDBIndexHashKey(attributeName = "groupId", globalSecondaryIndexName = "ByCreationDate")
    public String getGroupId() {
        return _groupId;
    }

    public void setGroupId(final String _groupId) {
        this._groupId = _groupId;
    }
    @DynamoDBIndexRangeKey(attributeName = "createdAt", globalSecondaryIndexName = "ByCreationDate")
    public String getCreatedAt() {
        return _createdAt;
    }

    public void setCreatedAt(final String _createdAt) {
        this._createdAt = _createdAt;
    }
    @DynamoDBAttribute(attributeName = "name")
    public String getName() {
        return _name;
    }

    public void setName(final String _name) {
        this._name = _name;
    }
    @DynamoDBAttribute(attributeName = "users")
    public Set<String> getUsers() {
        return _users;
    }

    public void setUsers(final Set<String> _users) {
        this._users = _users;
    }

}

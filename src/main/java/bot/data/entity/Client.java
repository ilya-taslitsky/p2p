package bot.data.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

//import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
//import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "clients")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Client {
    @Id
    private String id;
}

//@DynamoDBTable(tableName = "Client")
//@AllArgsConstructor
//@NoArgsConstructor
//public class Client {
//    @Id
//    private String id;
//    @DynamoDBHashKey(attributeName = "id")
//    public String getId() {
//        return id;
//    }
//
//    public void setId(String id) {
//        this.id = id;
//    }
//}


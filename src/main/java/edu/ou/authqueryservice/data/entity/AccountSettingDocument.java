package edu.ou.authqueryservice.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Data
@Document("AccountSetting")
public class AccountSettingDocument implements Serializable {
    @Id
    @JsonIgnore
    private String id;
    @JsonIgnore
    private int accountId;
    @JsonIgnore
    private int roleId;
    @JsonIgnore
    private int permissionId;
    private boolean status;
}

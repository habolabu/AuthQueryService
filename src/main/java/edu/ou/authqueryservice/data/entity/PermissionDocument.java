package edu.ou.authqueryservice.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document("Permission")
public class PermissionDocument {
    @Id
    @JsonIgnore
    private String id;
    @JsonProperty("id")
    private int oId;
    private String name;
    private String display;
    @JsonIgnore
    private int parentId;
    private int childOrder;
    private String type;
    @JsonIgnore
    private String serviceId;
    @Transient
    private List<PermissionDocument> permissionDocuments;
    @Transient
    private boolean status;
}

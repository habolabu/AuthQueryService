package edu.ou.authqueryservice.data.pojo.response.permission;

import edu.ou.authqueryservice.data.entity.PermissionDocument;
import lombok.Data;

import java.util.List;

@Data
public class PermissionFindByAccountIdResponse {
    private int roleId;
    private String roleName;
    private String roleDisplay;
    private List<PermissionDocument> permissions;
}

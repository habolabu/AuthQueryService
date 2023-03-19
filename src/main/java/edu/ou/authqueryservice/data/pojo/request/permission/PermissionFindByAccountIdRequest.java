package edu.ou.authqueryservice.data.pojo.request.permission;

import edu.ou.coreservice.data.pojo.request.base.IBaseRequest;
import lombok.Data;

@Data
public class PermissionFindByAccountIdRequest implements IBaseRequest {
    private int accountId;
}

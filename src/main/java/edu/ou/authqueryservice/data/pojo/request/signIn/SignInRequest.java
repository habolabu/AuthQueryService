package edu.ou.authqueryservice.data.pojo.request.signIn;

import edu.ou.coreservice.data.pojo.request.base.IBaseRequest;
import lombok.Data;

@Data
public class SignInRequest implements IBaseRequest {
    private String username;
    private String password;
}

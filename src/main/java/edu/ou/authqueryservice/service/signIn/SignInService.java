package edu.ou.authqueryservice.service.signIn;


import edu.ou.authqueryservice.common.constant.CodeStatus;
import edu.ou.authqueryservice.config.JwtAccessTokenProvider;
import edu.ou.authqueryservice.data.pojo.request.signIn.SignInRequest;
import edu.ou.coreservice.common.constant.Message;
import edu.ou.coreservice.common.exception.BusinessException;
import edu.ou.coreservice.common.validate.ValidValidation;
import edu.ou.coreservice.data.pojo.request.base.IBaseRequest;
import edu.ou.coreservice.data.pojo.response.base.IBaseResponse;
import edu.ou.coreservice.data.pojo.response.impl.SuccessPojo;
import edu.ou.coreservice.data.pojo.response.impl.SuccessResponse;
import edu.ou.coreservice.service.base.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.net.URISyntaxException;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class SignInService extends BaseService<IBaseRequest, IBaseResponse> {
    private final AuthenticationManager authenticationManager;
    private final JwtAccessTokenProvider jwtAccessTokenProvider;
    private final ValidValidation validValidation;

    /**
     * Validate request
     *
     * @param request input of task
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected void preExecute(IBaseRequest request) {
        if (validValidation.isInValid(request, SignInRequest.class)) {
            throw new BusinessException(
                    CodeStatus.INVALID_INPUT,
                    Message.Error.INVALID_INPUT,
                    "account"

            );
        }
    }

    /**
     * sign in to get access token
     *
     * @param request request from client
     * @return response to client
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected IBaseResponse doExecute(IBaseRequest request) {
        final SignInRequest signInRequest = (SignInRequest) request;
        try {
            final Map<String, String> token = jwtAccessTokenProvider.generateToken(
                    signInRequest.getUsername(),
                    signInRequest.getPassword());

            final Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            signInRequest.getUsername(),
                            signInRequest.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            return new SuccessResponse<>(
                    new SuccessPojo<>(
                            token,
                            CodeStatus.SUCCESS,
                            Message.Success.SUCCESSFUL
                    )
            );
        } catch (URISyntaxException | ExecutionException | InterruptedException e) {
            throw new BusinessException(
                    CodeStatus.SERVER_ERROR,
                    Message.Error.UN_KNOWN
            );
        }


    }

    @Override
    protected void postExecute(IBaseRequest input) {
        // do nothing
    }
}

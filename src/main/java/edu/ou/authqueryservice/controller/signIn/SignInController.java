package edu.ou.authqueryservice.controller.signIn;

import edu.ou.authqueryservice.common.constant.EndPoint;
import edu.ou.authqueryservice.data.pojo.request.signIn.SignInRequest;
import edu.ou.coreservice.common.constant.SecurityPermission;
import edu.ou.coreservice.data.pojo.request.base.IBaseRequest;
import edu.ou.coreservice.data.pojo.response.base.IBaseResponse;
import edu.ou.coreservice.service.base.IBaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(EndPoint.SignIn.BASE)
public class SignInController {

    private final IBaseService<IBaseRequest, IBaseResponse> signInService;

    /**
     * Add new password
     *
     * @param signInRequest new password information
     * @return id of new password
     * @author Nguyen Trung Kien - OU
     */
    @PreAuthorize(SecurityPermission.PERMIT_ALL)
    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<IBaseResponse> signIn(
            @Validated
            @RequestBody
            SignInRequest signInRequest
    ) {
        return new ResponseEntity<>(
                signInService.execute(signInRequest),
                HttpStatus.OK
        );
    }
}

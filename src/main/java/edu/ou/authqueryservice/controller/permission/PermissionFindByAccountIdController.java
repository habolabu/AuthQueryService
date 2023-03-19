package edu.ou.authqueryservice.controller.permission;

import edu.ou.authqueryservice.common.constant.EndPoint;
import edu.ou.authqueryservice.data.pojo.request.permission.PermissionFindByAccountIdRequest;
import edu.ou.coreservice.common.constant.SecurityPermission;
import edu.ou.coreservice.data.pojo.request.base.IBaseRequest;
import edu.ou.coreservice.data.pojo.response.base.IBaseResponse;
import edu.ou.coreservice.service.base.IBaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping(EndPoint.Permission.BASE)
public class PermissionFindByAccountIdController {
    private final IBaseService<IBaseRequest, IBaseResponse> permissionFindByAccountIdService;

    /**
     * find all permission
     *
     * @return permission list
     * @author Nguyen Trung Kien - OU
     */
    @PreAuthorize(SecurityPermission.AUTHENTICATED)
    @GetMapping(EndPoint.Permission.FIND_BY_ACCOUNT)
    public ResponseEntity<IBaseResponse> findAllPermissionByAccountPagination(
            @PathVariable int accountId
    ) {
        return new ResponseEntity<>(
                permissionFindByAccountIdService.execute(
                        new PermissionFindByAccountIdRequest().setAccountId(accountId)),
                HttpStatus.OK
        );
    }
}

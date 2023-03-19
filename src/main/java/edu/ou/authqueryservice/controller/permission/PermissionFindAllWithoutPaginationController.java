package edu.ou.authqueryservice.controller.permission;

import edu.ou.authqueryservice.common.constant.EndPoint;
import edu.ou.coreservice.common.constant.SecurityPermission;
import edu.ou.coreservice.data.pojo.request.base.IBaseRequest;
import edu.ou.coreservice.data.pojo.response.base.IBaseResponse;
import edu.ou.coreservice.service.base.IBaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(EndPoint.Permission.BASE)
public class PermissionFindAllWithoutPaginationController {
    private final IBaseService<IBaseRequest, IBaseResponse> permissionFindAllWithoutPaginationService;

    /**
     * find all permission
     *
     * @return permission list
     * @author Nguyen Trung Kien - OU
     */
    @PreAuthorize(SecurityPermission.AUTHENTICATED)
    @GetMapping(EndPoint.Permission.ALL)
    public ResponseEntity<IBaseResponse> findAllPermissionWithoutPagination() {
        return new ResponseEntity<>(
                permissionFindAllWithoutPaginationService.execute(null),
                HttpStatus.OK
        );
    }
}

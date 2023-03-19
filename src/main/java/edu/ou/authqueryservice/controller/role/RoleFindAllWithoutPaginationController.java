package edu.ou.authqueryservice.controller.role;

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
@RequestMapping(EndPoint.Role.BASE)
public class RoleFindAllWithoutPaginationController {
    private final IBaseService<IBaseRequest, IBaseResponse> roleFindAllWithoutPaginationService;

    /**
     * find all role
     *
     * @return role list
     * @author Nguyen Trung Kien - OU
     */
    @PreAuthorize(SecurityPermission.AUTHENTICATED)
    @GetMapping(EndPoint.Role.ALL)
    public ResponseEntity<IBaseResponse> findAllRoleWithoutPagination() {
        return new ResponseEntity<>(
                roleFindAllWithoutPaginationService.execute(null),
                HttpStatus.OK
        );
    }
}

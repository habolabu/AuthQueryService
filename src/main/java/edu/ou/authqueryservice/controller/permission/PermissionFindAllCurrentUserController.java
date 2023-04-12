package edu.ou.authqueryservice.controller.permission;

import edu.ou.authqueryservice.common.constant.CodeStatus;
import edu.ou.authqueryservice.common.constant.EndPoint;
import edu.ou.coreservice.common.constant.Message;
import edu.ou.coreservice.common.constant.SecurityPermission;
import edu.ou.coreservice.common.util.SecurityUtils;
import edu.ou.coreservice.data.pojo.response.base.IBaseResponse;
import edu.ou.coreservice.data.pojo.response.impl.SuccessPojo;
import edu.ou.coreservice.data.pojo.response.impl.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping(EndPoint.Permission.BASE)
public class PermissionFindAllCurrentUserController {
    private final RabbitTemplate rabbitTemplate;

    /**
     * find all permission of current user
     *
     * @return permission list
     * @author Nguyen Trung Kien - OU
     */
    @PreAuthorize(SecurityPermission.AUTHENTICATED)
    @GetMapping()
    public ResponseEntity<IBaseResponse> findAllPermissionOfCurrentUser() {
        return new ResponseEntity<>(
                new SuccessResponse<>(
                        new SuccessPojo<>(
                                SecurityUtils.getCurrentAccount(rabbitTemplate).getPermissions(),
                                CodeStatus.SUCCESS,
                                Message.Success.SUCCESSFUL
                        )
                ),
                HttpStatus.OK
        );
    }
}

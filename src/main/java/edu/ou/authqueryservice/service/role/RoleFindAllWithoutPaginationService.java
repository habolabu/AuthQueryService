package edu.ou.authqueryservice.service.role;

import edu.ou.authqueryservice.common.constant.CodeStatus;
import edu.ou.authqueryservice.data.entity.RoleDocument;
import edu.ou.coreservice.common.constant.Message;
import edu.ou.coreservice.data.pojo.request.base.IBaseRequest;
import edu.ou.coreservice.data.pojo.response.base.IBaseResponse;
import edu.ou.coreservice.data.pojo.response.impl.SuccessPojo;
import edu.ou.coreservice.data.pojo.response.impl.SuccessResponse;
import edu.ou.coreservice.repository.base.IBaseRepository;
import edu.ou.coreservice.service.base.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleFindAllWithoutPaginationService extends BaseService<IBaseRequest, IBaseResponse> {
    private final IBaseRepository<Query, List<RoleDocument>> roleFindAllRepository;

    @Override
    protected void preExecute(IBaseRequest request) {
        // do nothing
    }

    /**
     * Find all role
     *
     * @param request request
     * @return role list
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected IBaseResponse doExecute(IBaseRequest request) {
        return new SuccessResponse<>(
                new SuccessPojo<>(
                        roleFindAllRepository.execute(new Query()),
                        CodeStatus.SUCCESS,
                        Message.Success.SUCCESSFUL
                )
        );
    }

    @Override
    protected void postExecute(IBaseRequest input) {
        // do nothing
    }
}

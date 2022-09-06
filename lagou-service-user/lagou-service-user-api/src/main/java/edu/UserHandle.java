package edu;

import com.lagou.edu.ResModel;
import edu.request.UserRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

public interface UserHandle {

    /**
     * 登入提交接口
     * @param email     登入邮箱
     * @param password  登入密码
     * @return          返回登入状态的实体
     */
    ResModel<?> login( String email, String password  );

    /**
     * 注册提交接口
     * @param userRequest 用户注册参数
     */
    ResModel<?> register ( UserRequest userRequest );

    ResModel<?> getUserInfo(String tokenId );

}

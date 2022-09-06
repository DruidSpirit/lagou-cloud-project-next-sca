package com.lagou.edu.sentinel;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.lagou.edu.ResModel;

@SuppressWarnings("unused")
public class UserHandles {


    public static ResModel<?> loginBlockHandler(String email,String password,BlockException blockException){
        return ResModel.FAIL("登入限流，不能频繁登入！");
    }

    public static ResModel<?> loginFallback(String email,String password){
        return ResModel.FAIL("登入熔断，系统异常！");
    }

}

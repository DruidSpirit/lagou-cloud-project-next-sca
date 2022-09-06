package com.lagou.edu.sentinel;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.lagou.edu.ResModel;

public class CreateCodeHandle {

    public static ResModel<?> sendVerificationCodeBlockHandler(String email, BlockException blockException) {

        return ResModel.FAIL("验证码发送服务器限流");
    }

    public static ResModel<?> sendVerificationCodeFallback(String email) {

        return ResModel.FAIL("验证码发送服务器熔断");
    }
}

package com.lagou.edu.api;

import com.lagou.edu.ResModel;
public interface VerificationCodeCreate {


    ResModel createCodeAndSendEmail(String email );
}

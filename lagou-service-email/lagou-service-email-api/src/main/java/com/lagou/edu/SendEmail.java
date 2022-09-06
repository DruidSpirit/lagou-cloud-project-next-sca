package com.lagou.edu;

public interface SendEmail {

    /**
     * 通过邮箱发送验证码
     * @param verificationCode  被发送的验证码
     * @return                  true 发送成功 false 发送失败
     */
    boolean sendVerificationCode (String verificationCode,String email);

    String testEmail(String s);
}

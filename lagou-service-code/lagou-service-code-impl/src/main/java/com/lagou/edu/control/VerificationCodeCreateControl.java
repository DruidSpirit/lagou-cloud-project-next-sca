package com.lagou.edu.control;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.lagou.edu.dao.LagouAuthCodeDao;
import com.lagou.edu.ResModel;
import com.lagou.edu.SendEmail;
import com.lagou.edu.api.VerificationCodeCreate;
import com.lagou.edu.dao.LagouUserDao;
import com.lagou.edu.entity.LagouAuthCode;
import com.lagou.edu.entity.LagouUser;
import com.lagou.edu.sentinel.CreateCodeHandle;
import com.lagou.edu.util.UtilForData;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.domain.Example;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDateTime;
import java.util.Optional;

@RequestMapping("verificationCode")
@RestController
@RefreshScope
public class VerificationCodeCreateControl implements VerificationCodeCreate {

    @Reference
    private  SendEmail sendEmail;

    @Autowired
    private  LagouAuthCodeDao lagouAuthCodeDao;

    @Autowired
    private LagouUserDao lagouUserDao;

    @Value("${code-setting.expire-time}")
    private Long expireTime;
    /**
     * 生产验证码并通过邮箱发送
     *
     * @return 验证码
     */
    @Override
    @SentinelResource(value = "createCodeAndSendEmail",blockHandlerClass = CreateCodeHandle.class,
            blockHandler = "sendVerificationCodeBlockHandler",fallbackClass = CreateCodeHandle.class,fallback = "sendVerificationCodeFallback")
    @GetMapping("/createCodeAndSendEmail/{email}")
    public ResModel<?> createCodeAndSendEmail( @PathVariable String email ) {

        LagouUser lagouUser = new LagouUser();
        lagouUser.setUsername(email);
        Optional<LagouUser> one = lagouUserDao.findOne(Example.of(lagouUser));
        if ( one.isPresent() ) {
            return ResModel.FAIL("用户已存在");
        }
        //  生成6位数随机码
        String vCode = UtilForData.randomCodeByDigit();

        try {
            //  储存验证码
            LagouAuthCode lagouAuthCode = new LagouAuthCode();
            lagouAuthCode.setEmail(email);
            lagouAuthCode.setCode(vCode);
            LocalDateTime now = LocalDateTime.now();
            lagouAuthCode.setCreateTime( now );
            LocalDateTime expireDateTime = now.plusMinutes(expireTime);
            lagouAuthCode.setExpireTime(expireDateTime);
            lagouAuthCodeDao.save(lagouAuthCode);

            //  发送邮件
            return sendEmail.sendVerificationCode(vCode,email)
                    ? ResModel.SUCCESS()
                    :ResModel.FAIL("验证码发送失败");
        }catch (Exception e){
            e.printStackTrace();
            return ResModel.FAIL("验证码发送失败");
        }
    }

    @GetMapping("/test")
    public ResModel<?> test (){

        String testEmail = sendEmail.testEmail("验证码逾期时间是"+expireTime+"分钟");
        return ResModel.SUCCESS(testEmail);
    }
}

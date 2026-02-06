package com.houkunlin.common.aop;

import com.houkunlin.common.aop.annotation.RequestRateLimiter;
import com.houkunlin.common.aop.limit.LimitMethod;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/RequestRateLimiter/")
@RequiredArgsConstructor
public class RequestRateLimiterController {

    @RequestRateLimiter(useLock = false, method = LimitMethod.M1)
    @GetMapping("m11")
    public Object m11() {

        return true;
    }

    @RequestRateLimiter(useLock = true, method = LimitMethod.M1)
    @GetMapping("m12")
    public Object m12() {

        return true;
    }

    @RequestRateLimiter(useLock = false, method = LimitMethod.M2)
    @GetMapping("m21")
    public Object m21() {

        return true;
    }

    @RequestRateLimiter(useLock = true, method = LimitMethod.M2)
    @GetMapping("m22")
    public Object m22() {

        return true;
    }

    @RequestRateLimiter(useLock = false, method = LimitMethod.M3)
    @GetMapping("m31")
    public Object m31() {

        return true;
    }

    @RequestRateLimiter(useLock = true, method = LimitMethod.M3)
    @GetMapping("m32")
    public Object m32() {

        return true;
    }
}

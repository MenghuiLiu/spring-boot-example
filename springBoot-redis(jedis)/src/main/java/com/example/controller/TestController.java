package com.example.controller;

import com.example.redis.RedisCache;
import com.example.redis.RedisLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author 刘梦辉
 * @description
 * @date 2020/1/16
 */
@Controller
@ResponseBody
public class TestController {

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private RedisLock redisLock;

    @GetMapping("/test")
    public Boolean test(){
        Boolean status = redisCache.set("msg", "11");
        return status;
    }

    @GetMapping("/edit1")
    public void edit1() {
        boolean b = redisLock.tryGetLock("lockkey", "xiaoming", 60 * 1000);
        if (b) {
            String msg = redisCache.get("msg");
            Boolean msg1 = redisCache.set("msg", msg + "xiaoming");
            if (msg1) {
                redisLock.releaseLock("lockkey", "xiaoming");
            }
        }
    }

    /**
     * @description 对资源做修改时，先获取锁，操作完成后再释放出来。
     *              当数据修改失败了，因为对锁设置了过期时间，一分钟后锁失效，释放资源
     * @author 刘梦辉
     * @date 2020/1/16
     */
    @GetMapping("/edit2")
    public void edit2() {
        boolean b = redisLock.tryGetLock("lockkey", "shi", 60 * 1000);
        if (b) {
            String msg = redisCache.get("msg");
            Boolean msg1 = redisCache.set("msg", msg + "shi");
            if (msg1) {
                redisLock.releaseLock("lockkey", "shi");
            }
        }
    }
}

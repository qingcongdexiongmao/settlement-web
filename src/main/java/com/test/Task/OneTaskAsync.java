package com.test.Task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.servlet.AsyncContext;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

/**
 * Created by 青葱的熊猫 on 2017/6/20.
 * 线程调度
 */
@Component
public class OneTaskAsync {


    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 方式一：使用注解标签
     * @param ctx 异步上下文
     */
    @Async("oneTaskAsyncPool")
    public void submit(final AsyncContext ctx){
        logger.info("进入异步方法");
        ctx.setTimeout(20000);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            HttpServletResponse resp = (HttpServletResponse) ctx.getResponse();
            resp.setContentType("text/html;charset=UTF-8");
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write("中文");
        } catch (IOException e) {
            e.printStackTrace();
        }
        ctx.complete();// 完成
        logger.info("已执行完");
    }

    /**
     * 方式二：外部调用端已匿名内部类的方式,此方式可以公共
     * @param ctx   异步上下文对象
     * @param task 待调度任务
     */
    @Async("oneTaskAsyncPool")
    public void submit(final AsyncContext ctx, Callable<Object> task){
        logger.info("进入异步方法");
        ctx.setTimeout(20000);
        try {
            Thread.sleep(3000);
            Object o = task.call();
            this.callBack(ctx, o);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.info("已执行完");
    }

    private void callBack(AsyncContext ctx, Object result) throws IOException {
        HttpServletResponse res = (HttpServletResponse) ctx.getResponse();
        if(null != result){
            logger.info("输出结果====" + result.toString());
            res.setContentType("text/html;charset=UTF-8");
            res.setStatus(HttpServletResponse.SC_OK);
            res.getWriter().write(result.toString());
        }
        //响应容器
        ctx.complete();
    }
}

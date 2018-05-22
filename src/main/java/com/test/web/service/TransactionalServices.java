package com.test.web.service;

import com.test.web.properties.TransactionalProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by 青葱的熊猫 on 2018/5/20.
 * 1、@Transactional 只有通过接口形式调用才可以生效
 * 2、@Transactional 只对public方法生效（话说接口声明也没有私有啊）
 * 3、@Transactional 方法中调用本类内部声明新@Transactional方法，新事务不生效直接被老事务覆盖
 * 4、@Transactional 方法中调用本类内部无@Transactional方法，事务作用全局，全部生效
 * 5、@Transactional 方法中接口调用外部无@Transactional方法，事务作用全局，全部生效
 * 6、@Transactional 方法中调用本类@Transactional非public方法或无@Transactional非public方法，事务作用全局，全部生效
 * 7、@Transactional 方法中接口调用外部新@Transactional方法，，新事务生效不会被老事务覆盖
 *    1️⃣ 新事务回滚，老事务也回滚
 *    2️⃣ 老事务回滚，新事务不回滚
 * 8、无@Transactional 方法中调用本类@Transactional方法，事务不生效
 * 9、无@Transactional 方法中接口调用本类@Transactional方法，事务生效。与1呼应
 * 10、Transactional是否生效, 仅取决于是否加载于接口方法, 并且是否通过方法调用(而不是本类调用)。默认RuntimeException回滚
 * 11、通过 元素的 "proxy-target-class" 属性值来控制是基于接口的还是基于类的代理被创建。
 *      如果 "proxy-target-class" 属值被设置为 "true"，那么基于类的代理将起作用（这时需要CGLIB库cglib.jar在CLASSPATH中）。
 *      如果 "proxy-target-class" 属值被设置为 "false" 或者这个属性被省略，那么标准的JDK基于接口的代理将起作用
 * 12、只有当数据源和事务管理器相对应时，事务管理器才发生作用
 */
@Service
public class TransactionalServices {


    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private TransactionalProperties transactionalProperties;

    @Autowired
    private TransactionalNewServices transactionalNewServices;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 有事务标签且方法公开
     * 外部调用事务生效
     */
    @Transactional
    public void testOutPublic(){
        logger.info("有事务标签且方法公开-事务开始");
        this.jdbcTemplate.update(this.transactionalProperties.UPDATE_TEST_SQL);
        logger.info("有事务标签且方法公开-事务结束");
        throw new RuntimeException("new testOutPublic");
    }

    /**
     * 无事务标签且方法公开
     * 外部调用无事务
     */
    public void testOutPublicNOTr(){
        logger.info("无事务标签且方法公开-事务开始");
        this.jdbcTemplate.update(this.transactionalProperties.UPDATE_TESTOUTPUBLICNOTR_SQL);
        logger.info("无事务标签且方法公开-事务结束");
        throw new RuntimeException("new testOutPublicNOTr");
    }

    /**
     * 无事务标签且方法公开,本类调用内部有事务公开方法
     * 事务不生效
     */
    public void testOutPublicNoTrToPubTr(String setp){
        logger.info("无事务标签且方法公开,本类调用内部有事务公开方法-事务开始");
        this.testOutPublic();
        logger.info("无事务标签且方法公开,本类调用内部有事务公开方法-事务结束");
        throw new RuntimeException("new testOutPublicNoTrToPubTr");
    }

    /**
     * 有事务标签且方法公开,外部调用。本类调用内部无事务私有方法
     * 事务全部生效
     */
    @Transactional
    public void testOutPublicTrToAll(){
        logger.info("有事务标签且方法公开,外部调用。本类调用内部无事务私有方法-事务开始");
//        this.testOutPublicNOTr();
        this.testPrivate();
        logger.info("有事务标签且方法公开,外部调用。本类调用内部无事务私有方法-事务结束");
        throw new RuntimeException("new testOutPublicTrToPri");
    }

    /**
     * 私有方法无事务标签
     */
    private void testPrivate(){
        logger.info("私有方法无事务标签-事务开始");
        this.jdbcTemplate.update(this.transactionalProperties.UPDATE_TESTPRINOTR_SQL);
        logger.info("私有方法无事务标签-事务结束");
    }

    /**
     * 有事务且方法公开，外部调用。本地调用内部新事务公开方法
     * 本类调用内部方法，新事务无效
     */
    @Transactional
    public void testOutPublicTrToNewTr(){
        logger.info("有事务标签且方法公开,外部调用。本类调用内部无事务私有方法-事务开始");
        this.testPublicNewTr();
        logger.info("有事务标签且方法公开,外部调用。本类调用内部无事务私有方法-事务结束");
        throw new RuntimeException("new testOutPublicTrToNewTr");
    }

    /**
     * 新开事务公开方法
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void testPublicNewTr(){
        logger.info("新开事务公开方法-事务开始");
        this.jdbcTemplate.update(this.transactionalProperties.UPDATE_TESTPUBLICNEWTR_SQL);
        logger.info("新开事务公开方法-事务结束");
    }

    /**
     * 有事务且方法公开，外部调用。调用外部新事务公开方法
     * 1、本类异常回滚，接口方式调用的新事务方法不回滚
     * 2、接口方法异常回滚，接口方式调用的新事务方法外部也回滚
     */
    @Transactional
    public void testOutPublicTrToNewSerTr(String setp){
        logger.info("有事务且方法公开，外部调用。调用外部新事务公开方法-事务开始");
        this.testPrivate();
        this.transactionalNewServices.testPublicNewTr(setp);
        logger.info("有事务且方法公开，外部调用。调用外部新事务公开方法-事务结束");
        //外部异常回滚，接口方式调用的新事务方法不回滚
        if(setp.equals("1")){
            throw new RuntimeException("new testOutPublicTrToNewSerTr");
        }
    }

    /**
     * 有事务且方法公开，外部调用。调用外部无事务公开方法
     * 接口调用外部无事务方法，事务生效
     */
    @Transactional
    public void testOutPublicTrToNewSerNoTr(){
        logger.info("有事务且方法公开，外部调用。调用外部无事务公开方法-事务开始");
        this.testPrivate();
        this.transactionalNewServices.testPublicNewNoTr();
        logger.info("有事务且方法公开，外部调用。调用外部无事务公开方法-事务结束");
        throw new RuntimeException("new testOutPublicTrToNewSerTr");
    }

}

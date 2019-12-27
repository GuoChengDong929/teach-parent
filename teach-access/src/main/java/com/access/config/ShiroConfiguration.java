package com.access.config;

import com.teach.shiro.CORSAuthenticationFilter;
import com.teach.shiro.CustomSessionManager;

import com.teach.shiro.ParentRealm;
import com.teach.shiro.ShiroUserFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.Map;


@Configuration
public class ShiroConfiguration {

    //1.创建realm
    @Bean
    public ParentRealm getRealm() {
        return new AccessRealm();
    }

    //2.创建安全管理器
    @Bean
    public SecurityManager getSecurityManager(ParentRealm realm) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(realm);

        //将自定义的会话管理器注册到安全管理器中
        securityManager.setSessionManager(sessionManager());
        //将自定义的redis缓存管理器注册到安全管理器中
        securityManager.setCacheManager(cacheManager());

        return securityManager;
    }

    //3.配置shiro的过滤器工厂

    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean() {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(getSecurityManager(getRealm()));
        // 没有登陆的用户只能访问登陆页面，前后端分离中登录界面跳转应由前端路由控制，后台仅返回json数据

        // 登录成功后要跳转的链接
        //shiroFilterFactoryBean.setSuccessUrl("/auth/index");
        // 未授权界面;
        shiroFilterFactoryBean.setLoginUrl("/autherror?code=1");//跳转url地址
        shiroFilterFactoryBean.setUnauthorizedUrl("/autherror?code=2");//未授权的url

        //添加过滤器
        Map<String, Filter> filters = shiroFilterFactoryBean.getFilters();
        // 注意这里不要用Bean的方式，否则会报错
        //filters.put("authc", new ShiroUserFilter());

        filters.put("authc",new CORSAuthenticationFilter());
        shiroFilterFactoryBean.setFilters(filters);

        Map<String, String> filterMap = new LinkedHashMap<>();
        //注意过滤器配置顺序 不能颠倒
        //配置退出 过滤器,其中的具体的退出代码Shiro已经替我们实现了，登出后跳转配置的loginUrl
        filterMap.put("/logout", "logout");

        // 静态资源
        //filterMap.put("/static/**", "anon");

        // 放行追踪
        filterMap.put("/actuator/**","anon");

        // 登录方法
        filterMap.put("/sys/login", "anon");

        // 注册方法
        filterMap.put("/sys/register", "anon");

        //swagger2
        //filterMap.put("/swagger-ui.html", "anon");
        //filterMap.put("/swagger-resources/**", "anon");
        //filterMap.put("/v2/**", "anon");
        //filterMap.put("/webjars/**", "anon");


        filterMap.put("/**", "authc");

        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterMap);

        return shiroFilterFactoryBean;
    }


    /*@Bean //再web程序中，shiro进行权限控制全部是通过一组过滤器集合进行控制
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
        //1.创建过滤器工厂
        ShiroFilterFactoryBean filterFactory = new ShiroFilterFactoryBean();
        //2.设置安全管理器
        filterFactory.setSecurityManager(securityManager);
        //3.通用配置（跳转登录页面，未授权跳转的页面）
        filterFactory.setLoginUrl("/autherror?code=1");//跳转url地址
        filterFactory.setUnauthorizedUrl("/autherror?code=2");//未授权的url
        //4.设置过滤器集合
        Map<String,String> filterMap = new LinkedHashMap<>();
        //anon -- 匿名访问
        //filterMap.put("/**","anon");
        filterMap.put("/sys/login","anon");// 生产环境打开
        filterMap.put("/authError","anon"); //生产环境打开
        //注册
        //authc -- 认证之后访问（登录）
        filterMap.put("/**","authc"); //生产环境打开
        //perms -- 具有某中权限 (使用注解配置授权)
        filterFactory.setFilterChainDefinitionMap(filterMap);

        return filterFactory;
    }*/


//    @Value("${spring.redis.host}")
//    private String host;
//    @Value("${spring.redis.port}")
//    private int port;
//    @Value("${spring.redis.password}")
//    private String password;

    /**
     * 4.redis的控制器，操作redis
     */
    public RedisManager redisManager() {
        RedisManager redisManager = new RedisManager();
        redisManager.setHost("192.168.18.250");
        //redisManager.setHost("192.168.223.129");
        redisManager.setPort(6379);

        redisManager.setPassword("dmcn");
        return redisManager;
    }

    /**
     * 5.sessionDao
     */
    public RedisSessionDAO redisSessionDAO() {
        RedisSessionDAO sessionDAO = new RedisSessionDAO();
        sessionDAO.setRedisManager(redisManager());
        return sessionDAO;
    }

    /**
     * 6.会话管理器
     */
    public DefaultWebSessionManager sessionManager() {
        CustomSessionManager sessionManager = new CustomSessionManager();
        sessionManager.setSessionDAO(redisSessionDAO());
        //禁用cookie
        //sessionManager.setSessionIdCookieEnabled(false);
        //禁用url重写   url;jsessionid=id
        //sessionManager.setSessionIdUrlRewritingEnabled(false);
        return sessionManager;
    }

    /**
     * 7.缓存管理器
     */
    public RedisCacheManager cacheManager() {
        RedisCacheManager redisCacheManager = new RedisCacheManager();
        redisCacheManager.setRedisManager(redisManager());
        return redisCacheManager;
    }




    //8. 开启对shiro注解的支持
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }
}

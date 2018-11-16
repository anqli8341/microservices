package demo.msa.framework.registry.listener;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import demo.msa.framework.registry.sevice.ServiceRegistry;

/**
 * @author anquan li
 */
@Component
public class WebListener implements ServletContextListener {

  @Autowired
  ServiceRegistry serviceRegistry;
  @Value("${server.address}")
  private String serverAddress;
  @Value("${server.port}")
  private int serverPort;

  @Override
  public void contextInitialized(ServletContextEvent servletContextEvent) {
    //获取请求映射
    ServletContext servletContext = servletContextEvent.getServletContext();
    ApplicationContext applicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
    RequestMappingHandlerMapping mappingHandlerMapping = applicationContext.getBean(RequestMappingHandlerMapping.class);
    Map<RequestMappingInfo, HandlerMethod> infoHandlerMethodMap = mappingHandlerMapping.getHandlerMethods();
    for (RequestMappingInfo info : infoHandlerMethodMap.keySet()) {
      String serviceName = info.getName();
      if (null != serviceName) {
        //注册服务
        serviceRegistry.register(serviceName, String.format("%s:%d", serverAddress, serverPort));
      }
    }
  }

  @Override
  public void contextDestroyed(ServletContextEvent servletContextEvent) {

  }
}

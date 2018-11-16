package demo.msa.framework.registry.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import demo.msa.framework.registry.impl.ServiceRegistryImpl;
import demo.msa.framework.registry.sevice.ServiceRegistry;

/**
 * @author anquan li
 */
@Configuration
@ConfigurationProperties(prefix = "registry")
public class RegistryConfig {
  private String servers;

  @Bean
  public ServiceRegistry serviceRegistry() {
    return new ServiceRegistryImpl(servers);
  }

  public void setServers(String servers) {
    this.servers = servers;
  }
}

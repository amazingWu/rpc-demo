package xyz.amazingwu.blog.rpc.client;

import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.util.Set;

/**
 * @author amazingjadewu@gmail.com
 * @created 2020年02月28日 17:33
 */
public class RpcInitConfig implements ImportBeanDefinitionRegistrar {


  @Override
  public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry beanDefinitionRegistry) {
    ClassPathScanningCandidateComponentProvider provider = getScanner();
    // 扫描注解器
    provider.addIncludeFilter(new AnnotationTypeFilter(RpcClient.class));
    Set<BeanDefinition> beanDefinitionSet = provider.findCandidateComponents("xyz.amazingwu.blog.rpc");
    for (BeanDefinition beanDefinition : beanDefinitionSet) {
      if (beanDefinition instanceof AnnotatedBeanDefinition) {
        String beanClassAllName = beanDefinition.getBeanClassName();
        //将RpcClient的工厂类注册进去
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(RpcClientFactoryBean.class);
        //设置RpcClientFactoryBean工厂类中的构造函数的值
        builder.addConstructorArgValue(beanClassAllName);
        builder.getBeanDefinition().setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
        //将其注册进容器中
        beanDefinitionRegistry.registerBeanDefinition(beanClassAllName, builder.getBeanDefinition());
      }
    }
  }

  /**
   * 允许Spring扫描接口上的注解
   */
  protected ClassPathScanningCandidateComponentProvider getScanner() {
    return new ClassPathScanningCandidateComponentProvider(false) {
      @Override
      protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        return beanDefinition.getMetadata().isInterface() && beanDefinition.getMetadata().isIndependent();
      }
    };
  }
}

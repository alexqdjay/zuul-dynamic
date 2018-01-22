/**
 * (C) Copyright 2017 alex qian
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 **/

package me.ucake.zuuldy.core;

import com.netflix.client.config.IClientConfig;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.netflix.zuul.filters.RouteLocator;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.cloud.netflix.zuul.filters.discovery.ServiceRouteMapper;
import org.springframework.cloud.netflix.zuul.web.ZuulController;
import org.springframework.cloud.netflix.zuul.web.ZuulHandlerMapping;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

/**
 *
 * ZuulDynamicConfig
 *
 * @author alex.Q
 * @date 2017/9/15
 */
@Configuration
public class ZuulDynamicConfig {


    @Bean
    public ZuulDynamicMappingRegister zuulDynamicMappingRegister() {
        return new ZuulDynamicMappingRegister();
    }

    @Bean
    public DynamicRouteLocator dynamicRouteLocator(RouteStore routeStore,
            ServerProperties serverProperties,
            ZuulProperties zuulProperties, DiscoveryClient discovery) {
        return new DynamicRouteLocator(routeStore, serverProperties.getServletPath(), discovery, zuulProperties);
    }

    @ConditionalOnMissingBean(RouteStore.class)
    @Bean
    public RouteStore routeStore() {
        return new RouteStore() {
            @Override
            public List<ZuulProperties.ZuulRoute> getAllRoutes() {
                return Collections.emptyList();
            }

            @Override
            public void onRoutesChange(Consumer<List<ZuulProperties.ZuulRoute>> handleFunction) {
                handleFunction.accept(getAllRoutes());
            }

            @Override
            public List<ServiceRoute> getAllServiceRoutes() {
                return Collections.emptyList();
            }
        };
    }


    @Bean
    public ServiceRoutes serviceRoutes(RouteStore routeStore) {
        return new ServiceRoutes(routeStore);
    }

    @Bean
    public DynamicServerList dynamicServerList(IClientConfig clientConfig, ServiceRoutes serviceRoutes) {
        DynamicServerList dynamicServerList = new DynamicServerList(serviceRoutes);
        dynamicServerList.initWithNiwsConfig(clientConfig);
        return dynamicServerList;
    }

    @Bean
    public RouteChangeHandler routeChangeHandler() {
        return new RouteChangeHandler();
    }

    private static class ZuulDynamicMappingRegister implements BeanPostProcessor {

        @Autowired
        private RouteLocator routeLocator;

        @Autowired
        private ServiceRouteMapper serviceRouteMapper;

        @Autowired
        private ZuulController zuulController;

        @Autowired(required = false)
        private ErrorController errorController;

        @Override
        public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
            if (bean instanceof ZuulHandlerMapping) {
                ZuulDynamicMapping mapping = new ZuulDynamicMapping(routeLocator, zuulController);
                mapping.setErrorController(errorController);
                return mapping;
            }
            return bean;
        }

        @Override
        public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
            return bean;
        }
    }

}

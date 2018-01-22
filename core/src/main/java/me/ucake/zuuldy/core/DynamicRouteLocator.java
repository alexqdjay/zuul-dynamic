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

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.cloud.netflix.zuul.filters.discovery.DiscoveryClientRouteLocator;

import java.util.LinkedHashMap;

/**
 *
 * DynamicRouteLocator
 *
 * @author alex.Q
 * @date 2017/9/15
 **/
public @Data class DynamicRouteLocator extends DiscoveryClientRouteLocator {

    private RouteStore routeStore;

    public DynamicRouteLocator(RouteStore routeStore, String servletPath, DiscoveryClient discovery, ZuulProperties properties) {
        super(servletPath, discovery, properties);

        this.routeStore = routeStore;
    }

    @Override
    protected LinkedHashMap<String, ZuulProperties.ZuulRoute> locateRoutes() {
        LinkedHashMap<String, ZuulProperties.ZuulRoute> routeMap = super.locateRoutes();

        routeStore.getAllRoutes().forEach(route -> routeMap.put(route.getPath(), route));

        return routeMap;
    }


}

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
import com.netflix.loadbalancer.AbstractServerList;
import com.netflix.loadbalancer.Server;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * DynamicServerList
 *
 * @author alex.Q
 * @date 2017/9/20
 */
public class DynamicServerList extends AbstractServerList<Server> {

    private IClientConfig clientConfig;
    private ServiceRoutes serviceRoutes;

    public DynamicServerList(ServiceRoutes serviceRoutes) {
        this.serviceRoutes = serviceRoutes;
    }

    @Override
    public void initWithNiwsConfig(IClientConfig clientConfig) {
        this.clientConfig = clientConfig;
    }

    @Override
    public List<Server> getInitialListOfServers() {
        return getUpdatedListOfServers();
    }

    @Override
    public List<Server> getUpdatedListOfServers() {
        ServiceRoute serviceRoute = serviceRoutes.getClientServiceRoutes(clientConfig.getClientName());
        if (serviceRoute == null || serviceRoute.getRoutes() == null) {
            return Collections.EMPTY_LIST;
        }

        return serviceRoute.getRoutes().stream()
                .map(r -> new Server(r))
                .collect(Collectors.toList());
    }
}

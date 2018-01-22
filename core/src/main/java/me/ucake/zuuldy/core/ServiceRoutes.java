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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 *
 * ServiceRoutes
 *
 * @author alex.Q
 * @date 2017/9/20
 */
public class ServiceRoutes {

    private AtomicBoolean inited = new AtomicBoolean(false);

    private RouteStore routeStore;

    private Map<String, ServiceRoute> serviceRoutesMap = new HashMap<>();

    private ScheduledExecutorService scheduledExecutor;

    public ServiceRoutes(RouteStore routeStore) {
        this.routeStore = routeStore;
    }

    public ServiceRoute getClientServiceRoutes(String clientName) {
        ServiceRoute res = serviceRoutesMap.get(clientName);

        startRefreshIfNecessary();

        return res;
    }

    private void startRefreshIfNecessary() {
        if (!inited.compareAndSet(false, true)) {
            return;
        }

        scheduledExecutor = Executors.newScheduledThreadPool(1);


        scheduledExecutor.scheduleAtFixedRate(() -> this.updateServiceRoutes(), 0, 1, TimeUnit.MINUTES);
    }

    private void updateServiceRoutes() {
        System.out.println("start refresh routers...");
        List<ServiceRoute> serviceRoutes = routeStore.getAllServiceRoutes();

        synchronized (serviceRoutesMap) {
            serviceRoutesMap.clear();
            serviceRoutes.forEach(sr -> {
                serviceRoutesMap.put(sr.getServiceId(), sr);
            });
        }
        System.out.println("refresh routers end!");
    }

}

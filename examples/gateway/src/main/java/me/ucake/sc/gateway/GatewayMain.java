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

package me.ucake.sc.gateway;

import me.ucake.sc.gateway.web.ServerProperties;
import me.ucake.zuuldy.core.EnableZuulDynamic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 *
 * GatewayMain
 *
 * @author alex.Q
 * @date 2017/8/6
 */
@EnableZuulProxy
@EnableEurekaClient
@SpringBootApplication
@EnableZuulDynamic
public class GatewayMain {


    public static void main(String[] args) {
        SpringApplication.run(GatewayMain.class, args);
    }
}

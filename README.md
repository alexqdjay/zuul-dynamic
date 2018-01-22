# zuul-dynamic
> zuul dynamic routes  
可以让zuul实现动态加载路由的小插件 

## Usage

1. add `@EnableZuulDynamic` to the configuration class
2. implement interface `RouteStore`, the interface contains two methods 
    1. `getAllRoutes()`
    2. `onRoutesChange(routes)`

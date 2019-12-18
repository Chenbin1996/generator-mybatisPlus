# 加载对应的环境信息
spring.profiles.active=dev

# 让控制器输出的json字符串格式更美观
spring.jackson.serialization.indent_output=true

# Server Port
server.port=8081

# 日期格式化设置
spring.jackson.date-format=yyyy-MM-dd HH:mm:ss
spring.jackson.time-zone=GMT+8

############# Mybatis Plus配置 #############
mybatis-plus.mapper-locations=classpath:/mapper/*Mapper.xml
mybatis-plus.configuration.map-underscore-to-camel-case=true
mybatis-plus.configuration.cache-enabled=true
mybatis-plus.configuration.lazy-loading-enabled=false
mybatis-plus.configuration.log-impl=org.apache.ibatis.logging.stdout.StdOutImpl

############# 分页插件PageHelper配置 #############
pagehelper.helper-dialect=mysql
pagehelper.reasonable=true
pagehelper.support-methods-arguments=true
pagehelper.pageSizeZero=true
pagehelper.params=count=countSql

############# 仿@Value取值乱码 #############
spring.banner.charset=UTF-8
spring.messages.encoding=UTF-8
spring.http.encoding.charset=UTF-8
spring.http.encoding.force=true
spring.http.encoding.enabled=true
server.tomcat.uri-encoding=UTF-8
############# Tomcat配置 #############
server.tomcat.max-threads=1000
server.tomcat.min-spare-threads=30
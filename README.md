# simple-networking-service
简单的网络通信程序

## web 即时通讯

基于 JMS(消息中间件)和Websocket

架构 

    发送消息
    
        1. web端点击发送消息
        2. 后端接口收到消息，根据发送人和接收人信息，放到对应的消息队列
     
     接收消息
     
        前置：客户端与服务器端建立websocket连接，设置了对应消息队列（每个联系人对应一个消息队列）的监听器
        
        1. 消息中间件触发消息到达事件
        2. 根据队列的不同，找到对应的websocket连接
        3. 调用websocket把消息推送到web客户端
        
    消息中间件-activeMq
    
    服务端与客户端通信-websocket
    
    前端框架-vue
    
    数据库-mysql 保存用户密码和联系人

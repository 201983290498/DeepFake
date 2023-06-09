# 工程简介
启动说明
1. 激活配置文件.在application.yaml中默认激活dev配置文件
```
spring:
  profiles:
    active: dev
```
对于配置文件,需要配置个人的QQ(正常运行),redis(正常运行),mysql(建议配本地mysql),文件服务器OSS(服务已经停止使用,导致deepfake检测实际已经停用),rabbitMQ(正常运行),后端算法模型flask.
2. 初始化数据库,通过idea与本地数据集建立联系.运行数据库初始化脚本init.sql.与配置文件在同一目录.
3. 使用maven加载项目
4. 通过命令行进入design-front文件夹.
5. 安装vue相关知识,具体看design-front中的readme.md
# 延伸阅读


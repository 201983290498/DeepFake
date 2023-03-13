FROM openjdk:11-jre
EXPOSE 8080 #后端部署端口
#复制server-1.0-SNAPSHOT到docker容器中并命名为app.jar
ADD desgin-0.0.1-SNAPSHOT.jar app.jar
#执行命令
RUN bash -c 'touch /app.jar'
ENTRYPOINT ["java", "-jar", "/app.jar", "--spring.profiles.active=pro"]


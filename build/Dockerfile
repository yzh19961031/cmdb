FROM openjdk:8-jdk-alpine
WORKDIR /data
COPY target/cmdb-1.0.jar /data/cmdb-1.0.jar

ENV LANG=C.UTF-8 \
    TZ=Asia/Shanghai \
    JAVA_OPTS=""

RUN sed -i 's/dl-cdn.alpinelinux.org/mirrors.aliyun.com/g' /etc/apk/repositories \
    && apk update \
    && apk add tzdata \
    && ln -sf /usr/share/zoneinfo/$TZ /etc/localtime \
    && echo $TZ > /etc/timezone \

EXPOSE 8900
CMD ["sh", "-c", "java $JAVA_OPTS -jar /data/cmdb-1.0.jar"]

FROM gradle:5.4.1-jdk8 AS BUILD_IMAGE

RUN mkdir /apps
COPY --chown=gradle:gradle . /apps
WORKDIR /apps

RUN gradle clean build

#2
FROM openjdk:8-jre
COPY --from=BUILD_IMAGE /apps/build/libs/telegram-bot-core.jar .
RUN mkdir keyboards
COPY src/main/resources/bot/message/config keyboards/ 

COPY startup.sh .

CMD bash startup.sh
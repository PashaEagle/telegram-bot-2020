#!/bin/bash -ex
java -jar -Dspring.profiles.active=${ENVIRONMENT} telegram-bot-core.jar --message.config.path=keyboards/
package io.crypto.beer.telegram.bot.engine.generic.services;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class GenericHandler {

    private static final String METHOD_SEPARATOR = ".";

    public Object invokeMethod(Object object, Method method, Object... args) throws InvocationTargetException {
        try {
            return method.invoke(object, args);
        } catch (IllegalAccessException | IllegalArgumentException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public Method extractMethodFromString(String argGenerationMethodPath) {
        try {
            Class<?> c = Class.forName(getMethodPath(argGenerationMethodPath));
            return c.getMethod(getMethodName(argGenerationMethodPath));
        } catch (ClassNotFoundException | NoSuchMethodException | SecurityException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public Method extractDeclaredMethodFromString(String argGenerationMethodPath, Class<?>[] args) {
        try {
            Class<?> c = Class.forName(getMethodPath(argGenerationMethodPath));
            return c.getDeclaredMethod(getMethodName(argGenerationMethodPath), args);
        } catch (ClassNotFoundException | NoSuchMethodException | SecurityException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private String getMethodPath(String argGenerationMethodPath) {
        return argGenerationMethodPath.substring(0, argGenerationMethodPath.lastIndexOf(METHOD_SEPARATOR));
    }

    private String getMethodName(String argGenerationMethodPath) {
        return argGenerationMethodPath.substring(argGenerationMethodPath.lastIndexOf(METHOD_SEPARATOR) + 1);
    }
}

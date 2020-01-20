package io.crypto.beer.telegram.bot.engine.services;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.springframework.stereotype.Service;

import io.crypto.beer.telegram.bot.engine.generic.services.GenericHandler;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MethodCallService {

    private final GenericHandler genericHandler;

    public Object invokeMethod(Object object, String argGenerationMethodPath) throws InvocationTargetException {
        Method method = genericHandler.extractMethodFromString(argGenerationMethodPath);
        return genericHandler.invokeMethod(object, method);
    }

    public Object invokeMethod(Object object, String argGenerationMethodPath, Class<?>[] classParams, Object... args) throws InvocationTargetException {
        Method method = genericHandler.extractDeclaredMethodFromString(argGenerationMethodPath, classParams);
        return genericHandler.invokeMethod(object, method, args);
    }
}
package danekerscode.keremetchat.common.aspect;

import danekerscode.keremetchat.common.annotation.AvailableForRootOrOwner;
import danekerscode.keremetchat.context.holder.UserContextHolder;
import danekerscode.keremetchat.model.enums.security.SecurityRoleType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Stream;

@Aspect
@Component
@Slf4j
public class AvailableForRootOrOwnerAspect {

    /*
    * Spring AOP supports the following AspectJ pointcut designators for use in pointcut expressions:
    The full AspectJ pointcut language supports additional pointcut designators that are not supported in Spring. These are: call, initialization, preinitialization, staticinitialization, get, set, handler, adviceexecution, withincode, cflow, cflowbelow, if, @this, and @withincode. Use of these pointcut designators in pointcut expressions interpreted by Spring AOP will result in an IllegalArgumentException being thrown.
    The set of pointcut designators supported by Spring AOP may be extended in future releases both to support more of the AspectJ pointcut designators (e.g. "if"), and potentially to support Spring specific designators such as "bean" (matching on bean name).
        execution - for matching method execution join points, this is the primary pointcut designator you will use when working with Spring AOP
        within - limits matching to join points within certain types (simply the execution of a method declared within a matching type when using Spring AOP)
        this - limits matching to join points (the execution of methods when using Spring AOP) where the bean reference (Spring AOP proxy) is an instance of the given type
        target - limits matching to join points (the execution of methods when using Spring AOP) where the target object (application object being proxied) is an instance of the given type
        args - limits matching to join points (the execution of methods when using Spring AOP) where the arguments are instances of the given types
        @target - limits matching to join points (the execution of methods when using Spring AOP) where the class of the executing object has an annotation of the given type
        @args - limits matching to join points (the execution of methods when using Spring AOP) where the runtime type of the actual arguments passed have annotations of the given type(s)
        @within - limits matching to join points within types that have the given annotation (the execution of methods declared in types with the given annotation when using Spring AOP)
        @annotation - limits matching to join points where the subject of the join point (method being executed in Spring AOP) has the given annotation
        * */
    @Before("@annotation(availableForRootOrOwner)")
    public void beforeAvailableForRootOrOwner(
            JoinPoint joinPoint,
            AvailableForRootOrOwner availableForRootOrOwner
    ) {
        var signature = (MethodSignature) joinPoint.getSignature();
        var isHttpMethodCall = this.isHttpMethod(signature);

        if (!isHttpMethodCall) {
            throw new IllegalArgumentException("AvailableForRootOrOwner annotation is only allowed for HTTP methods");
        }

        var sourceName = availableForRootOrOwner.sourceName();

        var passedUserId = this.extractUserIdFromMethodSignature(joinPoint, sourceName);
        var currentUser = UserContextHolder.getContext();

        if (currentUser.getId().equals(passedUserId) ||
                currentUser.getRoles().stream().anyMatch(role -> role.getType() == SecurityRoleType.ROLE_APPLICATION_ROOT_ADMIN)
        ) {
            return;
        }

        throw new IllegalArgumentException("User is not allowed to access this resource"); // TODO: Custom exception
    }

    private boolean isHttpMethod(MethodSignature signature) {
        return Stream.of(
                RequestMapping.class,
                GetMapping.class,
                PostMapping.class,
                DeleteMapping.class,
                PatchMapping.class,
                PutMapping.class
        ).anyMatch(annotation -> signature.getMethod().getAnnotation(annotation) != null);
    }

    private Number extractUserIdFromMethodSignature(JoinPoint joinPoint, String argName) {
        var signature = (MethodSignature) joinPoint.getSignature();
        var parameterNames = signature.getParameterNames();
        var args = joinPoint.getArgs();

        for (int i = 0; i < parameterNames.length; i++) {
            if (parameterNames[i].equals(argName)) {
                var argValue = args[i];
                if (argValue instanceof Number) {
                    return (Number) argValue;
                }
            }
        }
        throw new IllegalArgumentException("User id not found in request param");
    }

}

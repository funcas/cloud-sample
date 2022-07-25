package com.github.funcas.spc.lb;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ScanResult;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.agent.ByteBuddyAgent;
import net.bytebuddy.description.annotation.AnnotationDescription;
import net.bytebuddy.dynamic.loading.ClassReloadingStrategy;
import org.springframework.context.annotation.Import;

import java.lang.instrument.Instrumentation;

/**
 * TODO
 *
 * @author shane
 * @since 1.0
 */
public class GreyLbAgent {

    static {
        ByteBuddyAgent.install();
    }

    /**
     * jvm 参数形式启动，运行此方法
     *
     * @param agentArgs
     * @param inst
     */
    public static void premain(String agentArgs, Instrumentation inst) {
        System.out.println("premain");
        String bootClassName = "";
        Class bootClass = null;
        String pkg = "com.github.funcas";
        String routeAnnotation = "org.springframework.boot.autoconfigure.SpringBootApplication";
        try (ScanResult scanResult =
                     new ClassGraph()
                //             .verbose()               // Log to stderr
                             .enableAllInfo()         // Scan classes, methods, fields, annotations
                             .acceptPackages(pkg)     // Scan com.xyz and subpackages (omit to scan all packages)
                             .scan()) {               // Start the scan
            for (ClassInfo routeClassInfo : scanResult.getClassesWithAnnotation(routeAnnotation)) {
                // @com.xyz.Route has one required parameter
                bootClassName = routeClassInfo.getName();
                bootClass = routeClassInfo.loadClass();
                System.out.println(bootClass);
                break;
            }
        }
        System.out.println("===================> " + bootClassName);
        AnnotationDescription description = AnnotationDescription.Builder.ofType(Import.class)
                .defineTypeArray("value", new Class[]{com.github.funcas.spc.lb.DyBean.class})
                .build();



        new ByteBuddy()
                .redefine(bootClass)
                .annotateType(description)
                .make()
                .load(bootClass.getClassLoader(), ClassReloadingStrategy.fromInstalledAgent());
    }

    /**
     * 动态 attach 方式启动，运行此方法
     *
     * @param agentArgs
     * @param inst
     */
    public static void agentmain(String agentArgs, Instrumentation inst) {
        System.out.println("agentmain");
    }
}

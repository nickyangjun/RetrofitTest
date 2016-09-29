package com.nick.study;

import com.google.auto.service.AutoService;
import com.nick.study.annotation.BindClick;
import com.nick.study.annotation.ViewBind;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeVariableName;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

@AutoService(Processor.class)
public class ViewBindProcessor extends AbstractProcessor {
    private static final String BINDING_CLASS_SUFFIX = "$$ViewBinder";//生成类的后缀 以后会用反射去取
    private static final ClassName VIEW_BINDER = ClassName.get("com.nick.study.viewinject", "ViewBinder");
    private Elements elementUtils;
    private Types typeUtils;
    private Filer filer;
    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        elementUtils = processingEnv.getElementUtils();
        typeUtils = processingEnv.getTypeUtils();
        filer = processingEnv.getFiler();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new LinkedHashSet<>();
        types.add(ViewBind.class.getCanonicalName());
        return types;
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Map<String, ViewBindInfo> targetClassMap = new LinkedHashMap<>();
        Set<? extends Element> set = roundEnv.getElementsAnnotatedWith(ViewBind.class);
        for (Element element : set) {
            if (element.getKind() != ElementKind.FIELD) {
                processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "only support class field");
                break;
            }
            TypeElement enclosingElement = (TypeElement) element.getEnclosingElement();
            String packageName = getPackageName(enclosingElement);
            String className = getClassName(enclosingElement,packageName);
            String classFullPath = packageName+"."+className;
            processingEnv.getMessager().printMessage(Diagnostic.Kind.WARNING,"process: "+classFullPath);
            ViewBindInfo viewBindInfo = targetClassMap.get(classFullPath);
            if(viewBindInfo == null){
                viewBindInfo = new ViewBindInfo();
                targetClassMap.put(classFullPath,viewBindInfo);
            }
            viewBindInfo.packageName = packageName;
            viewBindInfo.className = className;
            viewBindInfo.viewBindElementList.add(element);
            viewBindInfo.typeClassName =  ClassName.bestGuess(getClassName(enclosingElement, packageName));
        }
        Set<? extends Element> clickSet = roundEnv.getElementsAnnotatedWith(BindClick.class);
        for (Element element : clickSet) {
            if (element.getKind() != ElementKind.METHOD) {
                processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "only support class method");
                break;
            }
            TypeElement enclosingElement = (TypeElement) element.getEnclosingElement();
            String packageName = getPackageName(enclosingElement);
            String className = getClassName(enclosingElement,packageName);
            String classFullPath = packageName+"."+className;
            processingEnv.getMessager().printMessage(Diagnostic.Kind.WARNING,"process: "+classFullPath);
            ViewBindInfo viewBindInfo = targetClassMap.get(classFullPath);
            if(viewBindInfo == null){
                viewBindInfo = new ViewBindInfo();
                targetClassMap.put(classFullPath,viewBindInfo);
            }
            viewBindInfo.packageName = packageName;
            viewBindInfo.className = className;
            viewBindInfo.viewClickElementList.add(element);
            viewBindInfo.typeClassName =  ClassName.bestGuess(getClassName(enclosingElement, packageName));
        }
        buildViewBindClass(targetClassMap);
        return false;
    }

    private void buildViewBindClass( Map<String, ViewBindInfo> targetClassMap ){
        if(targetClassMap.size() == 0){
            return;
        }
        for (Map.Entry<String, ViewBindInfo> item : targetClassMap.entrySet()) {
            String newClassName = item.getValue().className+BINDING_CLASS_SUFFIX;
            String packageName = item.getValue().packageName;
            ClassName typeClassName = item.getValue().typeClassName;
            String methodName = "viewBind";
            MethodSpec.Builder viewBindMethodBuilder = MethodSpec.methodBuilder(methodName)
                    .addModifiers(Modifier.PUBLIC)
                    .returns(TypeName.VOID)
                    .addAnnotation(Override.class)
                    .addParameter(typeClassName, "target", Modifier.FINAL);

            for(Element element : item.getValue().viewBindElementList){
                int id = element.getAnnotation(ViewBind.class).value();
                ClassName viewClass = ClassName.bestGuess(element.asType().toString());
                viewBindMethodBuilder.addStatement("target.$L=($T)target.findViewById($L)",element.getSimpleName().toString(),viewClass, id);
            }

            if(item.getValue().viewClickElementList.size() > 0){
                viewBindMethodBuilder.addStatement("$T listener", ClassTypeUtils.ANDROID_ON_CLICK_LISTENER);
            }
            for(Element element : item.getValue().viewClickElementList){
                int id = element.getAnnotation(BindClick.class).id();
                // declare OnClickListener anonymous class
                TypeSpec listener = TypeSpec.anonymousClassBuilder("")
                        .addSuperinterface(ClassTypeUtils.ANDROID_ON_CLICK_LISTENER)
                        .addMethod(MethodSpec.methodBuilder("onClick")
                                .addAnnotation(Override.class)
                                .addModifiers(Modifier.PUBLIC)
                                .returns(TypeName.VOID)
                                .addParameter(ClassTypeUtils.ANDROID_VIEW, "view")
                                .addStatement("target.$N()",((ExecutableElement)element).getSimpleName())
                                .build())
                        .build();
                viewBindMethodBuilder.addStatement("listener = $L ", listener);
                // set listeners
                viewBindMethodBuilder.addStatement("target.findViewById($L).setOnClickListener(listener)", id);
            }

            MethodSpec viewBindMethod = viewBindMethodBuilder.build();
            TypeSpec viewBind = TypeSpec.classBuilder(newClassName)
                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                    .addTypeVariable(TypeVariableName.get("T", typeClassName))
                    .addSuperinterface(ParameterizedTypeName.get(VIEW_BINDER, typeClassName))
                    .addMethod(viewBindMethod)
                    .build();

            JavaFile javaFile = JavaFile.builder(packageName, viewBind).build();
            try {
                javaFile.writeTo(processingEnv.getFiler());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String getPackageName(TypeElement type) {
        return elementUtils.getPackageOf(type).getQualifiedName().toString();
    }

    private static String getClassName(TypeElement type, String packageName) {
        int packageLen = packageName.length() + 1;
        return type.getQualifiedName().toString().substring(packageLen).replace('.', '$');
    }

    private class ViewBindInfo{
        String packageName;
        String className;
        ClassName typeClassName;
        List<Element> viewBindElementList = new ArrayList<>();
        List<Element> viewClickElementList = new ArrayList<>();
    }
}

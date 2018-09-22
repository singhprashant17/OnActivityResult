package com.prashant.onactivityresult.annotationprocessor;

import com.google.auto.service.AutoService;
import com.prashant.onactivityresult.annotation.OnActivityResult;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.JavaFileObject;

import static com.prashant.onactivityresult.annotation.OnActivityResult.GENERATED_FILE_NAME_SUFFIX;

/**
 * @author Prashant Singh
 */
@AutoService(Processor.class)
@SupportedSourceVersion(SourceVersion.RELEASE_7)
@SupportedAnnotationTypes("com.prashant.onactivityresult.annotation.OnActivityResult")
public class ActivityResultProcessor extends AbstractProcessor {

    // list to maintain the classes containing the annotated methods with the list of annotated methods in that class
    private final HashMap<Element, ArrayList<Element>> classAndItsMethodsMap = new HashMap<>();

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnvironment) {

        // validations for all the annotated methods
        for (Element element : roundEnvironment.getElementsAnnotatedWith(OnActivityResult.class)) {
            final Element klass = element.getEnclosingElement();

            // although the annotation has @Target(ElementType.METHOD) and javac gives the validation.
            if (element.getKind() != ElementKind.METHOD) {
                error(element, "annotation applicable only to methods");
            }

            // get the modifiers of the annotated method
            Set<Modifier> modifiers = element.getModifiers();

            // check if the annotated method is not private or protected
            if (modifiers.contains(Modifier.PRIVATE) || modifiers.contains(Modifier.PROTECTED)) {
                error(element, "annotated method cannot be private or protected");
            }

            // check if the annotated method is not static
            if (modifiers.contains(Modifier.STATIC)) {
                error(element, "annotated method must not be static");
            }

            ExecutableType methodExe = (ExecutableType) element.asType();
            // check if the annotated method has exactly 1 parameter
            if (methodExe.getParameterTypes().size() != 1) {
                error(element, "annotated method must have exactly one parameter");
            }

            Types types = processingEnv.getTypeUtils();
            Elements elems = processingEnv.getElementUtils();

            TypeMirror actual = methodExe.getParameterTypes().get(0);
            TypeMirror expected = elems.getTypeElement("android.content.Intent").asType();

            // check if the annotated method parameter is of type "android.content.Intent"
            if (!types.isSameType(actual, expected)) {
                error(element, "annotated method parameter is not of type android.content.Intent");
            }

            // add the class element is not already added
            if (!classAndItsMethodsMap.containsKey(klass)) {
                classAndItsMethodsMap.put(klass, new ArrayList<Element>());
            }
            // add the method element
            classAndItsMethodsMap.get(klass).add(element);
        }

        generateClassFiles();

        return false;
    }

    private static String getGeneratedName(final int requestCode, final int resultCode) {
        final StringBuilder methodName = new StringBuilder("requestCode_");
        if (requestCode < 0) {
            methodName.append("neg");
        }
        methodName.append(Math.abs((long) requestCode)).append("_resultCode_");
        if (resultCode < 0) {
            methodName.append("neg");
        }
        methodName.append(Math.abs((long) resultCode));
        return methodName.toString();
    }

    private void error(Element method, String message) {
        throw new IllegalStateException(method.getSimpleName() + " >>>>> " + message);
    }

    @SuppressWarnings("UseOfSystemOutOrSystemErr")
    private void print(String message) {
        System.out.println(message);
    }

    private void generateClassFiles() {
        for (Map.Entry<Element, ArrayList<Element>> entry : classAndItsMethodsMap.entrySet()) {
            Element klass = entry.getKey();

            final String className = processingEnv.getElementUtils().getBinaryName((TypeElement) klass).toString();
            final String packageName = processingEnv.getElementUtils().getPackageOf(klass).getQualifiedName().toString();

            // writing the java source file
            PrintWriter out = null;
            try {
                String hookerClassName = className + GENERATED_FILE_NAME_SUFFIX;
                JavaFileObject javaFileObject = processingEnv.getFiler().createSourceFile(hookerClassName);
                out = new PrintWriter(javaFileObject.openWriter());

                out.print("package ");
                out.print(packageName);
                out.println(";");
                out.println();

                out.print("public class ");
                int lastDot = className.lastIndexOf('.');
                String hookerSimpleClassName = hookerClassName.substring(lastDot + 1);
                out.print(hookerSimpleClassName);
                out.println(" {");
                out.println();

                out.print(((TypeElement) klass).getQualifiedName());
                out.println(" target;");
                out.println();

                out.print("public ");
                out.print(hookerSimpleClassName);
                out.print("(");
                out.print(((TypeElement) klass).getQualifiedName());
                out.print(" target) {");
                out.println();
                out.print("this.target = target;");
                out.println();
                out.print("}");
                out.println();

                List<String> generatedMethodNames = new ArrayList<>();

                for (Element name : entry.getValue()) {

                    final OnActivityResult annotation = name.getAnnotation(OnActivityResult.class);
                    final int requestCode = annotation.requestCode();
                    final int resultCode = annotation.resultCode();

                    final String methodName = getGeneratedName(requestCode, resultCode);

                    if (generatedMethodNames.contains(methodName)) {
                        //error(name, "Duplicate method. OnActivityResult must be already applied to another method.");
                        continue;
                    }

                    generatedMethodNames.add(methodName);

                    out.print("public void ");
                    out.print(methodName);
                    out.print("(android.content.Intent data) {");
                    out.println();
                    out.print("this.target.");
                    out.print(name.getSimpleName());
                    out.print("(data);");
                    out.println();
                    out.print("}");
                    out.println();
                }

                out.println("}");
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (out != null) {
                    out.close();
                }
            }
        }
    }
}

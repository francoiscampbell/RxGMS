package xyz.fcampbell.rxgms.processor

import com.squareup.javapoet.*
import xyz.fcampbell.rxgms.annotation.GmsToRx
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.ElementKind
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement
import javax.lang.model.type.DeclaredType
import javax.lang.model.type.MirroredTypeException
import javax.lang.model.type.TypeKind
import javax.lang.model.type.TypeMirror
import javax.lang.model.util.Elements
import javax.lang.model.util.Types

class RxGmsProcessor : AbstractProcessor() {
    private lateinit var typeUtils: Types
    private lateinit var elementUtils: Elements
    private lateinit var filer: Filer
    private lateinit var messager: Messager

    override fun getSupportedSourceVersion(): SourceVersion {
        return SourceVersion.latestSupported()
    }

    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        return mutableSetOf(GmsToRx::class.java.canonicalName)
    }

    override fun init(environment: ProcessingEnvironment) {
        super.init(environment)
        typeUtils = environment.typeUtils
        elementUtils = environment.elementUtils
        filer = environment.filer
        messager = environment.messager
    }

    override fun process(typeElements: MutableSet<out TypeElement>, roundEnvironment: RoundEnvironment): Boolean {
        roundEnvironment.getElementsAnnotatedWith(GmsToRx::class.java).forEach { //annotated classes
            val annotatedType = it as TypeElement
            val gmsToRxAnnotation = annotatedType.getAnnotation(GmsToRx::class.java)
            val sourceGmsInterface = typeUtils.asElement(getTypeMirror(gmsToRxAnnotation))

            val methods = sourceGmsInterface.enclosedElements
                    .filter { element -> element.kind == ElementKind.METHOD }
                    .map { method -> method as ExecutableElement }
//                    .flatMap { method ->
//                        messager.printMessage(Diagnostic.Kind.NOTE, "Method: ${method.simpleName}")
//                        method.parameters
//                    }
//                    .forEach { parameter ->
//                        messager.printMessage(Diagnostic.Kind.NOTE, "Parameter: ${parameter.simpleName}: ${parameter.asType()}")
//                        messager.printMessage(Diagnostic.Kind.NOTE, "Parameter: ${parameter.simpleName}")
//                    }
                    .map { method ->
                        val methodBuilder = MethodSpec.methodBuilder(method.simpleName.toString())
                        val parameters = method.parameters
                                .filter { parameter -> !parameter.asType().toString().contains("GoogleApiClient") }
                                .map { parameter ->
                                    val typeName = TypeName.get(parameter.asType())
                                    ParameterSpec.builder(typeName, parameter.simpleName.toString().replace("\n", ""))
                                            .addModifiers(Modifier.FINAL)
                                            .build()
                                }
                        methodBuilder.addParameters(parameters)

                        if (method.returnType.kind != TypeKind.VOID) {
                            val returnType = typeUtils.asElement(method.returnType) as TypeElement
                            when {
                                returnType.simpleName.contains("PendingResult") -> {
                                    val className = ClassName.get("rx", "Observable")
                                    val returnTypeArguments = (method.returnType as DeclaredType).typeArguments
                                    val genericTypeNames = returnTypeArguments.map { TypeName.get(it) }.toTypedArray()
                                    methodBuilder.returns(ParameterizedTypeName.get(className, *genericTypeNames))

                                    val apiClientClassName = ClassName.get("com.google.android.gms.common.api", "GoogleApiClient")
                                    val functionType = TypeSpec.anonymousClassBuilder("")
                                            .addSuperinterface(ParameterizedTypeName.get(
                                                    ClassName.get("kotlin.jvm.functions", "Function1"),
                                                    apiClientClassName,
                                                    ClassName.get(returnType)))
                                            .addMethod(MethodSpec.methodBuilder("invoke")
                                                    .addAnnotation(Override::class.java)
                                                    .addModifiers(Modifier.PUBLIC)
                                                    .addParameter(apiClientClassName, "it")
                                                    .returns(TypeName.get(method.returnType))
                                                    .addCode("return com.google.android.gms.games.Games.Achievements.${method.simpleName}(it")
                                                    .addCode(parameters.map {
                                                        ", ${it.name}"
                                                    }.joinToString(""))
                                                    .addCode(");\n")
                                                    .build())
                                            .build()
                                    methodBuilder.addStatement("return fromPendingResult(\$L)", functionType)
                                }
                                else -> {
                                    val className = ClassName.get("rx", "Observable")
                                    val genericType = TypeName.get(returnType.asType())
                                    methodBuilder.returns(ParameterizedTypeName.get(className, genericType))
                                }
                            }
                        } else {
                            methodBuilder.returns(ClassName.get("rx", "Completable"))
                        }
                        methodBuilder.build()
                    }
            val defaultConstructor = MethodSpec.constructorBuilder()
                    .addParameter(ClassName.get("xyz.fcampbell.rxgms.common", "ApiClientDescriptor"), "apiClientDescriptor")
                    .addParameter(ArrayTypeName.of(ClassName.get("xyz.fcampbell.rxgms.common", "ApiDescriptor")), "apis")
                    .varargs()
                    .addStatement("super(\$N, \$N)", "apiClientDescriptor", "apis")
                    .build()
            val secondaryConstructor = MethodSpec.constructorBuilder()
                    .addParameter(ClassName.get("android.content", "Context"), "context")
                    .addParameter(ArrayTypeName.of(ClassName.get("xyz.fcampbell.rxgms.common", "ApiDescriptor")), "apis")
                    .varargs()
                    .addStatement("super(new ApiDescriptor(\$N), \$N)", "context", "apis")
                    .build()
            val genClass = TypeSpec.classBuilder("Rx${it.simpleName}")
                    .superclass(ClassName.get("xyz.fcampbell.rxgms.common", "RxGmsApi"))
                    .addMethod(defaultConstructor)
                    .addMethod(secondaryConstructor)
                    .addMethods(methods)
                    .build()

            JavaFile.builder(javaClass.`package`.name, genClass)
                    .build()
                    .writeTo(filer)
        }
        return true
    }

    private fun getTypeMirror(gmsToRx: GmsToRx): TypeMirror {
        try {
            return gmsToRx.gmsInterface as TypeMirror //should throw
        } catch (e: MirroredTypeException) {
            return e.typeMirror //return the typemirror
        }
    }
}

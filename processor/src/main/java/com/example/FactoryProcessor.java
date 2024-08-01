package com.example;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

import com.google.auto.service.AutoService;

@AutoService(Processor.class)
public class FactoryProcessor extends AbstractProcessor {

	private Types typeUtils;
	private Elements elementUtils;
	private Filer filer;
	private Messager messager;

	private final Map<String, FactoryGroupedClasses> groupToFactoryClasses = new HashMap<>();


	@Override
	public synchronized void init(ProcessingEnvironment processingEnv) {
		super.init( processingEnv );
		this.typeUtils = processingEnv.getTypeUtils();
		this.elementUtils = processingEnv.getElementUtils();
		this.filer = processingEnv.getFiler();
		this.messager = processingEnv.getMessager();
	}

	@Override
	public Set<String> getSupportedAnnotationTypes() {
		return Set.of( Factory.class.getCanonicalName() );
	}

	@Override
	public SourceVersion getSupportedSourceVersion() {
		return SourceVersion.RELEASE_9;
	}

	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
		try {
			for ( Element annotatedElement : roundEnv.getElementsAnnotatedWith( Factory.class ) ) {
				// Check if a class is annotated with @Factory
				validateAnnotationPosition( annotatedElement );
				TypeElement typeElement = (TypeElement) annotatedElement;

				FactoryAnnotatedClass factoryAnnotatedClass = new FactoryAnnotatedClass( typeElement );
				String groupName = factoryAnnotatedClass.getQualifiedFactoryGroupName();
				FactoryGroupedClasses factoryGroupedClasses1 = groupToFactoryClasses.get( groupName );
				if ( factoryGroupedClasses1 == null ) {
					factoryGroupedClasses1 = new FactoryGroupedClasses( groupName );
					groupToFactoryClasses.put(
							groupName,
							factoryGroupedClasses1
					);
				}
				factoryGroupedClasses1.add( factoryAnnotatedClass );
			}

			for ( FactoryGroupedClasses groupedClasses : groupToFactoryClasses.values() ) {
				groupedClasses.generateCode( elementUtils, filer );
			}
			groupToFactoryClasses.clear();
		}
		catch (ProcessingException e) {
			error( e.getElement(), e.getMessage() );
		}
		catch (IOException e) {
			error( null, e.getMessage() );
		}
		return true;
	}

	private void error(Element e, String message) {
		messager.printMessage( Diagnostic.Kind.ERROR, message, e );
	}

	public void log(String message) {
		messager.printMessage( Diagnostic.Kind.NOTE, message );
	}

	private void validateAnnotationPosition(Element annotatedElement) {
		if ( annotatedElement.getKind() != ElementKind.CLASS ) {
			throw new ProcessingException(
					annotatedElement,
					"The " + Factory.class.getSimpleName() + " should annotated " + " on class"
			);
		}

		if ( annotatedElement.getModifiers().contains( Modifier.ABSTRACT ) ) {
			throw new ProcessingException(
					annotatedElement,
					"The " + Factory.class.getSimpleName() + " should not be abstract"
			);
		}
	}
}

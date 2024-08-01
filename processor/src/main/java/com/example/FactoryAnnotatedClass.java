package com.example;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.MirroredTypeException;

import org.apache.commons.lang3.StringUtils;

public class FactoryAnnotatedClass {

	private String simpleFactoryGroupName;
	private String qualifiedGroupClassName;
	private TypeElement classElement;
	private Factory annotation;
	private String id;

	public FactoryAnnotatedClass(TypeElement typeElement) {
		this.classElement = typeElement;
		this.annotation = classElement.getAnnotation( Factory.class );
		this.id = this.annotation.id();
		if ( StringUtils.isEmpty( this.id ) ) {
			throw new ProcessingException(
					typeElement,
					"id() in %s for class %s is null or empty! please check again!!",
					Factory.class.getSimpleName(),
					classElement.getQualifiedName()
			);
		}

		try {
			Class<?> clazz = annotation.type();
			this.qualifiedGroupClassName = clazz.getCanonicalName();
			this.simpleFactoryGroupName = clazz.getSimpleName();
		}
		catch (MirroredTypeException mte) {
			DeclaredType classTypeMirror = (DeclaredType) mte.getTypeMirror();
			TypeElement classTypeElement = (TypeElement) classTypeMirror.asElement();
			qualifiedGroupClassName = classTypeElement.getQualifiedName().toString();
			simpleFactoryGroupName = classTypeElement.getSimpleName().toString();
		}
	}

	/**
	 * Get the id as specified in {@link Factory#id()}.
	 * return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * Get the full qualified name of the type specified in  {@link Factory#type()}.
	 *
	 * @return qualified name
	 */
	public String getQualifiedFactoryGroupName() {
		return qualifiedGroupClassName;
	}

	/**
	 * Get the simple name of the type specified in  {@link Factory#type()}.
	 *
	 * @return qualified name
	 */
	public String getSimpleFactoryGroupName() {
		return simpleFactoryGroupName;
	}

	/**
	 * The original element that was annotated with @Factory
	 */
	public TypeElement getTypeElement() {
		return classElement;
	}
}

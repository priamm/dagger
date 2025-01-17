/*
 * Copyright (C) 2014 The Dagger Authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dagger.internal.codegen;

import static dagger.internal.codegen.binding.MapKeys.getUnwrappedMapKeyType;
import static javax.lang.model.element.ElementKind.ANNOTATION_TYPE;

import androidx.room.compiler.processing.XMessager;
import androidx.room.compiler.processing.XTypeElement;
import androidx.room.compiler.processing.compat.XConverters;
import com.google.auto.common.MoreTypes;
import com.google.common.collect.ImmutableSet;
import com.squareup.javapoet.ClassName;
import dagger.MapKey;
import dagger.internal.codegen.javapoet.TypeNames;
import dagger.internal.codegen.langmodel.DaggerTypes;
import dagger.internal.codegen.validation.MapKeyValidator;
import dagger.internal.codegen.validation.TypeCheckingProcessingStep;
import dagger.internal.codegen.validation.ValidationReport;
import dagger.internal.codegen.writing.AnnotationCreatorGenerator;
import dagger.internal.codegen.writing.UnwrappedMapKeyGenerator;
import javax.inject.Inject;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;

/**
 * The annotation processor responsible for validating the mapKey annotation and auto-generate
 * implementation of annotations marked with {@link MapKey @MapKey} where necessary.
 */
final class MapKeyProcessingStep extends TypeCheckingProcessingStep<XTypeElement> {
  private final XMessager messager;
  private final DaggerTypes types;
  private final MapKeyValidator mapKeyValidator;
  private final AnnotationCreatorGenerator annotationCreatorGenerator;
  private final UnwrappedMapKeyGenerator unwrappedMapKeyGenerator;

  @Inject
  MapKeyProcessingStep(
      XMessager messager,
      DaggerTypes types,
      MapKeyValidator mapKeyValidator,
      AnnotationCreatorGenerator annotationCreatorGenerator,
      UnwrappedMapKeyGenerator unwrappedMapKeyGenerator) {
    this.messager = messager;
    this.types = types;
    this.mapKeyValidator = mapKeyValidator;
    this.annotationCreatorGenerator = annotationCreatorGenerator;
    this.unwrappedMapKeyGenerator = unwrappedMapKeyGenerator;
  }

  @Override
  public ImmutableSet<ClassName> annotationClassNames() {
    return ImmutableSet.of(TypeNames.MAP_KEY);
  }

  @Override
  protected void process(XTypeElement xElement, ImmutableSet<ClassName> annotations) {
    // TODO(bcorso): Remove conversion to javac type and use XProcessing throughout.
    TypeElement mapKeyAnnotationType = XConverters.toJavac(xElement);
    ValidationReport mapKeyReport = mapKeyValidator.validate(mapKeyAnnotationType);
    mapKeyReport.printMessagesTo(messager);

    if (mapKeyReport.isClean()) {
      MapKey mapkey = mapKeyAnnotationType.getAnnotation(MapKey.class);
      if (!mapkey.unwrapValue()) {
        annotationCreatorGenerator.generate(mapKeyAnnotationType, messager);
      } else if (unwrappedValueKind(mapKeyAnnotationType).equals(ANNOTATION_TYPE)) {
        unwrappedMapKeyGenerator.generate(mapKeyAnnotationType, messager);
      }
    }
  }

  private ElementKind unwrappedValueKind(TypeElement mapKeyAnnotationType) {
    DeclaredType unwrappedMapKeyType =
        getUnwrappedMapKeyType(MoreTypes.asDeclared(mapKeyAnnotationType.asType()), types);
    return unwrappedMapKeyType.asElement().getKind();
  }
}

/*
 * Copyright (C) 2016 The Dagger Authors.
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

package dagger.internal.codegen.validation;

import androidx.room.compiler.processing.XElement;
import dagger.internal.codegen.binding.InjectionAnnotations;
import dagger.internal.codegen.javapoet.TypeNames;

abstract class BindsInstanceElementValidator<E extends XElement>
    extends BindingElementValidator<E> {
  BindsInstanceElementValidator(InjectionAnnotations injectionAnnotations) {
    super(
        TypeNames.BINDS_INSTANCE,
        AllowsMultibindings.NO_MULTIBINDINGS,
        AllowsScoping.NO_SCOPING,
        injectionAnnotations);
  }

  @Override
  protected final String bindingElements() {
    // Even though @BindsInstance may be placed on methods, the subject of errors is the
    // parameter
    return "@BindsInstance parameters";
  }

  @Override
  protected final String bindingElementTypeVerb() {
    return "be";
  }
}

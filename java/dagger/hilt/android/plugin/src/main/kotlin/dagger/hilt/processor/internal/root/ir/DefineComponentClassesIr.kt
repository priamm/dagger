package dagger.hilt.processor.internal.root.ir

import com.squareup.javapoet.ClassName

data class DefineComponentClassesIr(
  val fqName: ClassName,
  val component: ClassName,
)
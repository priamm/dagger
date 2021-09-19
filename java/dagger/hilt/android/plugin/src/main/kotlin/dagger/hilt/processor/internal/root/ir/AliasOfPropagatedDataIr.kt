package dagger.hilt.processor.internal.root.ir

import com.squareup.javapoet.ClassName

data class AliasOfPropagatedDataIr(
  val fqName: ClassName,
  val defineComponentScope: ClassName,
  val alias: ClassName,
)

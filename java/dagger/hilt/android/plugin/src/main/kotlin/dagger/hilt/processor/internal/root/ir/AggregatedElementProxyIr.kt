package dagger.hilt.processor.internal.root.ir

import com.squareup.javapoet.ClassName

data class AggregatedElementProxyIr(
  val fqName: ClassName,
  val value: ClassName,
)
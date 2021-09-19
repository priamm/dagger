package dagger.hilt.processor.internal.root.ir

import com.squareup.javapoet.ClassName

data class AggregatedEarlyEntryPointIr(
  val fqName: ClassName,
  val earlyEntryPoint: ClassName,
)
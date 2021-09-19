package dagger.hilt.processor.internal.root.ir

import com.squareup.javapoet.ClassName

data class ProcessedRootSentinelIr(
  val fqName: ClassName,
  val roots: List<ClassName>
)